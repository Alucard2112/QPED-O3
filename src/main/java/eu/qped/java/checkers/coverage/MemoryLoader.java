package eu.qped.java.checkers.coverage;

import java.util.*;

/**
 * Loads a given class into memory
 * @author Herfurth
 */
public class MemoryLoader extends ClassLoader {
    public MemoryLoader() {

    }

    public MemoryLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    private final Map<String, byte[]> memory = new HashMap<>();

    public void upload(String classname, byte[] classCode) {
        this.memory.put(classname, classCode);
    }

    @Override
    protected Class<?> loadClass(String classname, boolean resolve) throws ClassNotFoundException {
        if (memory.containsKey(classname)) {
            final byte[] classCode = memory.get(classname);
            return defineClass(classname, classCode, 0, classCode.length);
        }
        return super.loadClass(classname, resolve);
    }

}