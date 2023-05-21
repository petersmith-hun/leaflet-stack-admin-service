package hu.psprog.leaflet.lsas.core.status.impl;

import hu.psprog.leaflet.lsas.core.domain.BuildInfo;
import hu.psprog.leaflet.lsas.core.domain.ServiceInfo;
import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

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
    private Client client;

    @Mock
    private WebTarget webTarget;

    @Mock
    private Invocation.Builder builder;

    @Mock
    private Response response;

    private ActuatorBasedServiceStatusAdapter actuatorBasedServiceStatusAdapter;

    @BeforeEach
    void setup() {
        actuatorBasedServiceStatusAdapter = new ActuatorBasedServiceStatusAdapter(client, SERVICE_ABBREVIATION, STATUS_URL);
    }

    @Test
    void shouldGetStatusWithSuccess() {

        // given
        given(client.target(STATUS_URL)).willReturn(webTarget);
        given(webTarget.request()).willReturn(builder);
        given(builder.get()).willReturn(response);
        given(response.getStatusInfo()).willReturn(Response.Status.OK);
        given(response.readEntity(ServiceStatus.class)).willReturn(SERVICE_STATUS);

        // when
        ServiceStatus result = actuatorBasedServiceStatusAdapter.getStatus();

        // then
        assertThat(result, equalTo(SERVICE_STATUS));
    }

    @Test
    void shouldGetStatusReturnWithDownServiceForException() {

        // given
        given(client.target(STATUS_URL)).willReturn(webTarget);
        given(webTarget.request()).willReturn(builder);
        doThrow(RuntimeException.class).when(builder).get();

        // when
        ServiceStatus result = actuatorBasedServiceStatusAdapter.getStatus();

        // then
        assertThat(result, equalTo(DOWN_SERVICE));
    }

    @Test
    void shouldGetStatusReturnWithDownServiceForNonSuccessfulResponse() {

        // given
        given(client.target(STATUS_URL)).willReturn(webTarget);
        given(webTarget.request()).willReturn(builder);
        given(builder.get()).willReturn(response);
        given(response.getStatusInfo()).willReturn(Response.Status.INTERNAL_SERVER_ERROR);

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
