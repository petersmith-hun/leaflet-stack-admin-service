package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Model class representing a Docker container's memory usage statistics information.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@JsonDeserialize(builder = MemoryStatsModel.MemoryStatsModelBuilder.class)
public class MemoryStatsModel {

    private final Long usage;
    private final Long limit;
    private final Long cache;

    public static MemoryStatsModelBuilder builder() {
        return new MemoryStatsModelBuilder();
    }

    @JsonPOJOBuilder(withPrefix = StringUtils.EMPTY)
    public static final class MemoryStatsModelBuilder {

        private Long usage = 0L;
        private Long limit = 0L;
        private Long cache = 0L;

        private MemoryStatsModelBuilder() {
        }

        public MemoryStatsModelBuilder usage(Long usage) {
            this.usage = usage;
            return this;
        }

        public MemoryStatsModelBuilder limit(Long limit) {
            this.limit = limit;
            return this;
        }

        @JsonProperty("stats")
        public MemoryStatsModelBuilder stats(Map<String, Object> stats) {

            this.cache = Optional.ofNullable(stats.get("cache"))
                    .map(String::valueOf)
                    .map(Long::valueOf)
                    .orElse(0L);

            return this;
        }

        public MemoryStatsModel build() {
            return new MemoryStatsModel(usage, limit, cache);
        }
    }
}
