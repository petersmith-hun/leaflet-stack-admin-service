package hu.psprog.leaflet.lsas.core.domain;

import java.util.List;

/**
 * Domain class representing a formatted Docker Registry content object, including the registry's name
 * and the list of repositories available in the registry.
 *
 * @author Peter Smith
 */
public record DockerRegistryContent(
        String registryName,
        List<String> repositories
) { }
