package hu.psprog.leaflet.lsas.core.dockerapi;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Model class representing the response of a Docker Registry repository listing.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record DockerRepositories(
        List<String> repositories
) { }
