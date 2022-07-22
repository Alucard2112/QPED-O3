package eu.qped.java.checkers.design.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link DesignCheckEntry}.
 *
 * @author Jannik Seus
 */
class DesignCheckEntryTest {

    private DesignCheckEntry designCheckEntry1;
    private DesignCheckEntry designCheckEntry2;
    private DesignCheckEntry designCheckEntry3;

    @BeforeEach
    void setUp() {
        designCheckEntry1 = new DesignCheckEntry("className", List.of());
        designCheckEntry2 = new DesignCheckEntry("", List.of());
        designCheckEntry3 = new DesignCheckEntry("anotherClass", List.of());
    }

    @Test
    void compareTo() {
        ArrayList<DesignCheckEntry> entries = new ArrayList<>(List.of(designCheckEntry1, designCheckEntry2, designCheckEntry3));
        entries.sort(Comparator.naturalOrder());
        List<DesignCheckEntry> sortedEntries = List.of(designCheckEntry2, designCheckEntry3, designCheckEntry1);
        assertArrayEquals(sortedEntries.toArray(), entries.toArray());
    }

    @Test
    void getClassName() throws NoSuchFieldException, IllegalAccessException {
        Field classNameField = designCheckEntry1.getClass().getDeclaredField("className");
        classNameField.setAccessible(true);
        assertEquals("className", classNameField.get(designCheckEntry1));
        assertEquals("className", designCheckEntry1.getClassName());
        classNameField.setAccessible(false);

    }

    @Test
    void getMetricsForClass() throws NoSuchFieldException, IllegalAccessException {
        Field metricsForClassField = designCheckEntry1.getClass().getDeclaredField("metricsForClass");
        metricsForClassField.setAccessible(true);
        Object[] expected = List.of().toArray();
        assertArrayEquals(expected, ((List<?>) metricsForClassField.get(designCheckEntry1)).toArray());
        assertArrayEquals(expected, designCheckEntry1.getMetricsForClass().toArray());
        metricsForClassField.setAccessible(false);
    }

}