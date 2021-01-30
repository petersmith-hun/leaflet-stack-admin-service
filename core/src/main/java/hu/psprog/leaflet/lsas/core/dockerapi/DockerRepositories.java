package hu.psprog.leaflet.lsas.core.dockerapi;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Model class representing the response of a Docker Registry repository listing.
 *
 * @author Peter Smith
 */
public class DockerRepositories {

    private List<String> repositories;

    public List<String> getRepositories() {
        return repositories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DockerRepositories that = (DockerRepositories) o;

        return new EqualsBuilder()
                .append(repositories, that.repositories)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(repositories)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("repositories", repositories)
                .toString();
    }

    public static DockerRepositoriesBuilder getBuilder() {
        return new DockerRepositoriesBuilder();
    }

    /**
     * Builder for {@link DockerRepositories}.
     */
    public static final class DockerRepositoriesBuilder {

        private List<String> repositories;

        private DockerRepositoriesBuilder() {
        }

        public DockerRepositoriesBuilder withRepositories(List<String> repositories) {
            this.repositories = repositories;
            return this;
        }

        public DockerRepositories build() {
            DockerRepositories dockerRepositories = new DockerRepositories();
            dockerRepositories.repositories = this.repositories;
            return dockerRepositories;
        }
    }
}
