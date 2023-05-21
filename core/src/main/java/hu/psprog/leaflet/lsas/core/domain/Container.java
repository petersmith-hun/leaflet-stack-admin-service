package hu.psprog.leaflet.lsas.core.domain;

import lombok.Builder;

import java.util.List;

/**
 * Domain class representing a Docker container's base information set.
 *
 * @author Peter Smith
 */
@Builder
public record Container(
        String id,
        String image,
        List<String> names,
        String state,
        long createTimestamp
) { }
