package eu.qped.java.checkers.design.res;

/**
 * @author Jannik Seus
 */
class TestClassDesignParent {

    protected int image;

    public TestClassDesignParent(int id) {
        image = id;
    }

    public int sru() {
        new TestClassDesign().m1();
        return image++;
    }

}
