package hu.psprog.leaflet.lsas.core.service.converter;

import hu.psprog.leaflet.lsas.core.dockerapi.ContainerDetailsModel;
import hu.psprog.leaflet.lsas.core.domain.ContainerDetails;
import hu.psprog.leaflet.lsas.core.domain.DockerContainerStatus;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for {@link ContainerDetailsConverter}.
 *
 * @author Peter Smith
 */
@ExtendWith(MockitoExtension.class)
class ContainerDetailsConverterTest {

    private static final ZonedDateTime STARTED_AT = ZonedDateTime.now();
    private static final String ID = "abcd1234";
    private static final String LOG_PATH = "/path/to/log.log";

    @InjectMocks
    private ContainerDetailsConverter containerDetailsConverter;

    @ParameterizedTest
    @CsvSource({"running,RUNNING", "EXITED,EXITED", "wrong-status,UNKNOWN"})
    public void shouldConvertContainerDetailsModelToContainerDetails(String rawStatus, DockerContainerStatus expectedStatusEnum) {

        // given
        ContainerDetailsModel source = prepareContainerDetailsModel(rawStatus);
        ContainerDetails expectedResult = prepareExpectedContainerDetails(expectedStatusEnum);

        // when
        ContainerDetails result = containerDetailsConverter.convert(source);

        // then
        assertThat(result, equalTo(expectedResult));
    }

    private ContainerDetailsModel prepareContainerDetailsModel(String rawStatus) {

        return ContainerDetailsModel.builder()
                .id(ID)
                .logPath(LOG_PATH)
                .state(Map.of(
                        "Status", rawStatus,
                        "StartedAt", STARTED_AT.toString()
                ))
                .build();
    }

    private ContainerDetails prepareExpectedContainerDetails(DockerContainerStatus expectedStatus) {

        return ContainerDetails.builder()
                .id(ID)
                .logPath(LOG_PATH)
                .startedAt(STARTED_AT)
                .status(expectedStatus)
                .build();
    }
}
