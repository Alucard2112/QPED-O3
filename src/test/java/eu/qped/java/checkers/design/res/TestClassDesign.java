package eu.qped.java.checkers.design.res;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a class where the different design checker metrics can be tested.
 *
 * @author Jannik
 */
public class TestClassDesign {

    int i = 0;

    /**
     * Creates a new instance of KlasaTestowa
     */
    public TestClassDesign() {
    }

    void m1() {
        new TestClassDesign2().m2();
        i = 1;
        int n = -1;
        if (i == 1) while (i < 0) {
            n++;
            break;
        }
        else if (i == 2) System.out.println("2");


        for (n = 0; n < 10; n++)
            do {
                n++;
                continue;
            } while (n < 8);
        n++;

        float f[] = {1, 2, 3, 4, 5};

        for (float x : f)
            System.out.println(x);

    }

    void m2(String name, List<Double> list, String c) {
        m1();
        int n = 17;
        switch (n) {
            case 0:
                System.out.println("zero");
                break;
            case 17:
                System.out.println("duzo");
                if (n > 0) n = 0;
                break;
            case 20:
                n = 2;
                break;
            default:
                System.out.println("unknown");
        }
    }

}

