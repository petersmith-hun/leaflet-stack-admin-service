package hu.psprog.leaflet.lsas.core.client.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.lsas.core.client.DockerRegistryClient;
import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerRepositories;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTagManifest;
import hu.psprog.leaflet.lsas.core.dockerapi.DockerTags;
import hu.psprog.leaflet.lsas.core.domain.DockerRegistryPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of {@link DockerRegistryClient}.
 *
 * @author Peter Smith
 */
@Component
public class DockerRegistryClientImpl implements DockerRegistryClient {

    private final Map<String, WebClient> clientMap;
    private final ObjectMapper objectMapper;

    @Autowired
    public DockerRegistryClientImpl(ServiceRegistrations serviceRegistrations, ObjectMapper objectMapper) {
        this.clientMap = prepareClientMap(serviceRegistrations);
        this.objectMapper = objectMapper;
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
                .bodyToMono(String.class)
                .map(responseContent -> {
                    try {
                        return objectMapper.readValue(responseContent, DockerTagManifest.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to read Docker Registry manifest response", e);
                    }
                });
    }

    private WebClient getWebClient(String registryID) {

        WebClient selectedWebClient = clientMap.get(registryID);
        if (Objects.isNull(selectedWebClient)) {
            throw new IllegalArgumentException(String.format("Specified registryID=%s has no mapped client.", registryID));
        }

        return selectedWebClient;
    }

    private Map<String, WebClient> prepareClientMap(ServiceRegistrations serviceRegistrations) {

        return serviceRegistrations.getDockerIntegration()
                .getRegistryCatalog()
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, this::buildWebClient));
    }

    private WebClient buildWebClient(Map.Entry<String, ServiceRegistrations.DockerRegistry> entry) {

        String basicAuth = String.format("%s:%s", entry.getValue().getUsername(), entry.getValue().getPassword());
        String authHeader = String.format("Basic %s", Base64Utils.encodeToString(basicAuth.getBytes()));

        return WebClient.builder()
                .baseUrl(entry.getValue().getHost())
                .defaultHeader("Authorization", authHeader)
                .build();
    }
}
