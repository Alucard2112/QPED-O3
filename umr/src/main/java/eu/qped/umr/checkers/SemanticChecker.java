package eu.qped.umr.checkers;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;
import eu.qped.umr.compiler.Logger;
import eu.qped.umr.configs.SemanticConfigurator;
import eu.qped.umr.exceptions.NoSuchMethodException;


import java.util.*;


public class SemanticChecker implements CheckerInterface {

    private final String source;
    private final SemanticConfigurator semanticConfigurator;
    private CompilationUnit compilationUnit;
    private final ArrayList<String> feedbacks;
    private Map<String , String> settings;
    private boolean usedALoop = false;
    private String targetReturnType;
    private String returnType;


    public SemanticChecker (String source , SemanticConfigurator semanticConfigurator){
        this.source = source;
        this.feedbacks = new ArrayList<>();
        this.semanticConfigurator = semanticConfigurator;
        settings = new HashMap<>();
        parseCompUnit();
        executeStmtVisitor();
        if (Boolean.parseBoolean(semanticConfigurator.getRecursionAllowed())){
            try {
                executeRecursionChecker();
            }
            catch (Exception e){
                Logger.getInstance().log(e.getMessage());
            }
        }
    }

    private void readSettings(){
        this.settings = semanticConfigurator.getOutputSettings();
    }

    private void checkReturnTyp(){
        boolean result = false;
        targetReturnType = semanticConfigurator.getReturnType();

        result=  targetReturnType.equalsIgnoreCase(returnType);

        if (!result &&  !targetReturnType.equals("undefined") && targetReturnType != null){
            feedbacks.add("you've used the return type " + returnType + "\n" + " you should use the return type " + targetReturnType) ;
        }
    }





    private void parseCompUnit (){
        this.compilationUnit = StaticJavaParser.parse(this.source);
    }


    private BlockStmt getTargetedMethod (String targetedMethodName) throws NoSuchMethodException {
        final BlockStmt[] result = {new BlockStmt()};
        final boolean[] methodFound = {false};

        for (int i = 0; i < compilationUnit.getChildNodes().size(); i++) {
            /*
            all Nodes from SourceFile (Comments, Imports, Class components)
             */
            Node node = compilationUnit.getChildNodes().get(i);
            /*
            every Class component's components;
             */
            if (node.getChildNodes() != null) {
                for (int j = 0; j < node.getChildNodes().size(); j++) {

                    Node currentNode = node.getChildNodes().get(j);

                    currentNode.accept(new VoidVisitorWithDefaults<Void>() {
                        @Override
                        public void visit(MethodDeclaration n, Void arg) {


                            if (n.getName().toString().equals(targetedMethodName)){
                                returnType = n.getType().toString();
                                methodFound[0] = true;
                                if (n.getBody().isPresent()){
                                    result[0] = n.getBody().get().asBlockStmt();
                                }
                            }

                        }
                    } , null);
                }
            }
        }
        if (!methodFound[0]){
            throw new NoSuchMethodException("the Method: " + targetedMethodName + " not found!");
        }
        return result[0];
    }

    private void executeStmtVisitor()  {


        String methodName = semanticConfigurator.getMethodName();

        if (methodName == null || methodName.equals("undefined")){
            feedbacks.add("method Not Found!");
        }
        else {
            try {
                StatementsVisitor statementsVisitor = new StatementsVisitor(getTargetedMethod(methodName));

                checkReturnTyp();

                int targetedWhile = Integer.parseInt(semanticConfigurator.getWhileLoop());
                int targetedFor = Integer.parseInt(semanticConfigurator.getForLoop());
                int targetedForEach = Integer.parseInt(semanticConfigurator.getForEachLoop());
                int targetedIfElse = Integer.parseInt(semanticConfigurator.getIfElseStmt());
                int targetedDoWhile = Integer.parseInt(semanticConfigurator.getDoWhileLoop());


                usedALoop = statementsVisitor.getWhileCounter() > 0
                        || statementsVisitor.getDoCounter() >0
                        || statementsVisitor.getForEachCounter() >0
                        || statementsVisitor.getForCounter() > 0;

                if (statementsVisitor.getWhileCounter() > targetedWhile && targetedWhile != -1){
                    feedbacks.add("You should not use no more than "+targetedWhile+" while loop in your code, but you've used "+statementsVisitor.getWhileCounter()+" while loop ");
                    //feedbacks.add("to much while loop ");
                }
                if (statementsVisitor.getForCounter() > targetedFor && targetedFor != -1){
                    feedbacks.add("You should not use no more than "+targetedFor+" for loop in your code, but you've used "+statementsVisitor.getForCounter()+"  for loop ");
                    //feedbacks.add("to much for loop ");
                }
                if (statementsVisitor.getForEachCounter() > targetedForEach && targetedForEach != -1){
                    feedbacks.add("You should not use no more than "+targetedForEach+" forEach loop in your code, but you've used "+statementsVisitor.getForEachCounter()+"  forEach loop ");
                   // feedbacks.add("to much forEach loop ");
                }
                if ( statementsVisitor.getIfElseCounter()  > targetedIfElse && targetedIfElse != -1){
                    feedbacks.add("You should not use no more than "+targetedIfElse+" IfElse Statment in your code, but you've used "+statementsVisitor.getIfElseCounter()+"  ifElse Statment ");
                   // feedbacks.add("to much ifElse  ");
                }
                if (statementsVisitor.getDoCounter() > targetedDoWhile && targetedDoWhile != -1){
                    feedbacks.add("You should not use no more than "+targetedDoWhile+" doWhile loop in your code, but you've used "+statementsVisitor.getForCounter()+"  doWhile loop ");
                    //feedbacks.add("to much do loop ");
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage() + " " + e.getCause());
            }
        }
    }

