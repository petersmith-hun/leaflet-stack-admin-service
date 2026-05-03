package hu.psprog.leaflet.lsas.core.status.impl;

import hu.psprog.leaflet.bridge.client.handler.ResponseReader;
import hu.psprog.leaflet.lsas.core.domain.BuildInfo;
import hu.psprog.leaflet.lsas.core.domain.ServiceInfo;
import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

/**
 * Unit tests for {@link ActuatorBasedServiceStatusAdapter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ActuatorBasedServiceStatusAdapterTest {

    private static final String SERVICE_ABBREVIATION = "svc1";
    private static final ServiceStatus DOWN_SERVICE = ServiceStatus.buildDownService(SERVICE_ABBREVIATION);
    private static final ServiceInfo SERVICE_INFO = new ServiceInfo("svc-name", SERVICE_ABBREVIATION);
    private static final String STATUS_URL = "http://localhost:9999/api";
    private static final BuildInfo BUILD_INFO = new BuildInfo(ZonedDateTime.now(), "v1.0");
    private static final ServiceStatus SERVICE_STATUS = new ServiceStatus(SERVICE_INFO, BUILD_INFO);

    @Mock
    private HttpClient httpClient;

    @Mock
    private ClassicHttpResponse response;

    @Mock
    private ResponseReader responseReader;

    @Captor
    private ArgumentCaptor<HttpClientResponseHandler<ServiceStatus>> responseHandlerCaptor;

    @Captor
    private ArgumentCaptor<TypeReference<ServiceStatus>> typeReferenceCaptor;

    @Captor
    private ArgumentCaptor<HttpGet> requestCaptor;

    private ActuatorBasedServiceStatusAdapter actuatorBasedServiceStatusAdapter;

    @BeforeEach
    void setup() {
        actuatorBasedServiceStatusAdapter = new ActuatorBasedServiceStatusAdapter(httpClient, responseReader, SERVICE_ABBREVIATION, STATUS_URL);
    }

    @Test
    void shouldGetStatusWithSuccess() throws IOException, HttpException, URISyntaxException {

        // given
        given(httpClient.execute(requestCaptor.capture(), responseHandlerCaptor.capture())).willReturn(SERVICE_STATUS);
        given(responseReader.read(eq(response), typeReferenceCaptor.capture())).willReturn(SERVICE_STATUS);

        // when
        ServiceStatus result = actuatorBasedServiceStatusAdapter.getStatus();

        // then
        responseHandlerCaptor.getValue().handleResponse(response);

        assertThat(result, equalTo(SERVICE_STATUS));
        assertThat(typeReferenceCaptor.getValue().getType(), equalTo(ServiceStatus.class));

        HttpGet request = requestCaptor.getValue();
        assertThat(request.getMethod(), equalTo("GET"));
        assertThat(request.getPath(), equalTo("/api"));
        assertThat(request.getUri().toString(), equalTo(STATUS_URL));
    }

    @Test
    void shouldGetStatusReturnWithDownServiceForException() throws IOException {

        // given
        given(httpClient.execute(requestCaptor.capture(), responseHandlerCaptor.capture())).willThrow(IOException.class);

        // when
        ServiceStatus result = actuatorBasedServiceStatusAdapter.getStatus();

        // then
        assertThat(result, equalTo(DOWN_SERVICE));
    }

    @Test
    void shouldGetStatusReturnWithDownServiceForNonSuccessfulResponse() throws IOException {

        // given
        given(responseReader.read(eq(response), typeReferenceCaptor.capture())).willThrow(RuntimeException.class);
        doAnswer(invocation -> {
            var handler = invocation.getArgument(1, HttpClientResponseHandler.class);
            handler.handleResponse(response);
            return null;
        }).when(httpClient).execute(requestCaptor.capture(), responseHandlerCaptor.capture());

        // when
        ServiceStatus result = actuatorBasedServiceStatusAdapter.getStatus();

        // then
        assertThat(result, equalTo(DOWN_SERVICE));
    }

    @Test
    void shouldGetRegisteredAbbreviation() {

        // when
        String result = actuatorBasedServiceStatusAdapter.getRegisteredAbbreviation();

        // then
        assertThat(result, equalTo(SERVICE_ABBREVIATION));
    }
}
