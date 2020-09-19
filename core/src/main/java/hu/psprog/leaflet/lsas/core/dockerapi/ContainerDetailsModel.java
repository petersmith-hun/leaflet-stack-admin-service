package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Model class representing a Docker container's detailed information.
 *
 * @author Peter Smith
 */
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = ContainerDetailsModel.ContainerDetailsModelBuilder.class)
public class ContainerDetailsModel {

    private final String id;
    private final String status;
    private final String logPath;
    private final ZonedDateTime startedAt;

    private ContainerDetailsModel(ContainerDetailsModelBuilder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.logPath = builder.logPath;
        this.startedAt = builder.startedAt;
    }

    public static ContainerDetailsModelBuilder builder() {
        return new ContainerDetailsModelBuilder();
    }

    /**
     * Builder for {@link ContainerDetailsModel}.
     */
    public static final class ContainerDetailsModelBuilder {

        private String id;
        private String status;
        private String logPath;
        private ZonedDateTime startedAt;

        private ContainerDetailsModelBuilder() {
        }

        @JsonProperty("Id")
        public ContainerDetailsModelBuilder id(String id) {
            this.id = id;
            return this;
        }

        @JsonProperty("LogPath")
        public ContainerDetailsModelBuilder logPath(String logPath) {
            this.logPath = logPath;
            return this;
        }

        @JsonProperty("State")
        public ContainerDetailsModelBuilder state(Map<String, Object> state) {
            this.status = String.valueOf(state.get("Status"));
            this.startedAt = ZonedDateTime.parse(String.valueOf(state.get("StartedAt")));
            return this;
        }

        public ContainerDetailsModel build() {
            return new ContainerDetailsModel(this);
        }
    }
}
