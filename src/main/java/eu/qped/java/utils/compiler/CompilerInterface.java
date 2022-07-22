package eu.qped.java.utils.compiler;

public interface CompilerInterface {

    boolean compileFromString(String code);

    boolean compileFromProject(String path);

}
