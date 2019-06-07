package hu.psprog.leaflet.lsas.core.service.factory.impl;

import hu.psprog.leaflet.lsas.core.status.impl.ActuatorBasedServiceStatusAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

import javax.ws.rs.client.Client;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

/**
 * Unit tests for {@link ActuatorBasedServiceStatusAdapterFactoryImpl}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ActuatorBasedServiceStatusAdapterFactoryImplTest {

    private static final String SVC_1 = "svc1";
    private static final String SVC_2 = "svc2";
    private static final String SVC_3 = "svc3";
    private static final String URL_1 = "http://localhost:9997/api";
    private static final String URL_2 = "http://localhost:9998/api";
    private static final String URL_3 = "http://localhost:9999/api";
    private static final Map<String, String> REGISTRATION_MAP = Map.of(
            SVC_1, URL_1,
            SVC_2, URL_2,
            SVC_3, URL_3);

    @Mock
    private Client client;

    @InjectMocks
    private ActuatorBasedServiceStatusAdapterFactoryImpl actuatorBasedServiceStatusAdapterFactory;

    private Field clientField;
    private Field abbreviationField;
    private Field statusUrlField;

    @BeforeEach
    void setup() {

        clientField = ReflectionUtils.findField(ActuatorBasedServiceStatusAdapter.class, "client");
        abbreviationField = ReflectionUtils.findField(ActuatorBasedServiceStatusAdapter.class, "serviceAbbreviation");
        statusUrlField = ReflectionUtils.findField(ActuatorBasedServiceStatusAdapter.class, "statusURL");

        clientField.setAccessible(true);
        abbreviationField.setAccessible(true);
        statusUrlField.setAccessible(true);
    }

    @Test
    void shouldCreateBulk() throws IllegalAccessException {

        // when
        List<ActuatorBasedServiceStatusAdapter> result = actuatorBasedServiceStatusAdapterFactory.createBulk(REGISTRATION_MAP);

        // then
        result.sort(Comparator.comparing(ActuatorBasedServiceStatusAdapter::getRegisteredAbbreviation));
        assertThat(result, hasSize(3));
        assertAdapter(result.get(0), SVC_1, URL_1);
        assertAdapter(result.get(1), SVC_2, URL_2);
        assertAdapter(result.get(2), SVC_3, URL_3);
    }

    private void assertAdapter(ActuatorBasedServiceStatusAdapter adapter, String abbreviation, String url) throws IllegalAccessException {
        assertThat(clientField.get(adapter), equalTo(client));
        assertThat(abbreviationField.get(adapter), equalTo(abbreviation));
        assertThat(statusUrlField.get(adapter), equalTo(url));
    }
}
