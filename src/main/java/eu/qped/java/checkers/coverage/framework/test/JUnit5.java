package eu.qped.java.checkers.coverage.framework.test;

import org.junit.platform.engine.*;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.*;
import org.junit.platform.launcher.listeners.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * JUnit 5 is used to run a set of test classes.
 * @author Herfurth
 * @version 1.0
 * @see <a href="https://junit.org/junit5/docs/current/user-guide/#launcher-api">JUnit5 API</a>
 */
class JUnit5 implements TestFramework {
    /**
     * SEGMENT_CLASS, SEGMENT_METHOD describes a segment in the class TestExecutionSummary
     */
    private static final String SEGMENT_CLASS = "class", SEGMENT_METHOD = "method";
    private final JUnit5Parser parser;

    JUnit5() {
        parser = new JUnit5Parser();
    }

    @Override
    public TestCollection testing(List<String> testClasses, ClassLoader loader, TestCollection collection) throws Exception {
        Objects.requireNonNull(testClasses, "ERROR: TestFramework.testing() parameter testClasses can't be null");
        Objects.requireNonNull(collection, "ERROR: TestFramework.testing() parameter collection can't be null");
        return convert(collection, run(testClasses, loader));
    }

    private TestExecutionSummary run(List<String> testClasses, ClassLoader loader) {
        if (Objects.nonNull(loader))
            Thread.currentThread().setContextClassLoader(loader);

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                .selectors(testClasses.stream().map(t -> selectClass(t)).collect(Collectors.toList()))
                .build();
        SummaryGeneratingListener summary = new SummaryGeneratingListener();

        Optional<TestEngine> testEngine = ServiceLoader.load(TestEngine.class).findFirst();
        if (testEngine.isEmpty())
            return  summary.getSummary();

        LauncherConfig launcherConfig = LauncherConfig
                .builder()
                .enableTestEngineAutoRegistration(false)
                .enableTestExecutionListenerAutoRegistration(false)
                .addTestExecutionListeners(summary)
                .addTestEngines(testEngine.get())
                .build();

        Launcher launcher = LauncherFactory.create(launcherConfig);
        TestPlan plan = launcher.discover(request);
        launcher.execute(plan);

        return summary.getSummary();
    }

    private TestCollection convert(TestCollection collection, TestExecutionSummary summary) {
        for (TestExecutionSummary.Failure failure : summary.getFailures()) {
            if (! parser.parse(failure.getException().toString()))
                continue;

            String className = null, methodName = null;
            for (UniqueId.Segment segment : failure
                    .getTestIdentifier()
                    .getUniqueIdObject()
                    .getSegments()) {

                if (SEGMENT_CLASS.equals(segment.getType())) {
                    className = simpleClassName(segment.getValue());
                    if (Objects.nonNull(methodName)) {
                        collection.add(new TestResult(
                                className,
                                methodName,
                                parser.want(),
                                parser.got()));
                        continue;
                    }
                } else if (SEGMENT_METHOD.equals(segment.getType())) {
                    methodName = convertMethodName(segment.getValue());
                    if (Objects.nonNull(className)) {
                        collection.add(new TestResult(
                                className,
                                methodName,
                                parser.want(),
                                parser.got()));
                        continue;
                    }
                }
            }
        }
        return collection;
    }

    private String simpleClassName(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    private String convertMethodName(String name) {
        return name.replace("()", "");
    }

}
