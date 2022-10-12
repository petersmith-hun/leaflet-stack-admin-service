package hu.psprog.leaflet.lsas.core.client.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import io.netty.channel.unix.DomainSocketAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * Factory implementation for creating a {@link WebClient} instance used for Docker Engine API communication.
 *
 * @author Peter Smith
 */
@Component
public class DockerEngineWebClientFactory {

    private final ServiceRegistrations.DockerIntegration dockerIntegration;
    private final ObjectMapper objectMapper;

    @Autowired
    public DockerEngineWebClientFactory(ServiceRegistrations serviceRegistrations, ObjectMapper objectMapper) {
        this.dockerIntegration = serviceRegistrations.getDockerIntegration();
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a {@link WebClient} instance based on the currently set Docker integration mode.
     * In case the integration mode ({@code lsas.docker-integration.integration-mode} parameter) is set to TCP,
     * the WebClient will expect a TCP endpoint. Otherwise, (in case of SOCKET integration mode) the host address
     * should be a socket path and WebClient will use a {@link ReactorClientHttpConnector} adapter to establish connection.
     *
     * @return instantiated {@link WebClient}
     */
    public WebClient createWebClient() {

        WebClient.Builder webClientBuilder = WebClient.builder()
                .codecs(clientCodecConfigurer -> registerJacksonDecoderCodec(objectMapper, clientCodecConfigurer));

        if (isDockerSocketIntegrationSelected()) {
            webClientBuilder.clientConnector(createReactorClientHttpConnector());
        } else {
            webClientBuilder.baseUrl(dockerIntegration.getEngineHost());
        }

        return webClientBuilder.build();
    }

    private void registerJacksonDecoderCodec(ObjectMapper objectMapper, ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.ALL));
    }

    private boolean isDockerSocketIntegrationSelected() {
        return dockerIntegration.getIntegrationMode() == ServiceRegistrations.DockerIntegration.IntegrationMode.SOCKET;
    }

    private ReactorClientHttpConnector createReactorClientHttpConnector() {

        HttpClient dockerHttpClient = HttpClient.create()
                .remoteAddress(() -> new DomainSocketAddress(dockerIntegration.getEngineHost()));

        return new ReactorClientHttpConnector(dockerHttpClient);
    }
}
