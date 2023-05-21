package hu.psprog.leaflet.lsas.core.domain;

import java.time.ZonedDateTime;

/**
 * Domain class representing a formatted Docker repository tag object,
 * including the name and the creation date of the tag.
 *
 * @author Peter Smith
 */
public record DockerTag(
        String name,
        ZonedDateTime created
) { }
