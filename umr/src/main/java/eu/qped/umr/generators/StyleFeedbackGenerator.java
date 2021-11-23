package eu.qped.umr.generators;


import eu.qped.umr.compiler.Translator;


import java.util.HashMap;
import java.util.Map;

public class StyleFeedbackGenerator {


    private final Map<String , String> data;
    private final static String NEWLINE = "\n";
    private  Translator translator;


    public StyleFeedbackGenerator(){
        data = new HashMap<>();
        translator = new Translator();
        setUpData();
    }

    private void setUpData(){
        data.put("\"UnnecessaryLocalBeforeReturn\"" , "Feedback for UnnecessaryLocalBeforeReturn ");
        data.put("\"CommentRequired\"" , "Feedback for CommentRequired");
        data.put("\"NoPackage\"" , "Feedback for NoPackage ");
        data.put("\"MethodNamingConventions\"" , "MethodNamingConventions");
        data.put("\"VariableNamingConventions\"" , "VariableNamingConventions");
        data.put("\"FieldNamingConventions\"" , "FieldNamingConventions");
        data.put("\"IfStmtsMustUseBraces\"" , "IfStmtsMustUseBraces");
        data.put("\"WhileLoopMustUseBraces\"" , "Feedback for While Braces");
        data.put("\"ForLoopMustUseBraces\"" , "Feedback for loop Braces");
        data.put("\"BooleanGetMethodName\"" , "BooleanGetMethodName");
        data.put("\"ClassNamingConventions\"" , "Feedback for ClassNamingConventions");
        data.put("\"shortMethodName\"" , "Feedback for shortMethodName");
        data.put("\"AbstractClassWithoutAnyMethod\"" , "Feedback for AbstractClassWithoutAnyMethod ");
        data.put("\"LogicInversion\"" , "Feedback for LogicInversion");
        data.put("\"SimplifyBooleanExpressions\"" , "Feedback for SimplifyBooleanExpressions");
        data.put("\"SimplifyBooleanReturns\"" , "Feedback for SimplifyBooleanReturns");
        data.put("\"CommentContent\"" , "Feedback for CommentContent");
        data.put("\"CommentSize\"" , "Feedback for CommentSize");
        data.put("\"UncommentedEmptyConstructor\"" , "Feedback for UncommentedEmptyConstructor");
        data.put("\"UncommentedEmptyMethodBody\"" , "Feedback for UncommentedEmptyMethodBody");
        data.put("\"ShortVariable\"" , "Variablen muessen deutlich und auf Englisch benannt werden.\n\n Sie haben das Variable, das in der Beschreibung erwaehnt wurde zu klein und unverstaendlich deklariert. ");
        data.put("\"AvoidFieldNameMatchingMethodName\"" , "Feedback for AvoidFieldNameMatchingMethodName");
        data.put("\"AvoidFieldNameMatchingTypeName\"" , "Feedback for AvoidFieldNameMatchingTypeName ");
        data.put("\"AvoidMultipleUnaryOperators\"" , "AvoidMultipleUnaryOperators");
        data.put("\"DontUseFloatTypeForLoopIndices\"" , "DontUseFloatTypeForLoopIndices");
        data.put("\"EmptyIfStmt\"" , "EmptyIfStmt");
        data.put("\"EmptyStatementBlock\"" , "EmptyStatementBlock");
        data.put("\"EmptyStatementNotInLoop\"" , "EmptyStatementNotInLoop");
        data.put("\"EmptySwitchStatements\"" , "EmptySwitchStatements");
        data.put("\"EmptyTryBlock\"" , "EmptyTryBlock");
        data.put("\"EmptyWhileStmt\"" , "Feedback for EmptyWhileStmt");
        data.put("\"EqualsNull\"" , "Feedback for EqualsNull");
        data.put("\"IdempotentOperations\"" , "Feedback for IdempotentOperations ");
        data.put("\"ImportFromSamePackage\"" , "Feedback for ImportFromSamePackage");
        data.put("\"JumbledIncrementer\"" , "Feedback for JumbledIncrementer");
        data.put("\"MethodWithSameNameAsEnclosingClass\"" , "Feedback for MethodWithSameNameAsEnclosingClass");
        data.put("\"MisplacedNullCheck\"" , "Feedback for MisplacedNullCheck");
        data.put("\"ReturnEmptyArrayRatherThanNull\"" , "Feedback for ReturnEmptyArrayRatherThanNull");
        data.put("\"UnconditionalIfStatement\"" , "Feedback for UnconditionalIfStatement");
        data.put("\"OnlyOneReturn\"" , "it's easy to write [if( condition ) return true; else return false] as [return condition]" );
    }

    public Map<String , String> getData(){
        return this.data;
    }

    public void addFeedback(String violationRule, String feedback){
        data.put(violationRule , feedback);
    }

    public String getFeedback (String rule) {

        StringBuilder result = new StringBuilder();

        for(Map.Entry<String , String> entry : data.entrySet()){
            if (entry.getKey().equals(rule)){
                result.append(entry.getValue()).append("\n");
            }
        }
        return result.toString();
    }
}
