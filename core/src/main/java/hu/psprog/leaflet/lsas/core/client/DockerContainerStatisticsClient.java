package hu.psprog.leaflet.lsas.core.client;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Client interface for Docker Engine API calls.
 * Implementation should be returning Project Reactor reactive streams.
 *
 * @author Peter Smith
 */
public interface DockerContainerStatisticsClient {

    /**
     * Retrieves a list of the existing containers from the Docker Engine.
     *
     * @return list of existing containers as {@link ContainerModel} objects, wrapped as {@link Mono}
     */
    Mono<List<ContainerModel>> getExistingContainers();

    /**
     * Retrieves detailed information about the specified container from the Docker Engine.
     *
     * @param containerID container ID string as provided by Docker
     * @return detailed information about the container as {@link ContainerDetailsModel} wrapped as {@link Mono}
     */
    Mono<ContainerDetailsModel> getContainerDetails(String containerID);

    /**
     * Retrieves statistics about the specified container as streaming data from the Docker Engine.
     *
     * @param containerID container ID string as provided by Docker
     * @return statistics about the container as {@link ContainerRuntimeStatsModel} wrapped as {@link Flux}
     */
    Flux<ContainerRuntimeStatsModel> getContainerRuntimeStats(String containerID);
}
