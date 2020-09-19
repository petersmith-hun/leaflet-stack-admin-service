package hu.psprog.leaflet.lsas.core.client.impl;

import hu.psprog.leaflet.lsas.core.client.DockerContainerStatisticsClient;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.domain.DockerPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Implementation of {@link DockerContainerStatisticsClient}.
 *
 * @author Peter Smith
 */
@Component
public class DockerContainerStatisticsClientImpl implements DockerContainerStatisticsClient {

    private static final ParameterizedTypeReference<List<ContainerModel>> CONTAINER_MODEL_LIST_TYPEREF
            = new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    @Autowired
    public DockerContainerStatisticsClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<List<ContainerModel>> getExistingContainers() {

        return webClient
                .get()
                .uri(DockerPath.CONTAINERS.getURI())
                .retrieve()
                .bodyToMono(CONTAINER_MODEL_LIST_TYPEREF);
    }

    @Override
    public Mono<ContainerDetailsModel> getContainerDetails(String containerID) {

        return webClient
                .get()
                .uri(DockerPath.CONTAINER_DETAILS.getURI(), containerID)
                .retrieve()
                .bodyToMono(ContainerDetailsModel.class)
                .onErrorResume(throwable -> Mono.empty());
    }

    @Override
    public Flux<ContainerRuntimeStatsModel> getContainerRuntimeStats(String containerID) {

        return webClient
                .get()
                .uri(DockerPath.CONTAINER_STATS.getURI(), containerID)
                .retrieve()
                .bodyToFlux(ContainerRuntimeStatsModel.class)
                .onErrorResume(throwable -> Flux.empty());
    }
}
