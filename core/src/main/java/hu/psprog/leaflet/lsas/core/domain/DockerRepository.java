package hu.psprog.leaflet.lsas.core.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Domain class representing a formatted Docker repository object,
 * including the registry name, repository name and the list of related tags.
 *
 * @author Peter Smith
 */
public class DockerRepository {

    private final String registry;
    private final String name;
    private final List<DockerTag> tags;

    public DockerRepository(String registry, String name, List<DockerTag> tags) {
        this.registry = registry;
        this.name = name;
        this.tags = tags;
    }

    public String getRegistry() {
        return registry;
    }

    public String getName() {
        return name;
    }

    public List<DockerTag> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DockerRepository that = (DockerRepository) o;

        return new EqualsBuilder()
                .append(registry, that.registry)
                .append(name, that.name)
                .append(tags, that.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(registry)
                .append(name)
                .append(tags)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("registry", registry)
                .append("name", name)
                .append("tags", tags)
                .toString();
    }
}
