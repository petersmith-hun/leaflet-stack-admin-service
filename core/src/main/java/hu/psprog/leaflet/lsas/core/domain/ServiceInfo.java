package hu.psprog.leaflet.lsas.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Domain class containing identification information about a single service.
 *
 * @author Peter Smith
 */
public record ServiceInfo(
        String name,
        String abbreviation
) {

    @JsonCreator
    public ServiceInfo {
    }
}
