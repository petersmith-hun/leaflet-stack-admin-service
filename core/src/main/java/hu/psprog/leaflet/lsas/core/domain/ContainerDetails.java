package hu.psprog.leaflet.lsas.core.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * Domain class representing a Docker container's detailed information.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class ContainerDetails {

    private final String id;
    private final DockerContainerStatus status;
    private final String logPath;
    private final ZonedDateTime startedAt;
}
