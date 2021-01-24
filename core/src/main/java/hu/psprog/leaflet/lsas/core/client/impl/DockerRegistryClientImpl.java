package hu.psprog.leaflet.lsas.core.client.impl;

import hu.psprog.leaflet.lsas.core.client.DockerRegistryClient;
import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerRepositories;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTagManifest;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTags;
import hu.psprog.leaflet.lsas.core.domain.DockerRegistryPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DockerRegistryClient}.
 *
 * @author Peter Smith
 */
@Component
public class DockerRegistryClientImpl implements DockerRegistryClient {

    private final Map<String, WebClient> clientMap;

    @Autowired
    public DockerRegistryClientImpl(ServiceRegistrations serviceRegistrations, Jackson2JsonDecoder dockerManifestDecoder) {
        this.clientMap = prepareClientMap(serviceRegistrations, dockerManifestDecoder);
    }

    @Override
    public Mono<DockerRepositories> getRepositories(String registryID) {

        return getWebClient(registryID)
                .method(HttpMethod.GET)
                .uri(DockerRegistryPath.REPOSITORIES.getUri())
                .retrieve()
                .bodyToMono(DockerRepositories.class);
    }

    @Override
    public Mono<DockerTags> getRepositoryTags(String registryID, String repositoryID) {

        return getWebClient(registryID)
                .method(HttpMethod.GET)
                .uri(String.format(DockerRegistryPath.TAGS.getUri(), repositoryID))
                .retrieve()
                .bodyToMono(DockerTags.class);
    }

    @Override
    public Mono<DockerTagManifest> getTagManifest(String registryID, String repositoryID, String tag) {

        return getWebClient(registryID)
                .method(HttpMethod.GET)
                .uri(String.format(DockerRegistryPath.TAG_MANIFEST.getUri(), repositoryID, tag))
                .retrieve()
                .bodyToMono(DockerTagManifest.class);
    }

    @Override
    public Mono<String> getTagDigest(String registryID, String repositoryID, String tag) {

        return getWebClient(registryID)
                .method(HttpMethod.GET)
                .uri(String.format(DockerRegistryPath.TAG_MANIFEST.getUri(), repositoryID, tag))
                .header("Accept", "application/vnd.docker.distribution.manifest.v2+json")
                .retrieve()
                .toEntity(Void.class)
                .map(responseEntity -> Optional
                        .ofNullable(responseEntity.getHeaders().getFirst("Docker-Content-Digest"))
                        .orElse("unknown"));
    }

    @Override
    public void deleteTagByDigest(String registryID, String repositoryID, String tagDigest) {

        getWebClient(registryID)
                .method(HttpMethod.DELETE)
                .uri(String.format(DockerRegistryPath.TAG_MANIFEST.getUri(), repositoryID, tagDigest))
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    private WebClient getWebClient(String registryID) {

        WebClient selectedWebClient = clientMap.get(registryID);
        if (Objects.isNull(selectedWebClient)) {
            throw new IllegalArgumentException(String.format("Specified registryID=%s has no mapped client.", registryID));
        }

        return selectedWebClient;
    }

    private Map<String, WebClient> prepareClientMap(ServiceRegistrations serviceRegistrations, Jackson2JsonDecoder dockerManifestDecoder) {

        return serviceRegistrations.getDockerIntegration()
                .getRegistryCatalog()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> buildWebClient(entry.getValue(), dockerManifestDecoder)));
    }

    private WebClient buildWebClient(ServiceRegistrations.DockerRegistry dockerRegistry, Jackson2JsonDecoder dockerManifestDecoder) {

        String basicAuth = String.format("%s:%s", dockerRegistry.getUsername(), dockerRegistry.getPassword());
        String authHeader = String.format("Basic %s", Base64Utils.encodeToString(basicAuth.getBytes()));

        return WebClient.builder()
                .baseUrl(dockerRegistry.getHost())
                .defaultHeader("Authorization", authHeader)
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .customCodecs()
                        .registerWithDefaultConfig(dockerManifestDecoder))
                .build();
    }
}
