package hu.psprog.leaflet.lsas.core.dockerapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Model class representing the response of a Docker Registry repository tag listing request.
 *
 * @author Peter Smith
 */
public class DockerTags {

    private String name;
    private List<String> tags;

    public String getName() {
        return name;
    }

    public List<String> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DockerTags that = (DockerTags) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(tags, that.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(tags)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("tags", tags)
                .toString();
    }

    public static DockerTagsBuilder getBuilder() {
        return new DockerTagsBuilder();
    }

    /**
     * Builder for {@link DockerTags}.
     */
    public static final class DockerTagsBuilder {
        private String name;
        private List<String> tags;

        private DockerTagsBuilder() {
        }

        public DockerTagsBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public DockerTagsBuilder withTags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public DockerTags build() {
            DockerTags dockerTags = new DockerTags();
            dockerTags.tags = this.tags;
            dockerTags.name = this.name;
            return dockerTags;
        }
    }
}
