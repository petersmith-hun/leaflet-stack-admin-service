package hu.psprog.leaflet.lsas.core.service.factory;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerRuntimeStatsModel;
import hu.psprog.leaflet.lsas.core.domain.ContainerStats;

/**
 * Factory interface to properly create {@link ContainerStats} objects.
 *
 * @author Peter Smith
 */
public interface ContainerStatsFactory {

    /**
     * Creates a {@link ContainerStats} object based on the given parameters.
     *
     * @param containerID container ID string as provided by Docker Engine
     * @param containerRuntimeStatsModel {@link ContainerRuntimeStatsModel} model containing raw runtime statistics information
     * @return processed and formatted statistics information as {@link ContainerStats}
     */
    ContainerStats create(String containerID, ContainerRuntimeStatsModel containerRuntimeStatsModel);
}
