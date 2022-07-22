package eu.qped.java.checkers.design.res;

import java.math.BigDecimal;

/**
 * @author Jannik Seus
 */
class TestClassDesignChild extends TestClassDesignParent {

    public TestClassDesignChild(int id) {
        super(id); //doesn't change the IC - it is not a redefined method.
    }

    protected Number number;

    public Number getFloatingPoint(int i) {
        if (this.number == null) {
            try {
                number = new Double(this.image);
            } catch (ArithmeticException e0) {
                number = new BigDecimal(this.image);
            }
        }
        return number;
    }

    public Object getValue() throws Exception {
        return getFloatingPoint(0);
    }

    public Class getType(int i) throws Exception {
        getValue();
        System.out.println(getName());
        return this.getFloatingPoint(0).getClass();
    }

    protected String getName() {
        return "Funny";
    }
}
