package hu.psprog.leaflet.lsas.core.config;

import lombok.Data;
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
@Data
@Component
@ConfigurationProperties(prefix = "lsas")
public class ServiceRegistrations {

    private final Map<String, String> registrations = new LinkedHashMap<>();
    private DockerIntegration dockerIntegration;

    /**
     * Model class for Docker Engine integration configuration parameters.
     */
    @Data
    public static class DockerIntegration {

        private IntegrationMode integrationMode = IntegrationMode.TCP;
        private String engineHost;
        private final Map<String, DockerRegistry> registryCatalog = new HashMap<>();

        public enum IntegrationMode {
            SOCKET,
            TCP
        }
    }

    /**
     * Model class for Docker Registry configuration parameters.
     */
    @Data
    public static class DockerRegistry {

        private String host;
        private String username;
        private String password;

    }
}
