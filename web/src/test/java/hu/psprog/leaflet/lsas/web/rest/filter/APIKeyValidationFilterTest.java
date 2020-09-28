package hu.psprog.leaflet.lsas.web.rest.filter;

import hu.psprog.leaflet.lsas.web.config.SecurityConfigParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Unit tests for {@link APIKeyValidationFilter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class APIKeyValidationFilterTest {

    private static final SecurityConfigParameters SECURITY_CONFIG_PARAMETERS = new SecurityConfigParameters();
    private static final String X_API_KEY_HEADER = "X-Api-Key";
    private static final String API_KEY = "api-key";
    private static final RequestPath REQUEST_PATH = RequestPath.parse(URI.create("/path/to/admin/endpoint"), "/");

    static {
        SECURITY_CONFIG_PARAMETERS.setApiKey(API_KEY);
    }

    @Mock
    private ServerWebExchange exchange;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private HttpHeaders httpHeaders;

    @Mock
    private WebFilterChain chain;

    private APIKeyValidationFilter apiKeyValidationFilter;

    @BeforeEach
    public void setup() {
        apiKeyValidationFilter = new APIKeyValidationFilter(SECURITY_CONFIG_PARAMETERS);
    }

    @Test
    public void shouldFilterAllowFurtherProcessingWithProperAPIKey() {

        // given
        given(exchange.getRequest()).willReturn(request);
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst(X_API_KEY_HEADER)).willReturn(API_KEY);
        given(request.getMethod()).willReturn(HttpMethod.GET);
        given(chain.filter(exchange)).willReturn(Mono.empty());

        // when
        Mono<Void> result = apiKeyValidationFilter.filter(exchange, chain);

        // then
        assertThat(result, equalTo(Mono.empty()));
        verifyNoMoreInteractions(response, chain);
    }

    @Test
    public void shouldFilterAllowFurtherProcessingForOptionsRequest() {

        // given
        given(exchange.getRequest()).willReturn(request);
        given(request.getHeaders()).willReturn(httpHeaders);
        given(request.getMethod()).willReturn(HttpMethod.OPTIONS);
        given(request.getPath()).willReturn(REQUEST_PATH);
        given(chain.filter(exchange)).willReturn(Mono.empty());

        // when
        Mono<Void> result = apiKeyValidationFilter.filter(exchange, chain);

        // then
        assertThat(result, equalTo(Mono.empty()));
        verifyNoMoreInteractions(response, chain);
    }

    @Test
    public void shouldFilterRejectFurtherProcessingWithMissingAPIKey() {

        // given
        given(exchange.getRequest()).willReturn(request);
        given(exchange.getResponse()).willReturn(response);
        given(request.getHeaders()).willReturn(httpHeaders);
        given(request.getMethod()).willReturn(HttpMethod.GET);
        given(request.getPath()).willReturn(REQUEST_PATH);
        given(httpHeaders.getFirst(X_API_KEY_HEADER)).willReturn(null);

        // when
        apiKeyValidationFilter.filter(exchange, chain);

        // then
        verify(response).setStatusCode(HttpStatus.FORBIDDEN);
        verify(response).setComplete();
        verifyNoMoreInteractions(response, chain);
    }

    @Test
    public void shouldFilterRejectFurtherProcessingWithInvalidAPIKey() {

        // given
        given(exchange.getRequest()).willReturn(request);
        given(exchange.getResponse()).willReturn(response);
        given(request.getHeaders()).willReturn(httpHeaders);
        given(request.getMethod()).willReturn(HttpMethod.GET);
        given(request.getPath()).willReturn(REQUEST_PATH);
        given(httpHeaders.getFirst(X_API_KEY_HEADER)).willReturn("different-api-key");

        // when
        apiKeyValidationFilter.filter(exchange, chain);

        // then
        verify(response).setStatusCode(HttpStatus.FORBIDDEN);
        verify(response).setComplete();
        verifyNoMoreInteractions(response, chain);
    }
}
