package hu.psprog.leaflet.lsas.core.service.impl;

import com.jayway.jsonpath.JsonPath;
import hu.psprog.leaflet.lsas.core.client.DockerRegistryClient;
import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerRepositories;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTagManifest;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTags;
import hu.psprog.leaflet.lsas.core.domain.DockerRegistryContent;
import hu.psprog.leaflet.lsas.core.domain.DockerRepository;
import hu.psprog.leaflet.lsas.core.domain.DockerTag;
import hu.psprog.leaflet.lsas.core.service.DockerRegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DockerRegistryService}.
 *
 * @author Peter Smith
 */
@Service
public class DockerRegistryServiceImpl implements DockerRegistryService {

    private final DockerRegistryClient dockerRegistryClient;
    private final Map<String, String> registryMap;

    @Autowired
    public DockerRegistryServiceImpl(ServiceRegistrations serviceRegistrations, DockerRegistryClient dockerRegistryClient) {
        this.registryMap = createSimpleRegistryMap(serviceRegistrations);
        this.dockerRegistryClient = dockerRegistryClient;
    }

    @Override
    public Map<String, String> listRegistries() {
        return registryMap;
    }

    @Override
    public Mono<DockerRegistryContent> getRepositories(String registryID) {

        return dockerRegistryClient.getRepositories(registryID)
                .map(DockerRepositories::getRepositories)
                .map(repositories -> new DockerRegistryContent(registryID, repositories));
    }

    @Override
    public Mono<DockerRepository> getRepositoryDetails(String registryID, String repositoryID) {

        return dockerRegistryClient.getRepositoryTags(registryID, repositoryID)
                .filter(dockerTags -> Objects.nonNull(dockerTags.getTags()))
                .map(DockerTags::getTags)
                .flatMapMany(Flux::fromIterable)
                .flatMap(tag -> dockerRegistryClient.getTagManifest(registryID, repositoryID, tag))
                .map(this::mapToDockerTag)
                .sort(Comparator
                        .comparing(DockerTag::getCreated)
                        .thenComparing(DockerTag::getName)
                        .reversed())
                .collectList()
                .map(dockerTags -> new DockerRepository(registryID, repositoryID, dockerTags));
    }

    @Override
    public void deleteImageByTag(String registryID, String repositoryID, String tag) {

        dockerRegistryClient.getTagDigest(registryID, repositoryID, tag)
                .subscribe(dockerTagDigest -> dockerRegistryClient
                        .deleteTagByDigest(registryID, repositoryID, dockerTagDigest));
    }

    private Map<String, String> createSimpleRegistryMap(ServiceRegistrations serviceRegistrations) {

        return serviceRegistrations.getDockerIntegration()
                .getRegistryCatalog()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getHost()));
    }

    private DockerTag mapToDockerTag(DockerTagManifest dockerTagManifest) {

        ZonedDateTime created = dockerTagManifest.getHistory().stream()
                .map(DockerTagManifest.DockerTagHistory::getV1Compatibility)
                .map(metaString -> JsonPath.read(metaString, "$.created"))
                .map(String::valueOf)
                .map(ZonedDateTime::parse)
                .max(Comparator.comparing(Function.identity()))
                .orElse(null);

        return new DockerTag(dockerTagManifest.getTag(), created);
    }
}
