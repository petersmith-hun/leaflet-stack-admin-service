package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Model class representing a Docker container's runtime statistics.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record ContainerRuntimeStatsModel(
        @JsonProperty("memory_stats") MemoryStatsModel memoryStats,
        @JsonProperty("cpu_stats") CPUStatsModel currentCPUStats,
        @JsonProperty("precpu_stats") CPUStatsModel previousCPUStats
) { }
