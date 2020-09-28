package hu.psprog.leaflet.lsas.web.rest.filter;

import hu.psprog.leaflet.lsas.web.config.SecurityConfigParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Filter implementation to validate the provided API key on every endpoints.
 *
 * @author Peter Smith
 */
@Component
public class APIKeyValidationFilter implements WebFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIKeyValidationFilter.class);

    private static final String X_API_KEY_HEADER = "X-Api-Key";

    private final String apiKey;

    @Autowired
    public APIKeyValidationFilter(SecurityConfigParameters securityConfigParameters) {
        this.apiKey = securityConfigParameters.getApiKey();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String providedApiKey = extractAPIKey(exchange);
        Mono<Void> response;

        if (shouldAllowRequest(exchange, providedApiKey)) {
            response = chain.filter(exchange);
        } else {
            LOGGER.error("API Key is invalid or missing from the request to endpoint={}", exchange.getRequest().getPath());
            response = forbidProcessing(exchange.getResponse());
        }

        return response;
    }

    private String extractAPIKey(ServerWebExchange exchange) {

        return exchange.getRequest()
                .getHeaders()
                .getFirst(X_API_KEY_HEADER);
    }

    private boolean shouldAllowRequest(ServerWebExchange exchange, String providedApiKey) {

        return isOptionsRequest(exchange) || apiKey.equals(providedApiKey);
    }

    private boolean isOptionsRequest(ServerWebExchange exchange) {

        boolean corsOptionsRequest = exchange.getRequest().getMethod() == HttpMethod.OPTIONS;
        if (corsOptionsRequest) {
            LOGGER.info("CORS OPTIONS request received for endpoint={}.", exchange.getRequest().getPath());
        }

        return corsOptionsRequest;
    }

    private Mono<Void> forbidProcessing(ServerHttpResponse response) {

        response.setStatusCode(HttpStatus.FORBIDDEN);

        return response.setComplete();
    }
}
