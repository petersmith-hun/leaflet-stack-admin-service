package hu.psprog.leaflet.lsas.core.dockerapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Model class representing the response of a Docker tag manifest request.
 *
 * @author Peter Smith
 */
public class DockerTagManifest {

    private final String tag;
    private final List<DockerTagHistory> history;

    @JsonCreator
    public DockerTagManifest(@JsonProperty("tag") String tag, @JsonProperty("history") List<DockerTagHistory> history) {
        this.tag = tag;
        this.history = history;
    }

    public String getTag() {
        return tag;
    }

    public List<DockerTagHistory> getHistory() {
        return history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DockerTagManifest that = (DockerTagManifest) o;

        return new EqualsBuilder()
                .append(tag, that.tag)
                .append(history, that.history)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tag)
                .append(history)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tag", tag)
                .append("history", history)
                .toString();
    }

    public static class DockerTagHistory {

        private final String v1Compatibility;

        @JsonCreator
        public DockerTagHistory(@JsonProperty("v1Compatibility") String v1Compatibility) {
            this.v1Compatibility = v1Compatibility;
        }

        public String getV1Compatibility() {
            return v1Compatibility;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            DockerTagHistory that = (DockerTagHistory) o;

            return new EqualsBuilder()
                    .append(v1Compatibility, that.v1Compatibility)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(v1Compatibility)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("v1Compatibility", v1Compatibility)
                    .toString();
        }
    }
}
