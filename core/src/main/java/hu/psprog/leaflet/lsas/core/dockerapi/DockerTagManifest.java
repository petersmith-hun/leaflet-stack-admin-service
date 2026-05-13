package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Model class representing the response of a Docker tag manifest request.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record DockerTagManifest(
        @JsonProperty("config") DockerTagManifestConfig config
) {

    @Builder
    @Jacksonized
    public record DockerTagManifestConfig(
            @JsonProperty("digest") String digest
    ) { }
}
