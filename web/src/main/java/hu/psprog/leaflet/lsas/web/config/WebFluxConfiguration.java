package hu.psprog.leaflet.lsas.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Spring WebFlux additional configuration.
 *
 * @author Peter Smith
 */
@Configuration
@EnableWebFluxSecurity
public class WebFluxConfiguration implements WebFluxConfigurer {

    private static final String ROLE_SERVICE = "SERVICE";

    private SecurityConfigParameters securityConfigParameters;

    @Autowired
    public WebFluxConfiguration(SecurityConfigParameters securityConfigParameters) {
        this.securityConfigParameters = securityConfigParameters;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService mapReactiveUserDetailsService() {

        UserDetails userDetails = User
                .withUsername(securityConfigParameters.getServiceUser().getUsername())
                .password(passwordEncoder().encode(securityConfigParameters.getServiceUser().getPassword()))
                .roles(ROLE_SERVICE)
                .build();

        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {

        return httpSecurity
                .httpBasic()
                .and()

                .authorizeExchange()
                    .anyExchange().authenticated()
                .and()

                .build();
    }
}
