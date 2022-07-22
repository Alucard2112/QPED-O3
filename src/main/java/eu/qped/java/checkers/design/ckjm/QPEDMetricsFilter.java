package eu.qped.java.checkers.design.ckjm;

import gr.spinellis.ckjm.*;
import gr.spinellis.ckjm.utils.LoggerHelper;
import lombok.RequiredArgsConstructor;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convert a list of classes into their metrics.
 * Given metrics are defined in {@link DesignCheckEntryHandler.Metric}.
 *
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a> (from CKJM-extended tool)
 * @author Jannik Seus (edited)
 */
@RequiredArgsConstructor
public class QPEDMetricsFilter implements ICountingProperities {

    /**
     * The same instance of MoaClassVisitor must be used to process all class, so it must be a class field.
     */
    private final MoaClassVisitor moaVisitor;

    /**
     * container for available metrics
     */
    private final IClassMetricsContainer metricsContainer;

    public QPEDMetricsFilter() {

        metricsContainer = new ClassMetricsContainer(this);
        moaVisitor = new MoaClassVisitor(metricsContainer);
    }

    /**
     * @return true if the measurements should include calls to the Java JDK into account
     */
    public boolean isJdkIncluded() {
        //TODO Possible feature for later, include into design settings
        // True if the measurements should include calls to the Java JDK into account

        boolean dontIncludeJdk = true;
        return !dontIncludeJdk;
    }

    /**
     * @return true if the measurements should include all classes
     */
    public boolean includeAll() {
        //TODO Possible feature for later, include into design settings
        // True if the reports should only include public classes.

        boolean onlyPublic = false;
        return !onlyPublic;
    }

    /**
     * Load and parse the specified class.
     * The class specification can be either a class file name, or
     * a jarfile, followed by space, followed by a class file name.
     *
     * @param classSpecification the specification of the current class
     */
    void processClass(String classSpecification) {
        JavaClass jc;

        if (classSpecification.toLowerCase().endsWith(".jar")) {
            JarFile jf;
            try {
                jf = new JarFile(classSpecification);
                Enumeration<JarEntry> entries = jf.entries();

                while (entries.hasMoreElements()) {
                    String cl = entries.nextElement().getName();
                    if (cl.toLowerCase().endsWith(".class")) {
                        try {
                            jc = new ClassParser(classSpecification, cl).parse();
                            processClass(jc);
                        } catch (IOException e) {
                            LoggerHelper.printError("Error loading " + cl + " from " + classSpecification + ": " + e);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(QPEDMetricsFilter.class.getName()).log(Level.SEVERE, "Unable to load jar file " + classSpecification, ex);
            }
        } else {
            try {
                jc = new ClassParser(classSpecification).parse();
                processClass(jc);
            } catch (IOException e) {
                LoggerHelper.printError("Error loading " + classSpecification + ": " + e);
            }
        }
    }

    /**
     * Processes a given class using a class visitor.
     *
     * @param javaClass the given class
     */
    private void processClass(JavaClass javaClass) {

        if (javaClass != null) {
            ClassVisitor visitor = new ClassVisitor(javaClass, metricsContainer, this);
            visitor.start();
            visitor.end();

            LocClassVisitor locVisitor = new LocClassVisitor(metricsContainer);
            locVisitor.visitJavaClass(javaClass);

            DamClassVisitor damVisitor = new DamClassVisitor(javaClass, metricsContainer);
            damVisitor.visitJavaClass(javaClass);

            moaVisitor.visitJavaClass(javaClass);
            MfaClassVisitor mfaVisitor = new MfaClassVisitor(metricsContainer);
            mfaVisitor.visitJavaClass(javaClass);

            CamClassVisitor camVisitor = new CamClassVisitor(metricsContainer);
            camVisitor.visitJavaClass(javaClass);

            QPEDIcAndCbmClassVisitor icAndCbmClassVisitor = new QPEDIcAndCbmClassVisitor(metricsContainer);
            icAndCbmClassVisitor.visitJavaClass(javaClass);

            AmcClassVisitor amcVisitor = new AmcClassVisitor(metricsContainer);
            amcVisitor.visitJavaClass(javaClass);
        } else {
            LoggerHelper.printError("Given class is null.");
        }
    }

    /**
     * The interface for other Java based applications.
     * Implement the output handler to catch the results
     *
     * @param files         class files to be analyzed
     * @param outputHandler an implementation of the CkjmOutputHandler interface
     */
    public void runMetricsInternal(List<String> files, CkjmOutputHandler outputHandler) {

        files.forEach(this::processClass);
        moaVisitor.end();
        metricsContainer.printMetrics(outputHandler);
    }
}
