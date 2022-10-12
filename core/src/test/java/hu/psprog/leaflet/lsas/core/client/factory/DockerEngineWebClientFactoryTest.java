package hu.psprog.leaflet.lsas.core.client.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import io.netty.channel.unix.DomainSocketAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientConfig;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link DockerEngineWebClientFactory}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class DockerEngineWebClientFactoryTest {

    private static final String TCP_ADDRESS = "http://localhost:9999";
    private static final String SOCKET_PATH = "/path/to/socket";

    @Mock
    private ServiceRegistrations serviceRegistrations;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private ClientCodecConfigurer clientCodecConfigurer;

    @Mock
    private ClientCodecConfigurer.ClientDefaultCodecs clientDefaultCodecs;

    @Captor
    private ArgumentCaptor<ClientHttpConnector> connectorCaptor;

    @Captor
    private ArgumentCaptor<Consumer<ClientCodecConfigurer>> codecCaptor;

    private DockerEngineWebClientFactory dockerEngineWebClientFactory;

    @Test
    public void shouldFactoryCreateTCPClient() {

        try (MockedStatic<WebClient> mockedWebClient = mockStatic(WebClient.class)) {

            // given
            prepareFactory(ServiceRegistrations.DockerIntegration.IntegrationMode.TCP);
            mockedWebClient.when(WebClient::builder).thenReturn(webClientBuilder);
            given(webClientBuilder.codecs(any())).willReturn(webClientBuilder);
            given(webClientBuilder.build()).willReturn(webClient);

            // when
            WebClient result = dockerEngineWebClientFactory.createWebClient();

            // then
            assertThat(result, equalTo(webClient));
            verify(webClientBuilder).baseUrl(TCP_ADDRESS);
            verifyCodecConfigurerRegistration();
        }
    }

    @Test
    public void shouldFactoryCreateSocketClient() throws IllegalAccessException, ClassNotFoundException {

        try (MockedStatic<WebClient> mockedWebClient = mockStatic(WebClient.class)) {

            // given
            prepareFactory(ServiceRegistrations.DockerIntegration.IntegrationMode.SOCKET);
            mockedWebClient.when(WebClient::builder).thenReturn(webClientBuilder);
            given(webClientBuilder.codecs(any())).willReturn(webClientBuilder);
            given(webClientBuilder.build()).willReturn(webClient);

            // when
            WebClient result = dockerEngineWebClientFactory.createWebClient();

            // then
            assertThat(result, equalTo(webClient));
            verify(webClientBuilder).clientConnector(connectorCaptor.capture());
            verifyCodecConfigurerRegistration();

            ClientHttpConnector connectorCaptorValue = connectorCaptor.getValue();
            assertThat(connectorCaptorValue instanceof ReactorClientHttpConnector, is(true));

            HttpClient httpClient = extractClient(connectorCaptorValue);
            assertThat(httpClient, notNullValue());

            HttpClientConfig httpClientConfig = extractClientConfig(httpClient);
            assertThat(httpClientConfig, notNullValue());

            assertThat(httpClientConfig.remoteAddress().get() instanceof DomainSocketAddress, is(true));
            assertThat(httpClientConfig.remoteAddress().get().toString(), equalTo(SOCKET_PATH));
        }
    }

    private void verifyCodecConfigurerRegistration() {

        given(clientCodecConfigurer.defaultCodecs()).willReturn(clientDefaultCodecs);

        verify(webClientBuilder).codecs(codecCaptor.capture());

        Consumer<ClientCodecConfigurer> configurerValue = codecCaptor.getValue();
        configurerValue.accept(clientCodecConfigurer);
        verify(clientDefaultCodecs).jackson2JsonDecoder(any(Jackson2JsonDecoder.class));
    }

    private HttpClient extractClient(ClientHttpConnector clientHttpConnector) throws IllegalAccessException {

        Field httpClientField = ReflectionUtils.findField(ReactorClientHttpConnector.class, "httpClient");
        httpClientField.setAccessible(true);

        return (HttpClient) httpClientField.get(clientHttpConnector);
    }

    private HttpClientConfig extractClientConfig(HttpClient httpClient) throws ClassNotFoundException, IllegalAccessException {

        Field httpClientConfig = ReflectionUtils.findField(Class.forName("reactor.netty.http.client.HttpClientConnect"), "config");
        httpClientConfig.setAccessible(true);

        return (HttpClientConfig) httpClientConfig.get(httpClient);
    }

    private void prepareFactory(ServiceRegistrations.DockerIntegration.IntegrationMode integrationMode) {

        ServiceRegistrations.DockerIntegration dockerIntegration = new ServiceRegistrations.DockerIntegration();
        dockerIntegration.setIntegrationMode(integrationMode);
        dockerIntegration.setEngineHost(integrationMode == ServiceRegistrations.DockerIntegration.IntegrationMode.TCP
                ? TCP_ADDRESS
                : SOCKET_PATH);

        given(serviceRegistrations.getDockerIntegration()).willReturn(dockerIntegration);

        dockerEngineWebClientFactory = new DockerEngineWebClientFactory(serviceRegistrations, objectMapper);
    }
}
