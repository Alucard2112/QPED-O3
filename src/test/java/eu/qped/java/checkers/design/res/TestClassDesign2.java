package eu.qped.java.checkers.design.res;

/**
 * @author Jannik Seus
 */
class TestClassDesign2 {

    static int i = 0;
    static int j = 0;

    protected TestClassDesign mKt = null;

    void m1() {
        mKt = new TestClassDesign();
        i = 1;
        try {
            i += 17;
            throw new Exception(Integer.toHexString(i));
        } catch (Exception ex) {
            if (ex != null) System.err.println(ex.toString());
        }
    }

    void m2() {
        m1();
    }

    int m3(int jk) {
        m1();
        jk = j;
        return jk;
    }

}
