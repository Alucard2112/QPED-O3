package eu.qped.umr.generators;

import eu.qped.umr.compiler.Logger;
import eu.qped.umr.checkers.SyntaxErrorPredictor;
import eu.qped.umr.configs.ConfLevel;
import eu.qped.umr.compiler.Translator;
import eu.qped.umr.model.SyntaxError;

import java.util.ArrayList;

public class SyntaxFeedbackGenerator {


    private final static String ERROR_TRIGGER_CONS = "An bzw. Vor der folgenden Stelle: ";
    private final static String LINE_NUMBER_CONS = " in der Zeile: ";
    private final static String ERROR_MSG_CONS = "beienthaelt der Code die Folgenden Fehler: ";
    private final static String FEEDBACK_CONS = "und dafuer schlagen wir folgendes vor: ";
    private final static String NEW_LINE = "\n\n";
    private final static ArrayList<String> types = new ArrayList<>();

    static {
        types.add("for");
        types.add("switch");
        types.add("while");
        types.add("if");
        types.add("else");
        types.add("System");
        types.add("break");
        types.add("continue");
        types.add("case");
    }


    private final ConfLevel level;

    private final String sourceCode;
    private Translator translator;

    private StringBuilder result = new StringBuilder();

    public SyntaxFeedbackGenerator(String sourceCode, ConfLevel level) {
        this.level = level;
        this.sourceCode = sourceCode;
        translator = new Translator();
    }

    private StringBuilder appendCliche(String errorMsg, String errorTrigger, long errorLine, StringBuilder result) {

        return result.append(NEW_LINE)
                .append(ERROR_TRIGGER_CONS).append(errorTrigger)
                .append(LINE_NUMBER_CONS)
                .append(errorLine).append(NEW_LINE)
                .append(NEW_LINE).append(ERROR_MSG_CONS).append(errorMsg)
                .append(NEW_LINE)
                .append(FEEDBACK_CONS).append(NEW_LINE);
    }

