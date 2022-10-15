package hu.psprog.leaflet.lsas.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Spring WebFlux security configuration.
 *
 * @author Peter Smith
 */
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private static final String ENDPOINT_STACK_STATUS = "/lsas/stack-status/**";
    private static final String ENDPOINT_CONTAINERS = "/lsas/containers/**";
    private static final String ENDPOINT_REGISTRY = "/lsas/registry/**";

    private static final String SCOPE_READ_SERVICES_STATUS = "SCOPE_read:services:status";
    private static final String SCOPE_READ_DOCKER_CONTAINERS = "SCOPE_read:docker:containers";
    private static final String SCOPE_READ_DOCKER_REGISTRY = "SCOPE_read:docker:registry";
    private static final String SCOPE_WRITE_DOCKER_REGISTRY = "SCOPE_write:docker:registry";

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {

        return http
                .authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS, ENDPOINT_STACK_STATUS, ENDPOINT_CONTAINERS)
                        .permitAll()
                    .pathMatchers(HttpMethod.GET, ENDPOINT_STACK_STATUS)
                        .hasAuthority(SCOPE_READ_SERVICES_STATUS)
                    .pathMatchers(HttpMethod.GET, ENDPOINT_CONTAINERS)
                        .hasAuthority(SCOPE_READ_DOCKER_CONTAINERS)
                    .pathMatchers(HttpMethod.GET, ENDPOINT_REGISTRY)
                        .hasAuthority(SCOPE_READ_DOCKER_REGISTRY)
                    .pathMatchers(HttpMethod.DELETE, ENDPOINT_REGISTRY)
                        .hasAuthority(SCOPE_WRITE_DOCKER_REGISTRY)
                    .and()

                .csrf()
                    .disable()

                .cors()
                    .and()

                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)

                .build();
    }
}
