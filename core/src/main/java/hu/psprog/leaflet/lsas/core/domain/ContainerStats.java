package hu.psprog.leaflet.lsas.core.domain;

import lombok.Builder;

/**
 * Domain class representing a Docker container's runtime statistics.
 *
 * @author Peter Smith
 */
@Builder
public record ContainerStats(
        String id,
        Double cpuUsagePercent,
        Integer memoryUsageInMegabytes,
        Double memoryUsagePercent
) { }
