package hu.psprog.leaflet.lsas.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Configuration for REST API layer.
 *
 * @author Peter Smith
 */
@Configuration
public class RestApiConfiguration implements WebFluxConfigurer {

    private final SecurityConfigParameters.CORSConfigParameters cors;

    @Autowired
    public RestApiConfiguration(SecurityConfigParameters securityConfigParameters) {
        this.cors = securityConfigParameters.getCors();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        if (cors.isEnabled()) {
            cors.getAllowedPaths().forEach((path, methods) -> registry
                    .addMapping(path)
                    .allowedMethods(methods)
                    .allowedOrigins(cors.getAllowedOrigins()));
        }
    }
}
