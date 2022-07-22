package eu.qped.java.checkers.design.res;

/**
 * @author Jannik Seus
 */
class ChildOfChild extends TestClassDesignChild {

    public ChildOfChild() throws Exception {
        super(0);
        getType(0);
        number = new Double(3.0);  //IC+1 p.1
    }

    static int getInt() {
        return 23761;
    }

    private final void notUsable(String slamp) throws Exception {
        final String s = "You won't be able to call this method!";
        System.out.println(s + slamp);
    }

    @Override
    public Object getValue() throws Exception { //IC+1 p.2 is used by super.getType(); super.getType() doesn't use the return value
        notUsable("slamp");
        getType(getInt()); //IC+0 (p.3) Coupling ChildOfChld.getValue()<=> KlasaTestowaChld.getType() is already counted; see the above line
        return null;
    }

    @Override
    protected String getName() { //IC+1 //p.2 (is used by super.getType())
        getFloatingPoint(0); //IC+1 //p.3
        return "Not funny";
    }

}
