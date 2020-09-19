package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Model class representing a Docker container's base information set.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
@JsonDeserialize(builder = ContainerModel.ContainerModelBuilder.class)
public class ContainerModel {

    @JsonProperty("Id")
    private final String id;

    @JsonProperty("Image")
    private final String image;

    @JsonProperty("Names")
    private final List<String> names;

    @JsonProperty("State")
    private final String state;

    @JsonProperty("Created")
    private final long createdTimestamp;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    public static class ContainerModelBuilder {

    }
}
