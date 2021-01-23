package hu.psprog.leaflet.lsas.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.client.WebClient;

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

    private final ServiceRegistrations serviceRegistrations;

    @Autowired
    public ServiceConfiguration(ServiceRegistrations serviceRegistrations) {
        this.serviceRegistrations = serviceRegistrations;
    }

    @Bean
    public Client jerseyClient(ObjectMapper objectMapper, @Value("${lsas.call-timeout}") int readTimeout) {

        return ClientBuilder.newBuilder()
                .register(new JacksonJsonProvider(objectMapper))
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    @Bean
    public WebClient dockerEngineWebClient(ObjectMapper objectMapper) {

        return WebClient.builder()
                .baseUrl(serviceRegistrations.getDockerIntegration().getEngineHost())
                .codecs(clientCodecConfigurer -> registerJacksonDecoderCodec(objectMapper, clientCodecConfigurer))
                .build();
    }

    @Bean
    public Jackson2JsonDecoder dockerManifestDecoder(ObjectMapper objectMapper) {
        return new Jackson2JsonDecoder(objectMapper, new MimeType("application", "vnd.docker.distribution.manifest.v1+prettyjws"));
    }

    private void registerJacksonDecoderCodec(ObjectMapper objectMapper, ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.ALL));
    }
}
