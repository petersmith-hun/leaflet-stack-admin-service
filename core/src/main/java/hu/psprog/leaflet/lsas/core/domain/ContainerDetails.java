package hu.psprog.leaflet.lsas.core.domain;

import lombok.Builder;

import java.time.ZonedDateTime;

/**
 * Domain class representing a Docker container's detailed information.
 *
 * @author Peter Smith
 */
@Builder
public record ContainerDetails(
        String id,
        DockerContainerStatus status,
        String logPath,
        ZonedDateTime startedAt
) { }