    public String getFeedback(SyntaxError syntaxError)  {

        result = appendCliche(syntaxError.getErrorMsg(), syntaxError.getErrorTrigger(), syntaxError.getLine(), result);
        SyntaxErrorPredictor syntaxErrorPredictor = new SyntaxErrorPredictor(syntaxError.getErrorCode(), syntaxError.getErrorMsg(), syntaxError.getErrorTrigger());
        switch (syntaxError.getErrorCode()) {
            case "compiler.err.expected":
            case "compiler.err.expected3":
            case "compiler.err.expected1":
            case "compiler.err.expected2": {
                expectedSubSwitch(result, syntaxErrorPredictor, syntaxError);
                //System.out.println(syntaxError.getErrorTrigger());
                break;
            }
            case "compiler.err.var.might.not.have.been.initialized": {
                result.append("Das Variable musste nicht nur deklariert , sondern initialisiert werden wie: ").append(NEW_LINE)
                        .append("(Deklaration) int number = (Initialisiertung) 10")
                        .append(NEW_LINE)
                        .append("The variable must be initialized and not just declared,for Example: ").append(NEW_LINE)
                        .append("(Declaration) int number = (Initialising) 10;");
                break;
            }
            //isMatch maybe
            case "compiler.err.already.defined": {
                result
                        .append("Sie haben schon ein Variable bzw. eine Methode im gemeinsamen Scope mit dem gleichen Namen")
                        .append(NEW_LINE)
                        .append("Scope bedeutet wo das Variable bzw. Methode aufrufbar ist.")
                        .append("In jedem Scope müssen alle Variablen bzw. Methoden eindeutige Namen besitzen. ");
                break;
            }
            case "compiler.err.cant.resolve.location": {
                //for var
                result.append("Sie haben ein undefiniertes Symbol (Variable) aufgerufen, an der o.g Stelle könnte es Sein,")
                        .append(NEW_LINE)
                        .append("dass Sie ein Tipfehler mit dem Namen getan hätten,oder das Symbol vergessen zu definieren.");
                break;
            }
            case "compiler.err.abstract.cant.be.instantiated": {
                result.append("Aus abstrakten Klassen könnte es keine Objekt ertellt werden")
                .append(NEW_LINE)
                .append("man Hat die Möglickkeit ein Objekt einer Subklasse einer abstrakten klasse zu erstellen")
                .append(NEW_LINE)
                .append("ClassAbstract className = new SubClassFromClassAbstract();");
                break;
            }
            case "compiler.err.repeated.modifier": {
                try {
                    System.out.println(translator.translate("en" , "de" , syntaxError.getErrorMsg()));
                }
                catch (Exception e){
                    Logger.getInstance().log(e.getMessage() + " " + e.getCause());
                }
            }
            case "compiler.err.illegal.combination.of.modifiers": {
                result.append("Modifieres sind zusätzliche Eigenschaften für Java-Deklarationen wie Methoden,Variablen und Klassen,")
                        .append(NEW_LINE)
                        .append("man muss die immer ganz am Anfang der Deklaration angeben,")
                        .append(NEW_LINE)
                        .append("man darf auch die Kombinieren aber leider nicht wie was Sie getan haben. Beispielweise: ")
                        .append(NEW_LINE)
                        .append("Man darf fast alle Modifiers mit static kombinieren aber man darf es nicht public mit sich selbst oder mit private bzw. protected zu kombinieren ");
                try {
                    result.append(NEW_LINE);
                    result.append(translator.translate("de" , "ar" , result.toString()));
                }
                catch (Exception e){
                    Logger.getInstance().log(e.getMessage());
                }
                break;
            }
            case "compiler.err.illegal.start.of.expr": {
                String kind = syntaxErrorPredictor.getErrorKind();
                String feedbackForVar = "public, private und protected duerfen nicht innerhalb der Methode mit lokalen Variablen verwendet werden, da ihr Methodenbereich ihre Zugaenglichkeit definiert.";
                String feedbackForMethod = "Eine Methode kann keine andere Methode innerhalb ihres Gueltigkeitsbereichs haben";
                String feedbackForStrLit = "String Character Without Double Quotes \"\" Z.B: String = a;";
                //todo Feedback umschreiben (operationen)
                String feedbackForBraces = "Missing Curly \"{}\" Braces:  jede Block (Method) oder Klassendefinition mit geschweiften Klammern beginnen und enden muss";
                System.out.println(kind + " kind");
                if (kind.equals("variable")) {
                    result.append(feedbackForVar);
                } else if (kind.equals("method")) {
                    result.append(feedbackForMethod);
                } else {
                    if (level.equals(ConfLevel.beginner)) {
                        result.append(feedbackForBraces)
                                .append(NEW_LINE)
                                .append(feedbackForStrLit)
                                .append(NEW_LINE)
                                .append(feedbackForMethod)
                                .append(NEW_LINE)
                                .append(feedbackForVar);
                    } else if (level.equals(ConfLevel.professional) || level.equals(ConfLevel.advanced)) {
                        result.append(feedbackForStrLit)
                                .append(NEW_LINE)
                                .append(feedbackForBraces)
                                .append(NEW_LINE)
                                .append(feedbackForMethod)
                                .append(NEW_LINE)
                                .append(feedbackForVar);
                    }
                }
                break;
            }
            case "compiler.err.illegal.start.of.type": {
                for (String s : types) {
                    if (syntaxError.getErrorTrigger().contains(s)) {
                        result.append(" Sie haben diese Typ: ").append(s).append(" in einer falshen Stelle verwendet");
                    }
                }
                break;
            }
            case "compiler.err.not.stmt": {
                if (syntaxError.getErrorTrigger().contains("=")) {
                    result.append("Sie versuchen ein Statement zu instialisieren, aber die Deklaration war leider falsch. ");
                    result.append(NEW_LINE);
                    result.append("Ein Variable z.B kann man so in Java definieren: <Datentyp> varName = value;");
                }
                result.append("Sie haben Java Statment verletzt weil es in Java folgende Statments gibt: ")
                        .append(NEW_LINE)
                        .append("1) Expression Statments : um die Werte eine Datenfeld zu ändern oder Methoden aufzuruden oder Objekt zu erstellen ")
                        .append(NEW_LINE).append("int <varName> = value1; ").append(NEW_LINE).append("<varName> = value2;")
                        .append("2) Declaration Statment : um Variablen zu deklarieren Z.B : int <varName> ;  ");
                break;
            }
            case "compiler.err.unclosed.str.lit": {
                // str x = "x;
                result.append("Wenn Sie eine Zeichenkette (String) mit der Sprache Java definieren wollen")
                .append(NEW_LINE)
                .append("wäre es richtig, wenn Sie innerhalb zwei Einführungszeichen schreiben")
                .append(NEW_LINE)
                .append("Wie: String <var name> =\"value\" ");
                break;
            }
            case "compiler.err.premature.eof":
            case "compiler.err.var.not.initialized.in.default.constructor":
            case "compiler.err.non-static.cant.be.ref":
            case "compiler.err.invalid.meth.decl.ret.type.req":
            case "compiler.err.override.static":
            case "compiler.err.cant.resolve.location.args":
            case "compiler.err.cant.apply.symbol":
            case "compiler.err.generic.array.creation": {
                try{
                    result.append(translator.translate("en" , "de" , syntaxError.getErrorMsg()));
                }
                catch (Exception e){
                    Logger.getInstance().log(e.getMessage() + " " + e.getCause());
                }
                break;
            }
            case "compiler.err.prob.found.req": {
                if (syntaxError.getErrorMsg().equals("incompatible types: int cannot be converted to boolean")) {
                    try{
                        result.append(translator.translate("en" , "de" , syntaxError.getErrorMsg()));
                    }
                    catch (Exception e){
                        Logger.getInstance().log(e.getMessage() + " " + e.getCause());
                    }
                    result.append("feedback for incompatible types: int cannot be converted to boolean");
                } else if (syntaxError.getErrorMsg().equals("incompatible types: possible lossy conversion from double to int")) {
                    try{
                        result.append(translator.translate("en" , "de" , syntaxError.getErrorMsg()));
                    }
                    catch (Exception e){
                        Logger.getInstance().log(e.getMessage() + " " + e.getCause());
                    }
                } else{
                    try{
                        result.append(translator.translate("en" , "de" , syntaxError.getErrorMsg()));
                    }
                    catch (Exception e){
                        Logger.getInstance().log(e.getMessage() + " " + e.getCause());
                    }
                }
                break;
            }
            case "compiler.err.else.without.if": {
                result.append("wenn man Bedingungenen in Java überprüfen will,verwendet man dazu die if-Anweisung, dazu kann man else hinzufügen ")
                        .append("aber ein Else-Anweisung ohne if ist problematisch");
                break;
            }
            case "compiler.err.missing.ret.stmt": {
                //non-void method without return.
                result.append("Jede Methode, deren Rückgabetyp nicht void ist , braucht ein\"return\" am Ende.");
                break;
            }
            case "compiler.err.unreachable.stmt": {
                //stmt after return
                result.append("Return schließt immer eine Methode,deswegen kann man kein Anweisungen nach einem return reichen ");
                break;
            }
            case "compiler.err.missing.meth.body.or.decl.abstract": {
                //put a ; just before the first {
                result.append("Die Deklaration einer Methode besteht aus 2 Schritten: ")
                        .append(NEW_LINE)
                        .append("Methoden Kopf: <Rückgabetyp> MethodeName ()")
                        .append(NEW_LINE)
                        .append("Methoderumpf: {Code block und ein return wenn nötig}");
                if(!level.equals(ConfLevel.beginner)){
                    result .append(NEW_LINE)
                            .append("In der abstract Klasse kann man eine Methodekopf ohne Methoderumpf deklarieren aber mit mit dem Schluesselwort \"abstract\" ");
                }


                break;
            }
            default:
                result.append(syntaxError.getErrorMsg()).append(LINE_NUMBER_CONS).append(syntaxError.getLine());
        }
        result.append(NEW_LINE).append(" ---------------------------------------------------------------------  ");
        return result.toString();
    }


