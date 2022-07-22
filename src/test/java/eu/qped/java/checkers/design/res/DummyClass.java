package eu.qped.java.checkers.design.res;

/**
 * @author Jannik Seus
 */
abstract class DummyClass {

    abstract protected void dummyMethod();

    public void realMethod() {
        System.out.println("I'm doing something very important!");
    }
}
