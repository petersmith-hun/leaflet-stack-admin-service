package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * Model class representing a Docker container's runtime statistics.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
@JsonDeserialize(builder = ContainerRuntimeStatsModel.ContainerRuntimeStatsModelBuilder.class)
public class ContainerRuntimeStatsModel {

    @JsonProperty("memory_stats")
    private final MemoryStatsModel memoryStats;

    @JsonProperty("cpu_stats")
    private final CPUStatsModel currentCPUStats;

    @JsonProperty("precpu_stats")
    private final CPUStatsModel previousCPUStats;

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    public static class ContainerRuntimeStatsModelBuilder {

    }
}
