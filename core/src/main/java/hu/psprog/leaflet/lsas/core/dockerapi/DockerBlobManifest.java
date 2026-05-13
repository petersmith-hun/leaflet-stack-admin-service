package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Model class representing the response of a Docker blob manifest request.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record DockerBlobManifest(
    @JsonProperty("created") String created
) { }
