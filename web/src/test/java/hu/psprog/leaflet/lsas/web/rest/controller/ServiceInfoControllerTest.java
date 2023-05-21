package hu.psprog.leaflet.lsas.web.rest.controller;

import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import hu.psprog.leaflet.lsas.core.service.StackStatusService;
import hu.psprog.leaflet.lsas.web.model.RegisteredServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link ServiceInfoController}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ServiceInfoControllerTest {

    private static final List<String> SERVICE_LIST = Arrays.asList("svc1", "svc2");
    private static final ServiceStatus DOWN_SERVICE = ServiceStatus.buildDownService("svc3");

    @Mock
    private StackStatusService stackStatusService;

    @InjectMocks
    private ServiceInfoController serviceInfoController;

    @Test
    void shouldListRegisteredServices() {

        // given
        given(stackStatusService.getRegisteredServices()).willReturn(SERVICE_LIST);

        // when
        ResponseEntity<RegisteredServices> result = serviceInfoController.listRegisteredServices();

        // then
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody().registeredServices(), equalTo(SERVICE_LIST));
    }

    @Test
    void shouldGetServiceStatus() {

        // given
        given(stackStatusService.getServiceStackStatus()).willReturn(Flux.just(DOWN_SERVICE));

        // when
        Flux<ServiceStatus> result = serviceInfoController.getServiceStatus();

        // then
        assertThat(result.blockFirst(), equalTo(DOWN_SERVICE));
    }
}