    private void executeRecursionChecker() throws Exception {
        String methodName = semanticConfigurator.getMethodName();
        boolean mustRec =Boolean.parseBoolean(semanticConfigurator.getRecursionAllowed());

        if (methodName == null || methodName.equals("undefined")){
            feedbacks.add("not found");
        }
        else {
            RecursiveChecker recursiveChecker = new RecursiveChecker(getTargetedMethod(methodName) , methodName);
            if ((!recursiveChecker.check() && mustRec && !usedALoop)){
                feedbacks.add("you have to solve the method recursive");
            }
            else if (mustRec && usedALoop && recursiveChecker.check()){
                feedbacks.add("you have used a Loop with your recursive Call");
            }
            else if (mustRec && usedALoop && !recursiveChecker.check()){
                feedbacks.add("you have used a Loop without a recursive Call, you have to solve it just recursive");
            }
            else {
                feedbacks.add("well done!");
            }

        }

    }

    private void printFeedbacks(){
        System.out.println("feedbacks");
        for (String feedback : feedbacks) {
            System.out.println(feedback);
        }
        System.out.println("feedbacks");
    }

    public String getSource() {
        return source;
    }

    @Override
    public ArrayList<String> getFeedbacks() {
        return feedbacks;
    }

    public SemanticConfigurator getSemanticConfigurator() {
        return semanticConfigurator;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }
    private int a(int a, boolean b , String c ,double e){
        return 1;
    }
    private boolean b(List a ,ArrayList b ,float c){
        return false;
    }

    private int rec(int r){

        if (true){
            if (false){

            }
        }

        for (int i = 0; i <100 ; i++)
            for (int j = 0; j <100 ; j++) {
                for (int k = 0; k <4 ; k++) {
                    System.out.println();
                }
            }


        for (int i = 0; i <10 ; i++) {
            System.out.println();
            for (int j = 0; j < 10; j++) {
                System.out.println();
                for (int k = 0; k < 15; k++) {
                    System.out.println();
                    for (int l = 0; l < 111; l++) {
                        System.out.println();
                        return 1;
                    }
                }
            }
        }
        if (r == 1) return 1;
        else return rec(r /2);
    }
//
//    public void print(){
//        if (true) System.out.println();
//        else {
//            if (true) System.out.println();
//        }
//        if (1 < 1 ) {
//            if (true) System.out.println();
//        }
//        else {
//            if (true) System.out.println();
//        }
//
//        for (int i = 0; i <100 ; i++)
//            for (int j = 0; j <100 ; j++) {
//                for (int k = 0; k <4 ; k++) {
//                    System.out.println();
//                }
//            }
//
//
//        for (int i = 0; i <10 ; i++) {
//            System.out.println();
//            for (int j = 0; j < 10; j++) {
//                System.out.println();
//                for (int k = 0; k < 15; k++) {
//                    System.out.println();
//                    for (int l = 0; l < 111; l++) {
//                        System.out.println();
//                        return;
//                    }
//                }
//            }
//        }
//    }

    public static void main(String[] args) throws Exception {

        long start = System.nanoTime();

        Map<String , String> settings = new HashMap<>();
        settings.put("methodName" , "a");
        settings.put("recursionAllowed" , "true");
        settings.put("whileLoop" , "1");
        settings.put("forLoop" , "1");
        settings.put("forEachLoop" , "1");
        settings.put("ifElseStmt" , "2");
        settings.put("doWhileLoop" , "0");
        settings.put("returnType" , "void");

        SemanticConfigurator semanticConfigurator = new SemanticConfigurator(settings);


        String source =  "/*** Test class*/import java.util.*;class TestClass {" +
                "    private int a(int a, boolean b , String c ,double e){\n" +
                "        return 1;\n" +
                "    }\n" +
                "    private boolean b(List a ,ArrayList b ,float c){\n" +
                "        return false;\n" +
                "    }" +
                "}";
        SemanticChecker semanticChecker1 = new SemanticChecker(source , semanticConfigurator);
        semanticChecker1.executeStmtVisitor();
        semanticChecker1.executeRecursionChecker();
        semanticChecker1.printFeedbacks();

//        SemanticChecker semanticChecker = new SemanticChecker(source);
        //semanticChecker.getTargetedMethod("test");
//        semanticChecker.executeStmtVisitor();
        //semanticChecker.executeRecursionChecker();

        long end = System.nanoTime() -start;
        System.out.println(end * Math.pow(10 , -9));
    }

    public Map<String, String> getSettings() {
        return settings;
    }
}
