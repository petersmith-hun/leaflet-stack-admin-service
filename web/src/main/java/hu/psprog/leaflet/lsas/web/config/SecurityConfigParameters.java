package hu.psprog.leaflet.lsas.web.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration parameters model for CORS and API Key configuration.
 *
 * @author Peter Smith
 */
@Data
@Setter(AccessLevel.PACKAGE)
@Component
@ConfigurationProperties(prefix = "lsas.security")
public class SecurityConfigParameters {

    private CORSConfigParameters cors;
    private String apiKey;

    /**
     * Configuration parameter model for CORS specific settings.
     */
    @Data
    @Setter(AccessLevel.PACKAGE)
    public static class CORSConfigParameters {

        private boolean enabled;
        private final Map<String, String[]> allowedPaths = new HashMap<>();
        private String[] allowedOrigins;

    }
}
