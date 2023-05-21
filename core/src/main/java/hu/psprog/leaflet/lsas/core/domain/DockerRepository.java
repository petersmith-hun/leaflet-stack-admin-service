package hu.psprog.leaflet.lsas.core.domain;

import java.util.List;

/**
 * Domain class representing a formatted Docker repository object,
 * including the registry name, repository name and the list of related tags.
 *
 * @author Peter Smith
 */
public record DockerRepository(
        String registry,
        String name,
        List<DockerTag> tags
) { }
