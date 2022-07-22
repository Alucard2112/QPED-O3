package eu.qped.java.checkers.design.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Test class for {@link DesignCheckReport}.
 *
 * @author Jannik Seus
 */
class DesignCheckReportTest {

    private final DesignCheckEntry mockedDesignCheckEntry = mock(DesignCheckEntry.class);
    private final String samplePath = "path";
    private DesignCheckReport designCheckReport;

    @BeforeEach
    void setUp() {
        designCheckReport = DesignCheckReport.builder()
                .metricsMap(List.of(mockedDesignCheckEntry))
                .pathsToClassFiles(List.of(samplePath))
                .build();
    }

    @Test
    void getPathsToClassFiles() {
        assertEquals(1, designCheckReport.getPathsToClassFiles().size());
        assertTrue(designCheckReport.getPathsToClassFiles().contains(samplePath));

    }

    @Test
    void getMetricsMap() {
        assertEquals(1, designCheckReport.getMetricsMap().size());
        assertTrue(designCheckReport.getMetricsMap().contains(mockedDesignCheckEntry));

    }

    @Test
    void setPathsToClassFiles() {
        String mocked = "anotherPath";
        designCheckReport.setPathsToClassFiles(List.of(mocked));
        assertEquals(1, designCheckReport.getPathsToClassFiles().size());
        assertTrue(designCheckReport.getPathsToClassFiles().contains(mocked));
    }

    @Test
    void setMetricsMap() {
        DesignCheckEntry mocked = mock(DesignCheckEntry.class);
        designCheckReport.setMetricsMap(List.of(mocked));
        assertEquals(1, designCheckReport.getMetricsMap().size());
        assertTrue(designCheckReport.getMetricsMap().contains(mocked));



    }
}