    private void expectedSubSwitch(StringBuilder result, SyntaxErrorPredictor syntaxErrorPredictor, SyntaxError syntaxError) {

        String forSemExp = syntaxError.getAdditionalProperties().get("forSemExpected");

        switch (syntaxError.getErrorMsg()) {
            case "';' expected":
                if (forSemExp != null) {
                    if (syntaxErrorPredictor.hasBraces(forSemExp)) {
                        result.append("klammer vergessen neu");
                    } else {
                        result.append("jedes Java Statment muss mit einem Semikolon beendet werden, z.B: ")
                                .append(NEW_LINE)
                                .append("String richtigerCode = \"hello World!\";  ")
                                .append(NEW_LINE)
                                .append("Every java statement must end with a Semicolon, for Example: ")
                                .append(NEW_LINE)
                                .append("int oddNumber = 7;");
                    }
                }
                break;
            case "'(' expected":
            case "= expected":
            case "'.class' expected":
            case "'[' expected":
                try{
                    result.append(translator.translate("en" , "de" , syntaxError.getErrorMsg()));
                }
                catch (Exception e){
                    Logger.getInstance().log(e.getMessage() + " " + e.getCause());
                }
                break;
            case "<identifier> expected":
                if (syntaxErrorPredictor.getErrorKind().equals("method")) {
                    result.append("Sie haben einen Codeblock irgendwo geschrieben, wo Java ihn nicht erwartet, Z.B: System.out.println(\"Hello\"); innerhalb der Klasse ausserhalb einer Methode");
                } else if (syntaxErrorPredictor.getErrorKind().equals("variable")) {
                    result.append("Sie haben einen Datenfeld ohne Name definiert, man kann   den Datenfeld so Z.B: int variable = 5; definieren");
                }
                break;
            case "class, interface, or enum expected":
                boolean isNumberOfBrEq = syntaxErrorPredictor.hasEqualNumberOfBraces(getSourceCode());
                if (isNumberOfBrEq) {
                    result.append("wenn man eine Methode ausserhalb der Klasse zu schreiben versucht, mann kann dieser Fehler aufheben wenn mann diese Methode zu innerhalb der Klasse verschibt");
                } else {
                    result.append("zusaetzliche geschweifte Klammer \"}\" Hier kann der Fehler durch einfaches Entfernen der zusartzlichen geschweifte Klammer \"}\" oder durch Beobachten der Vertiefung korrigiert werden");
                }
                break;
            default:
                result.append(syntaxError.getErrorMsg());
        }
    }

    public String getSourceCode() {
        return sourceCode;
    }
}

