//package eu.qped.java.checkers.semantics;
//
//import eu.qped.java.checkers.mass.QFSemSettings;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SemanticCheckerTestReport {
//
//    @ParameterizedTest
//    @MethodSource("provideForParameters")
//    void checkForLoop(String code, QFSemSettings qfSemSettings) {
//        SemanticSettingReader semanticSettingReader = SemanticSettingReader.createSemanticConfigurator(qfSemSettings);
//        SemanticChecker semanticChecker = SemanticChecker.createSemanticMassChecker(semanticSettingReader);
//        semanticChecker.setReturnType("void");
//        semanticChecker.setSource(code);
//        semanticChecker.check();
//
//        assertEquals("You should not use no more than 0 for loop in your code, but you've used 1  for loop ", semanticChecker.getFeedbacks().get(0).getBody());
//
//    }
//
//    @ParameterizedTest
//    @MethodSource("provideRecursionParameters")
//    void checkRecursionLoop(String code, QFSemSettings qfSemSettings) {
//        SemanticSettingReader semanticSettingReader = SemanticSettingReader.createSemanticConfigurator(qfSemSettings);
//        SemanticChecker semanticChecker = SemanticChecker.createSemanticMassChecker(semanticSettingReader);
//        semanticChecker.setReturnType("void");
//        semanticChecker.setSource(code);
//        semanticChecker.check();
//        assertEquals("well done!", semanticChecker.getFeedbacks().get(0).getBody());
//
//    }
//
//    private static Stream<Arguments> provideRecursionParameters() {
//        return Stream.of(
//                Arguments.of(
//                        "class Test {\n" +
//                                "        void rec(){\n" +
//                                "            rec();\n" +
//                                "        }\n" +
//                                "    }"
//                        , QFSemSettings.builder()
//                                .methodName("rec")
//                                .recursionAllowed("true")
//                                .whileLoop("-1")
//                                .forLoop("-1")
//                                .forEachLoop("-1")
//                                .ifElseStmt("-1")
//                                .doWhileLoop("-1")
//                                .returnType("void")
//                                .build()
//                )
//        );
//    }
//
//    private static Stream<Arguments> provideForParameters() {
//        return Stream.of(
//                Arguments.of(
//                        "class Test {\n" +
//                        "        void rec(){\n" +
//                        "            for(int i = 1 ; i < 10 ; i++){\n" +
//                        "                System.out.println(\"Hallo\");\n" +
//                        "            }\n" +
//                        "        }\n" +
//                        "    }"
//                        , QFSemSettings.builder()
//                                .methodName("rec")
//                                .recursionAllowed("true")
//                                .whileLoop("-1")
//                                .forLoop("0")
//                                .forEachLoop("-1")
//                                .ifElseStmt("-1")
//                                .doWhileLoop("-1")
//                                .returnType("void")
//                                .build()
//                )
//        );
//    }
//
////    class Test {
////        void rec(){
////            rec();
////        }
////    }
//
//
//}