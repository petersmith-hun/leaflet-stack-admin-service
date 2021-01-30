package hu.psprog.leaflet.lsas.core.config;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
        private final Map<String, DockerRegistry> registryCatalog = new HashMap<>();

        public String getEngineHost() {
            return engineHost;
        }

        public void setEngineHost(String engineHost) {
            this.engineHost = engineHost;
        }

        public Map<String, DockerRegistry> getRegistryCatalog() {
            return registryCatalog;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            DockerIntegration that = (DockerIntegration) o;

            return new EqualsBuilder()
                    .append(engineHost, that.engineHost)
                    .append(registryCatalog, that.registryCatalog)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(engineHost)
                    .append(registryCatalog)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("engineHost", engineHost)
                    .append("registryCatalog", registryCatalog)
                    .toString();
        }
    }

    /**
     * Model class for Docker Registry configuration parameters.
     */
    public static class DockerRegistry {

        private String host;
        private String username;
        private String password;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            DockerRegistry that = (DockerRegistry) o;

            return new EqualsBuilder()
                    .append(host, that.host)
                    .append(username, that.username)
                    .append(password, that.password)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(host)
                    .append(username)
                    .append(password)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("host", host)
                    .append("username", username)
                    .append("password", password)
                    .toString();
        }
    }
}
