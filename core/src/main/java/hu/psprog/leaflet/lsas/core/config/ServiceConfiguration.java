package hu.psprog.leaflet.lsas.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.TimeUnit;

/**
 * Service configuration.
 *
 * @author Peter Smith
 */
@Configuration
public class ServiceConfiguration {

    @Bean
    public Client jerseyClient(ObjectMapper objectMapper, @Value("${lsas.call-timeout}") int readTimeout) {

        return ClientBuilder.newBuilder()
                .register(new JacksonJsonProvider(objectMapper))
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
    }
}
