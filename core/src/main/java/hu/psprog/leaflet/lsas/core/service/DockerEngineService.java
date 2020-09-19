package hu.psprog.leaflet.lsas.core.service;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.domain.Container;
import hu.psprog.leaflet.lsas.core.domain.ContainerDetails;
import hu.psprog.leaflet.lsas.core.domain.ContainerStats;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Service interface for Docker Engine related operations.
 *
 * @author Peter Smith
 */
public interface DockerEngineService {

    /**
     * Returns a list of the existing containers.
     *
     * @return list of existing containers as {@link ContainerModel} objects, wrapped as {@link Mono}
     */
    Mono<List<Container>> getExistingContainers();

    /**
     * Returns detailed information about the specified containers as streaming data.
     * Flux streams returning from multiple containers are merged as a single stream.
     *
     * @param containerIDList list of container ID strings as provided by Docker
     * @return detailed information about the containers as {@link ContainerDetailsModel} wrapped as {@link Flux}
     */
    Flux<ContainerDetails> getContainerDetails(List<String> containerIDList);

    /**
     * Returns statistics about the specified containers as streaming data.
     * Flux streams returning from multiple containers are merged as a single stream.
     *
     * @param containerIDList container ID strings as provided by Docker
     * @return statistics about the container as {@link ContainerRuntimeStatsModel} wrapped as {@link Flux}
     */
    Flux<ContainerStats> getContainerStatistics(List<String> containerIDList);
}
