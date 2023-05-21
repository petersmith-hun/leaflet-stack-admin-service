package hu.psprog.leaflet.lsas.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.ZonedDateTime;

/**
 * Domain class containing information about a service's build data.
 *
 * @author Peter Smith
 */
public record BuildInfo(
        ZonedDateTime time,
        String version
) {

    @JsonCreator
    public BuildInfo {
    }
}
