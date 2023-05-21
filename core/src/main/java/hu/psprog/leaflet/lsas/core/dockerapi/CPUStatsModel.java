package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.Optional;

/**
 * Model class representing a Docker container's CPU usage statistics information.
 *
 * @author Peter Smith
 */
@Builder
@Jacksonized
public record CPUStatsModel(
        @JsonProperty("cpu_usage") CPUStatsModel.CPUUsageModel cpuUsageModel,
        @JsonProperty("online_cpus") Integer onlineCPUs,
        @JsonProperty("system_cpu_usage") Long systemCPUUsage
) {

    @Override
    public Long systemCPUUsage() {
        return Optional.ofNullable(systemCPUUsage)
                .orElse(0L);
    }

    @Builder
    @Jacksonized
    public record CPUUsageModel(@JsonProperty("total_usage") Long totalCPUUsage) {

        @Override
        public Long totalCPUUsage() {
            return Optional.ofNullable(totalCPUUsage)
                    .orElse(0L);
        }
    }
}
