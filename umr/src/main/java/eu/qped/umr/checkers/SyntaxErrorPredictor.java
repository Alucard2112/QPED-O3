package eu.qped.umr.checkers;

public class SyntaxErrorPredictor  {



    private final String errorCode;
    private final String errorTrigger;
    private final String errorMsg;


    public SyntaxErrorPredictor(String errorCode , String errorMsg , String errorTrigger){
        this.errorCode = errorCode;
        this.errorTrigger = errorTrigger;
        this.errorMsg = errorMsg;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorTrigger() {
        return errorTrigger;
    }

    public  String getErrorKind(){
        String errorKind = "";
        switch (errorCode){
            case "compiler.err.illegal.start.of.expr":{
                boolean hasMod = hasModifier(errorTrigger);
                if (!isAMethod(errorTrigger) && hasMod){
                    System.out.println("variable");
                    errorKind = "variable";
                }
                else if (isAMethod(errorTrigger) && !errorTrigger.contains("(")){
                    errorKind = "braces || strLit";
                }
                else if (isAMethod(errorTrigger)){
                    errorKind = "method";
                }
            }
            case "compiler.err.illegal.start.of.type":{

            }
            case "compiler.err.expected":
            case "compiler.err.expected3":
            case "compiler.err.expected1":
            case "compiler.err.expected2":
            {
                if (errorMsg.equals("<identifier> expected")) {
                    if (errorTrigger.contains("(") && errorTrigger.contains(")")) {
                        errorKind = "method";
                    } else if (errorTrigger.contains("=")) {
                        errorKind = "variable";
                    }
                }
            }
        }

        //todo
        return errorKind;
    }
    public   boolean isAMethod(String input){
        for (int i = input.length() -1; i >= 0 ; i--){
            if (input.charAt(i) == ' ' || input.charAt(i) != ')'){
                continue;
            }
            else return input.charAt(i) == ')' && (input.contains("return") || input.contains("void") );
        }
        return false;
    }

    public boolean hasBraces(String string){
        return string.contains(")");
    }


    private  boolean hasModifier(String input){
        return input.contains("public")||input.contains("private")||input.contains("protected");
    }

    public boolean hasEqualNumberOfBraces(String input){
        int openCounter = 0;
        int closedCounter = 0;

        for (int i = 0; i < input.length() ; i++) {
            if (input.charAt(i)=='{'){
                openCounter++;
            }
            else if (input.charAt(i) == '}'){
                closedCounter++;
            }
        }

        return openCounter == closedCounter;
    }


    public static void main(String[] args) {


        String code = " private void ttt (){\\n\" +\n" +
                "{"+
                "                \"        private int x = 3;\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"    }"+
                "}";




//        ErrorPredictor errorPredictor = new ErrorPredictor( "compiler.err.illegal.start.of.expr" , " void xxx (){\n" +
//                "             \n" +
//                "        }\n" +
//                "\n" +
//                "    }}");

//        System.out.println(errorPredictor.hasEqualNumberOfBraces(code));


    }

}
