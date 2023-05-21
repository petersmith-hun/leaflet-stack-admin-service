package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model class representing the response of a Docker tag manifest request.
 *
 * @author Peter Smith
 */
public record DockerTagManifest(
        @JsonProperty("tag") String tag,
        @JsonProperty("history") List<DockerTagHistory> history
) {

    @JsonCreator
    public DockerTagManifest {
    }

    public record DockerTagHistory(
            @JsonProperty("v1Compatibility") String v1Compatibility
    ) {

        @JsonCreator
        public DockerTagHistory {
        }
    }
}
