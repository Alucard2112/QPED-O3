package eu.qped.java.checkers.coverage.framework.coverage;

import eu.qped.java.checkers.coverage.*;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import eu.qped.java.checkers.coverage.framework.test.TestFrameworkFactory;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.*;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.*;
import java.util.*;
import java.util.stream.Collectors;

class Jacoco extends CoverageFramework {

    Jacoco(TestFrameworkFactory factory) {
        super(factory);
    }

    @Override
    public CoverageCollection analyze(CoverageCollection collection,
                                      List<CoverageFacade> testClasses,
                                      List<CoverageFacade> classes) throws Exception{
        return convertResult(collection, testClasses, classes);
    }

    private CoverageCollection convertResult(CoverageCollection collection,
                                             List<CoverageFacade> testClasses,
                                             List<CoverageFacade> classes) throws Exception {

        CoverageBuilder builder = runTests(collection, testClasses, classes);

        for (IClassCoverage cc : builder.getClasses()) {
            ArrayList<StateOfCoverage> byIndex = new ArrayList<>(cc.getLastLine());
            for (int i = 0; i <= cc.getLastLine(); i ++) {
                byIndex.add(convertState(cc.getLine(i).getStatus()));
            }
            collection.add(new CoverageClass(
                    byIndex,
                    coverageICount(cc.getBranchCounter()),
                    coverageICount(cc.getLineCounter()),
                    convertState(cc.getLineCounter().getStatus()),
                    simpleName(cc.getName())
            ));

            for (IMethodCoverage mc : cc.getMethods()) {
                try {
                    if (isStatic(mc.getName()))
                        continue;

                    collection.add(new CoverageMethod(
                            coverageICount(mc.getBranchCounter()),
                            coverageICount(mc.getLineCounter()),
                            convertState(mc.getLineCounter().getStatus()),
                            simpleName(cc.getName()),
                            mc.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return collection;
    }

    private CoverageCount coverageICount(ICounter iCounter) {
        return new CoverageCount(iCounter.getTotalCount(), iCounter.getMissedCount());
    }

    private StateOfCoverage convertState(int counter) {
        switch (counter) {
            case ICounter.EMPTY : return StateOfCoverage.EMPTY;
            case ICounter.NOT_COVERED: return StateOfCoverage.NOT;
            case ICounter.PARTLY_COVERED: return StateOfCoverage.PARTLY;
            case ICounter.FULLY_COVERED: return StateOfCoverage.FULL;
        }
        return StateOfCoverage.NOT;
    }

    private boolean isStatic(String name) {
        return name.equals("<init>");
    }

    private String simpleName(String name) {
        return name.substring(name.lastIndexOf("/") + 1);
    }

    private CoverageBuilder runTests(CoverageCollection collection,
                                     List<CoverageFacade> testClasses,
                                     List<CoverageFacade> classes) throws Exception {
        IRuntime runtime = new LoggerRuntime();
        RuntimeData runtimeData = new RuntimeData();
        Instrumenter instrumenter = new Instrumenter(runtime);

        runtime.startup(runtimeData);

        MemoryLoader memoryLoader = new MemoryLoader(this.getClass().getClassLoader());
        for (CoverageFacade testClass : testClasses) {
            memoryLoader.upload(testClass.className(), testClass.byteCode());
        }
        for (CoverageFacade clazz : classes) {
            memoryLoader.upload(clazz.className(), instrumenter.instrument(clazz.byteCode(), clazz.className()));
        }

        factory.create().testing(
                testClasses.stream().map(CoverageFacade::className).collect(Collectors.toList()),
                memoryLoader,
                collection);

        ExecutionDataStore executionDataStore = new ExecutionDataStore();
        runtimeData.collect(executionDataStore,  new SessionInfoStore(), false);
        runtime.shutdown();

        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(executionDataStore, coverageBuilder);
        for (CoverageFacade clazz : classes) {
            analyzer.analyzeClass(clazz.byteCode(), clazz.className());
        }
        return coverageBuilder;
    }

}
