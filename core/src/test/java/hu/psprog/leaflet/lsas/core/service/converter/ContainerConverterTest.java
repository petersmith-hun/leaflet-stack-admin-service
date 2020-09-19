package hu.psprog.leaflet.lsas.core.service.converter;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerModel;
import hu.psprog.leaflet.lsas.core.domain.Container;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ContainerConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ContainerConverterTest {

    private static final String ID = "abcd1234";
    private static final long CREATED_TIMESTAMP = 123456789L;
    private static final List<String> NAMES = Collections.singletonList("container");
    private static final String IMAGE = "image";
    private static final String STATE = "running";
    private static final ContainerModel CONTAINER_MODEL = ContainerModel.builder()
            .id(ID)
            .createdTimestamp(CREATED_TIMESTAMP)
            .names(NAMES)
            .image(IMAGE)
            .state(STATE)
            .build();
    private static final Container CONTAINER = Container.builder()
            .id(ID)
            .createTimestamp(CREATED_TIMESTAMP)
            .names(NAMES)
            .image(IMAGE)
            .state(STATE)
            .build();

    @InjectMocks
    private ContainerConverter containerConverter;

    @Test
    public void shouldConvertContainerModelToContainer() {

        // when
        Container result = containerConverter.convert(CONTAINER_MODEL);

        // then
        assertThat(result, equalTo(CONTAINER));
    }
}
