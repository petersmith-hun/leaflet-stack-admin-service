package hu.psprog.leaflet.lsas.core.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Configuration model for service registrations.
 *
 * @author Peter Smith
 */
@Component
@ConfigurationProperties(prefix = "lsas")
public class ServiceRegistrations {

    private final Map<String, String> registrations = new LinkedHashMap<>();
    private DockerIntegration dockerIntegration;

    public Map<String, String> getRegistrations() {
        return registrations;
    }

    public DockerIntegration getDockerIntegration() {
        return dockerIntegration;
    }

    public void setDockerIntegration(DockerIntegration dockerIntegration) {
        this.dockerIntegration = dockerIntegration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ServiceRegistrations that = (ServiceRegistrations) o;

        return new EqualsBuilder()
                .append(registrations, that.registrations)
                .append(dockerIntegration, that.dockerIntegration)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(registrations)
                .append(dockerIntegration)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("registrations", registrations)
                .append("dockerIntegration", dockerIntegration)
                .toString();
    }

    /**
     * Model class for Docker Engine integration configuration parameters.
     */
    public static class DockerIntegration {

        private String engineHost;

        public String getEngineHost() {
            return engineHost;
        }

        public void setEngineHost(String engineHost) {
            this.engineHost = engineHost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            DockerIntegration that = (DockerIntegration) o;

            return new EqualsBuilder()
                    .append(engineHost, that.engineHost)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(engineHost)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("engineHost", engineHost)
                    .toString();
        }
    }
}
