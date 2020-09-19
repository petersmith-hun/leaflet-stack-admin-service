package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

/**
 * Model class representing a Docker container's CPU usage statistics information.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@Builder
@JsonDeserialize(builder = CPUStatsModel.CPUStatsModelBuilder.class)
public class CPUStatsModel {

    @JsonProperty("cpu_usage")
    private final CPUUsageModel cpuUsageModel;

    @JsonProperty("online_cpus")
    private final Integer onlineCPUs;

    @JsonProperty("system_cpu_usage")
    private final Long systemCPUUsage;

    public Long getSystemCPUUsage() {
        return Optional.ofNullable(systemCPUUsage)
                .orElse(0L);
    }

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    public static class CPUStatsModelBuilder {

    }

    @Getter
    @EqualsAndHashCode
    @ToString
    @Builder
    @JsonDeserialize(builder = CPUUsageModel.CPUUsageModelBuilder.class)
    public static class CPUUsageModel {

        @JsonProperty("total_usage")
        private final Long totalCPUUsage;

        public Long getTotalCPUUsage() {
            return Optional.ofNullable(totalCPUUsage)
                    .orElse(0L);
        }

        @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
        public static class CPUUsageModelBuilder {

        }
    }
}
