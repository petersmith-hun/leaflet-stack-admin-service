package hu.psprog.leaflet.lsas.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import hu.psprog.leaflet.lsas.core.client.factory.DockerEngineWebClientFactory;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Bean
    public WebClient dockerEngineWebClient(DockerEngineWebClientFactory dockerEngineWebClientFactory) {
        return dockerEngineWebClientFactory.createWebClient();
    }

    @Bean
    public Jackson2JsonDecoder dockerManifestDecoder(ObjectMapper objectMapper) {
        return new Jackson2JsonDecoder(objectMapper, new MimeType("application", "vnd.docker.distribution.manifest.v1+prettyjws"));
    }
}
