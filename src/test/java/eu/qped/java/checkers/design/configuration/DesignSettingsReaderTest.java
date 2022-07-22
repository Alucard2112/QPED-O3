package eu.qped.java.checkers.design.configuration;

import eu.qped.java.checkers.design.utils.DesignTestUtility;
import eu.qped.java.checkers.mass.QFDesignSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link  DesignSettingsReader}.
 *
 * @author Jannik Seus
 */
class DesignSettingsReaderTest {

    private DesignSettingsReader designSettingsReader;
    private QFDesignSettings qfDesignSettings;

    @BeforeEach
    void setUp() {
        qfDesignSettings = DesignTestUtility.generateQfDesignSettings();
        this.designSettingsReader = DesignSettingsReader.builder().qfDesignSettings(qfDesignSettings).build();

    }

    @Test
    void readDesignSettingsTest() {
        DesignSettings designSettings = designSettingsReader.readDesignSettings(DesignSettings.builder().build());

        assertEquals(Double.parseDouble(qfDesignSettings.getAmcMin()), designSettings.getAmc().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCaMin()), designSettings.getCa().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCamMin()), designSettings.getCam().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCbmMin()), designSettings.getCbm().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCboMin()), designSettings.getCbo().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCcMin()), designSettings.getCc().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCeMin()), designSettings.getCe().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getDamMin()), designSettings.getDam().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getDitMin()), designSettings.getDit().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getIcMin()), designSettings.getIc().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getLcomMin()), designSettings.getLcom().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getLcom3Min()), designSettings.getLcom3().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getLocMin()), designSettings.getLoc().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getMfaMin()), designSettings.getMfa().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getMoaMin()), designSettings.getMoa().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getNocMin()), designSettings.getNoc().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getNpmMin()), designSettings.getNpm().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getRfcMin()), designSettings.getRfc().getLowerBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getWmcMin()), designSettings.getWmc().getLowerBound());

        assertEquals(Double.parseDouble(qfDesignSettings.getAmcMax()), designSettings.getAmc().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCaMax()), designSettings.getCa().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCamMax()), designSettings.getCam().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCbmMax()), designSettings.getCbm().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCboMax()), designSettings.getCbo().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCcMax()), designSettings.getCc().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getCeMax()), designSettings.getCe().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getDamMax()), designSettings.getDam().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getDitMax()), designSettings.getDit().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getIcMax()), designSettings.getIc().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getLcomMax()), designSettings.getLcom().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getLcom3Max()), designSettings.getLcom3().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getLocMax()), designSettings.getLoc().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getMfaMax()), designSettings.getMfa().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getMoaMax()), designSettings.getMoa().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getNocMax()), designSettings.getNoc().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getNpmMax()), designSettings.getNpm().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getRfcMax()), designSettings.getRfc().getUpperBound());
        assertEquals(Double.parseDouble(qfDesignSettings.getWmcMax()), designSettings.getWmc().getUpperBound());


    }

    @Test
    void retrieveMetricThresholdAllBoundsGivenTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method retrieveMetricThresholdMethod = designSettingsReader.getClass().getDeclaredMethod("retrieveMetricThreshold", Metric.class);
        retrieveMetricThresholdMethod.setAccessible(true);
        MetricThreshold retrievedThreshold = (MetricThreshold) retrieveMetricThresholdMethod.invoke(designSettingsReader, Metric.AMC);
        assertThat(retrievedThreshold.getLowerBound(), allOf(greaterThan(.0), lessThan(.5)));
        assertThat(retrievedThreshold.getUpperBound(), allOf(greaterThan(.5), lessThan(1.)));
        assertEquals(Metric.AMC, retrievedThreshold.getMetric());
        retrieveMetricThresholdMethod.setAccessible(false);
    }

    @Test
    void retrieveMetricUpperBoundGivenTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QFDesignSettings qfDesignSettings = new QFDesignSettings();
        qfDesignSettings.setAmc("", "4.0");
        this.designSettingsReader = DesignSettingsReader.builder().qfDesignSettings(qfDesignSettings).build();

        Method retrieveMetricThresholdMethod = designSettingsReader.getClass().getDeclaredMethod("retrieveMetricThreshold", Metric.class);
        retrieveMetricThresholdMethod.setAccessible(true);
        MetricThreshold retrievedThreshold = (MetricThreshold) retrieveMetricThresholdMethod.invoke(designSettingsReader, Metric.AMC);
        assertEquals(0, retrievedThreshold.getLowerBound());
        assertEquals(4.0, retrievedThreshold.getUpperBound());
        assertEquals(Metric.AMC, retrievedThreshold.getMetric());
        retrieveMetricThresholdMethod.setAccessible(false);
    }

    @Test
    void retrieveMetricLowerBoundGivenTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QFDesignSettings qfDesignSettings = new QFDesignSettings();
        qfDesignSettings.setAmc("2.0", "");
        this.designSettingsReader = DesignSettingsReader.builder().qfDesignSettings(qfDesignSettings).build();

        Method retrieveMetricThresholdMethod = designSettingsReader.getClass().getDeclaredMethod("retrieveMetricThreshold", Metric.class);
        retrieveMetricThresholdMethod.setAccessible(true);
        MetricThreshold retrievedThreshold = (MetricThreshold) retrieveMetricThresholdMethod.invoke(designSettingsReader, Metric.AMC);
        assertEquals(2.0, retrievedThreshold.getLowerBound());
        assertEquals(Double.MAX_VALUE, retrievedThreshold.getUpperBound());
        assertEquals(Metric.AMC, retrievedThreshold.getMetric());
        retrieveMetricThresholdMethod.setAccessible(false);
    }

    @Test
    void retrieveMetricNoBoundGivenTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QFDesignSettings qfDesignSettings = new QFDesignSettings();
        qfDesignSettings.setAmc("", "");
        this.designSettingsReader = DesignSettingsReader.builder().qfDesignSettings(qfDesignSettings).build();

        Method retrieveMetricThresholdMethod = designSettingsReader.getClass().getDeclaredMethod("retrieveMetricThreshold", Metric.class);
        retrieveMetricThresholdMethod.setAccessible(true);
        MetricThreshold retrievedThreshold = (MetricThreshold) retrieveMetricThresholdMethod.invoke(designSettingsReader, Metric.AMC);
        assertEquals(0, retrievedThreshold.getLowerBound());
        assertEquals(Double.MAX_VALUE, retrievedThreshold.getUpperBound());
        assertEquals(Metric.AMC, retrievedThreshold.getMetric());
        retrieveMetricThresholdMethod.setAccessible(false);
    }

    @Test
    void retrieveMetricMaximumWrongFormatTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        qfDesignSettings.setAmc("0,4r", "0.99ö");
        this.designSettingsReader = DesignSettingsReader.builder().qfDesignSettings(qfDesignSettings).build();

        Method retrieveMetricMaximumMethod = designSettingsReader.getClass().getDeclaredMethod("retrieveMetricMaximum", Metric.class);
        retrieveMetricMaximumMethod.setAccessible(true);

        assertEquals(-1, (Double) retrieveMetricMaximumMethod.invoke(designSettingsReader, Metric.AMC));
        qfDesignSettings.setWmc(null, null);
        assertEquals(Double.MAX_VALUE, retrieveMetricMaximumMethod.invoke(designSettingsReader, Metric.WMC));

        retrieveMetricMaximumMethod.setAccessible(false);
    }
    @Test
    void retrieveMetricMinimumWrongFormatTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        qfDesignSettings.setAmc("0,4r", "0.99ö");
        this.designSettingsReader = DesignSettingsReader.builder().qfDesignSettings(qfDesignSettings).build();

        Method retrieveMetricMinimumMethod = designSettingsReader.getClass().getDeclaredMethod("retrieveMetricMinimum", Metric.class);
        retrieveMetricMinimumMethod.setAccessible(true);

        assertEquals(-1.0, retrieveMetricMinimumMethod.invoke(designSettingsReader, Metric.AMC));
        qfDesignSettings.setWmc(null, null);
        assertEquals(0d, retrieveMetricMinimumMethod.invoke(designSettingsReader, Metric.WMC));

        retrieveMetricMinimumMethod.setAccessible(false);
    }
}