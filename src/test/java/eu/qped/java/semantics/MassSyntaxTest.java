package eu.qped.java.semantics;

import org.junit.jupiter.api.Test;

class MassSyntaxTest {

	@Test
	void test() {
//		Map<String, String> mainSettings = new HashMap<>();
//		mainSettings.put("semanticNeeded", "false");
//		mainSettings.put("syntaxLevel", "2");
//		mainSettings.put("preferredLanguage", "en");
//		mainSettings.put("styleNeeded", "false");
//
//		MainSettingsConfigurator mainSettingsConfiguratorConf = new MainSettingsConfigurator(mainSettings);
//
//		Map<String, String> semanticConf = new HashMap<>();
//
//		semanticConf.put("methodName", "recR");
//		semanticConf.put("recursionAllowed", "false");
//		semanticConf.put("whereLoop", "-1");
//		semanticConf.put("forLoop", "1");
//		semanticConf.put("forEachLoop", "-1");
//		semanticConf.put("ifElseStmt", "-1");
//		semanticConf.put("doWhileLoop", "-1");
//		semanticConf.put("returnType", "int");
//
//		QFSemSettings qfSemSettings = new QFSemSettings();
//		qfSemSettings.setMethodName("recR");
//		qfSemSettings.setRecursionAllowed("true");
//		qfSemSettings.setWhileLoop("-1");
//		qfSemSettings.setForLoop("2");
//		qfSemSettings.setForEachLoop("-1");
//		qfSemSettings.setIfElseStmt("-1");
//		qfSemSettings.setDoWhileLoop("-1");
//		qfSemSettings.setReturnType("null");
//
//		SemanticSettingReader semanticSettingReader = SemanticSettingReader.createSemanticConfigurator(qfSemSettings);
//
//		String code = " void rec (){\n" + "        System.out.println(\"pretty\");\n" + "    }";
//
//		QFStyleSettings qfStyleSettings = new QFStyleSettings();
//		qfStyleSettings.setNamesLevel("adv");
//		qfStyleSettings.setMethodName("[AA]");
//
//		StyleConfigurationReader styleConfigurationReader = StyleConfigurationReader.createStyleConfigurator(qfStyleSettings);
//
//		StyleChecker styleChecker = StyleCheckerFactory.createStyleChecker(styleConfigurationReader);
//		SemanticChecker semanticChecker = SemanticChecker.createSemanticMassChecker(semanticSettingReader);
//		SyntaxErrorChecker syntaxErrorChecker = SyntaxErrorChecker.createSyntaxErrorChecker(code);
//		final StyleChecker styleChecker1 = styleChecker;
//		final SemanticChecker semanticChecker1 = semanticChecker;
//		final SyntaxErrorChecker syntaxErrorChecker1 = syntaxErrorChecker;
//		final MainSettingsConfigurator mainSettingsConfigurator = mainSettingsConfiguratorConf;
//
//		MassExecutor massE = new MassExecutor(styleChecker1, semanticChecker1, syntaxErrorChecker1,
//				mainSettingsConfigurator);
//
////		        MassExecutor massExecutor = MassExecutorFactory.createExecutor(styleConfigurationReader, semanticSettingReader, mainSettingsConf, code);
//		massE.execute();
////		        new ArrayList<StyleViolation>(massExecutor.getViolations()).forEach(x -> System.out.println(x.getRule()));
//
//		// todo false Alarm: Here was Semicolon expected!
//
//		// Compiler compiler = new Compiler(code, styleConfigurationReader,
//		// syntaxConfigurator);
//
//		for (Feedback s : massE.semanticFeedbacks) {
//			System.out.println(s.getBody());
//		}
//
//		/*
//		 * for Style Errors
//		 */
//
//		List<StyleFeedback> feedbacks = massE.styleFeedbacks;
//
//		for (StyleFeedback f : feedbacks) {
//			System.out.println(f.getDesc());
//			System.out.println(f.getBody());
//			System.out.println(f.getLine());
//			System.out.println(f.getExample());
//			System.out.println("-----------------------------------------------------------------");
//		}
//
//		/*
//		 * for Syntax Errors
//		 */
//		List<SyntaxFeedback> arrayList = massE.syntaxFeedbacks;
//		for (SyntaxFeedback s : arrayList) {
//			System.out.println(s.getHead());
//			System.out.println(s.getBody());
//			System.out.println(s.getExample());
//			System.out.println("--------0T0----------");
//		}
//		long end = System.nanoTime() - start;
//		System.out.println("Feedback generated in: " + end * Math.pow(10.0, -9.0) + " sec");
	}

}
