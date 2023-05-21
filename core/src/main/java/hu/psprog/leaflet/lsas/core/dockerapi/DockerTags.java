package hu.psprog.leaflet.lsas.core.dockerapi;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Model class representing the response of a Docker Registry repository tag listing request.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record DockerTags(
        String name,
        List<String> tags
) { }
