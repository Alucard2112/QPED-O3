package eu.qped.java.checkers.design.configuration;

import eu.qped.java.checkers.design.utils.DesignTestUtility;
import eu.qped.java.checkers.mass.QFDesignSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static eu.qped.java.checkers.design.ckjm.DesignCheckEntryHandler.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Represents a test class for {@link DesignSettings}.
 *
 * @author Jannik Seus
 */
class DesignSettingsTest {

    private DesignSettings designSettings;

    @BeforeEach
    void setUp() {
        this.designSettings = DesignSettings.builder().build();
    }

    @Test
    void setDesignSettingsTest() {
        QFDesignSettings qfDesignSettings = DesignTestUtility.generateQfDesignSettings();

        designSettings.setAmc(new MetricThreshold(Metric.AMC, Double.parseDouble(qfDesignSettings.getAmcMin()), Double.parseDouble(qfDesignSettings.getAmcMax())));
        assertThat(designSettings.getAmc().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getAmc().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setCa(new MetricThreshold(Metric.CA, Double.parseDouble(qfDesignSettings.getCaMin()), Double.parseDouble(qfDesignSettings.getCaMax())));
        assertThat(designSettings.getCa().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getCa().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setCam(new MetricThreshold(Metric.CAM, Double.parseDouble(qfDesignSettings.getCaMin()), Double.parseDouble(qfDesignSettings.getCaMax())));
        assertThat(designSettings.getCam().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getCam().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setCbm(new MetricThreshold(Metric.CBM, Double.parseDouble(qfDesignSettings.getCbmMin()), Double.parseDouble(qfDesignSettings.getCbmMax())));
        assertThat(designSettings.getCbm().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getCbm().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setCbo(new MetricThreshold(Metric.CBO, Double.parseDouble(qfDesignSettings.getCboMin()), Double.parseDouble(qfDesignSettings.getCboMax())));
        assertThat(designSettings.getCbo().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getCbo().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setCc(new MetricThreshold(Metric.CC, Double.parseDouble(qfDesignSettings.getCcMin()), Double.parseDouble(qfDesignSettings.getCcMax())));
        assertThat(designSettings.getCc().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getCc().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setCe(new MetricThreshold(Metric.CE, Double.parseDouble(qfDesignSettings.getCeMin()), Double.parseDouble(qfDesignSettings.getCeMax())));
        assertThat(designSettings.getCe().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getCe().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setDam(new MetricThreshold(Metric.DAM, Double.parseDouble(qfDesignSettings.getDamMin()), Double.parseDouble(qfDesignSettings.getDamMax())));
        assertThat(designSettings.getDam().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getDam().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setDit(new MetricThreshold(Metric.DIT, Double.parseDouble(qfDesignSettings.getDitMin()), Double.parseDouble(qfDesignSettings.getDitMax())));
        assertThat(designSettings.getDit().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getDit().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setIc(new MetricThreshold(Metric.IC, Double.parseDouble(qfDesignSettings.getIcMin()), Double.parseDouble(qfDesignSettings.getIcMax())));
        assertThat(designSettings.getIc().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getIc().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setLcom(new MetricThreshold(Metric.LCOM, Double.parseDouble(qfDesignSettings.getLcomMin()), Double.parseDouble(qfDesignSettings.getLcomMax())));
        assertThat(designSettings.getLcom().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getLcom().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setLcom3(new MetricThreshold(Metric.LCOM3, Double.parseDouble(qfDesignSettings.getLcom3Min()), Double.parseDouble(qfDesignSettings.getLcom3Max())));
        assertThat(designSettings.getLcom3().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getLcom3().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setLoc(new MetricThreshold(Metric.LOC, Double.parseDouble(qfDesignSettings.getLocMin()), Double.parseDouble(qfDesignSettings.getLocMax())));
        assertThat(designSettings.getLoc().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getLoc().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setMfa(new MetricThreshold(Metric.MFA, Double.parseDouble(qfDesignSettings.getMfaMin()), Double.parseDouble(qfDesignSettings.getMfaMax())));
        assertThat(designSettings.getMfa().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getMfa().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setMoa(new MetricThreshold(Metric.MOA, Double.parseDouble(qfDesignSettings.getMoaMin()), Double.parseDouble(qfDesignSettings.getMoaMax())));
        assertThat(designSettings.getMoa().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getMoa().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setNoc(new MetricThreshold(Metric.NOC, Double.parseDouble(qfDesignSettings.getNocMin()), Double.parseDouble(qfDesignSettings.getNocMax())));
        assertThat(designSettings.getNoc().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getNoc().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setNpm(new MetricThreshold(Metric.NPM, Double.parseDouble(qfDesignSettings.getNpmMin()), Double.parseDouble(qfDesignSettings.getNpmMax())));
        assertThat(designSettings.getNpm().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getNpm().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setRfc(new MetricThreshold(Metric.RFC, Double.parseDouble(qfDesignSettings.getRfcMin()), Double.parseDouble(qfDesignSettings.getRfcMax())));
        assertThat(designSettings.getRfc().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getRfc().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));

        designSettings.setWmc(new MetricThreshold(Metric.WMC, Double.parseDouble(qfDesignSettings.getWmcMin()), Double.parseDouble(qfDesignSettings.getWmcMax())));
        assertThat(designSettings.getWmc().getLowerBound(), allOf(greaterThan(0.), lessThan(0.5)));
        assertThat(designSettings.getWmc().getUpperBound(), allOf(greaterThan(0.5), lessThan(1.)));
    }
}