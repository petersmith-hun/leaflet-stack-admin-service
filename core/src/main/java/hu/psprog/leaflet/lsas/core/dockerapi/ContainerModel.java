package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Model class representing a Docker container's base information set.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record ContainerModel(
        @JsonProperty("Id") String id,
        @JsonProperty("Image") String image,
        @JsonProperty("Names") List<String> names,
        @JsonProperty("State") String state,
        @JsonProperty("Created") long createdTimestamp
) { }
