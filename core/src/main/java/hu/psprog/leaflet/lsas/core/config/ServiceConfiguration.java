package hu.psprog.leaflet.lsas.core.config;

import hu.psprog.leaflet.bridge.client.handler.ResponseReader;
import hu.psprog.leaflet.bridge.client.impl.ResponseReaderImpl;
import hu.psprog.leaflet.bridge.client.request.RequestAdapter;
import hu.psprog.leaflet.lsas.core.client.factory.DockerEngineWebClientFactory;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.JacksonJsonDecoder;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.json.JsonMapper;

import java.util.concurrent.TimeUnit;

/**
 * Service configuration.
 *
 * @author Peter Smith
 */
@Configuration
public class ServiceConfiguration {

    private static final TimeValue MAX_IDLE_TIME = TimeValue.ofSeconds(30L);
    private static final RequestAdapter DUMMY_REQUEST_ADAPTER = new DummyRequestAdapter();

    @Bean
    public HttpClient bridgeHttpClient(@Value("${lsas.call-timeout}") int readTimeout) {

        return HttpClientBuilder.create()
                .disableAuthCaching()
                .disableAutomaticRetries()
                .disableConnectionState()
                .disableCookieManagement()
                .disableRedirectHandling()
                .setDefaultRequestConfig(RequestConfig.copy(RequestConfig.DEFAULT)
                        .setResponseTimeout(readTimeout, TimeUnit.MILLISECONDS)
                        .build())
                .evictIdleConnections(MAX_IDLE_TIME)
                .build();
    }

    @Bean
    public ResponseReader responseReader(JsonMapper jsonMapper) {
        return new ResponseReaderImpl(DUMMY_REQUEST_ADAPTER, jsonMapper);
    }

    @Bean
    public WebClient dockerEngineWebClient(DockerEngineWebClientFactory dockerEngineWebClientFactory) {
        return dockerEngineWebClientFactory.createWebClient();
    }

    @Bean
    public JacksonJsonDecoder dockerManifestDecoder(JsonMapper jsonMapper) {
        return new JacksonJsonDecoder(jsonMapper, new MimeType("application", "vnd.docker.distribution.manifest.v1+prettyjws"));
    }

    static class DummyRequestAdapter implements RequestAdapter {

        @Override
        public String provideDeviceID() {
            return "";
        }

        @Override
        public String provideClientID() {
            return "";
        }

        @Override
        public void consumeAuthenticationToken(String token) {
        }
    }
}
