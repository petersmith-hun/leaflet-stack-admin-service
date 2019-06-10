package hu.psprog.leaflet.lsas.core.service.impl;

import hu.psprog.leaflet.lsas.core.config.ServiceRegistrations;
import hu.psprog.leaflet.lsas.core.domain.ServiceStatus;
import hu.psprog.leaflet.lsas.core.service.factory.ServiceStatusAdapterFactory;
import hu.psprog.leaflet.lsas.core.status.ServiceStatusAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Unit tests for {@link StackStatusServiceImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class StackStatusServiceImplTest {

    private static final String ABBREVIATION_SVC_1 = "svc1";
    private static final String ABBREVIATION_SVC_2 = "svc2";
    private static final ServiceStatus DOWN_SERVICE_1 = ServiceStatus.buildDownService(ABBREVIATION_SVC_1);
    private static final ServiceStatus DOWN_SERVICE_2 = ServiceStatus.buildDownService(ABBREVIATION_SVC_2);
    private static final Map<String, String> REGISTRATION_MAP = new HashMap<>();

    @Mock
    private ServiceStatusAdapter serviceStatusAdapter1;

    @Mock
    private ServiceStatusAdapter serviceStatusAdapter2;

    @Mock
    private ServiceRegistrations serviceRegistrations;

    @Mock
    private ServiceStatusAdapterFactory serviceStatusAdapterFactory;

    private StackStatusServiceImpl stackStatusService;

    @BeforeEach
    void setup() {
        given(serviceRegistrations.getRegistrations()).willReturn(REGISTRATION_MAP);
        given(serviceStatusAdapterFactory.createBulk(REGISTRATION_MAP)).willReturn(Arrays.asList(serviceStatusAdapter1, serviceStatusAdapter2));
        stackStatusService = new StackStatusServiceImpl(serviceRegistrations, serviceStatusAdapterFactory);
    }

    @Test
    void shouldGetRegisteredServices() {

        // given
        given(serviceStatusAdapter1.getRegisteredAbbreviation()).willReturn(ABBREVIATION_SVC_1);
        given(serviceStatusAdapter2.getRegisteredAbbreviation()).willReturn(ABBREVIATION_SVC_2);

        // when
        List<String> result = stackStatusService.getRegisteredServices();

        // then
        assertThat(result.size(), equalTo(2));
        assertThat(result, hasItems(ABBREVIATION_SVC_1, ABBREVIATION_SVC_2));
    }

    @Test
    void shouldGetServiceStackStatus() {

        // given
        given(serviceStatusAdapter1.getStatus()).willReturn(DOWN_SERVICE_1);
        given(serviceStatusAdapter2.getStatus()).willReturn(DOWN_SERVICE_2);

        // when
        Flux<ServiceStatus> result = stackStatusService.getServiceStackStatus();

        // then
        List<ServiceStatus> resultAsList = result.toStream()
                .collect(Collectors.toList());
        assertThat(resultAsList.size(), equalTo(2));
        assertThat(resultAsList, hasItems(DOWN_SERVICE_1, DOWN_SERVICE_2));
    }
}
