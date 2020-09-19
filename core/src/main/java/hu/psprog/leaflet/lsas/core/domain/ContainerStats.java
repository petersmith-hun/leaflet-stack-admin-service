package hu.psprog.leaflet.lsas.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Domain class representing a Docker container's runtime statistics.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@Builder
@ToString
public class ContainerStats {

    private final String id;
    private final Double cpuUsagePercent;
    private final Integer memoryUsageInMegabytes;
    private final Double memoryUsagePercent;
}
