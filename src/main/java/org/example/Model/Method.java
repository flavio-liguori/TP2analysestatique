package org.example.Model;

public class Method {
    public final String className;
    public final String methodName;

    public Method(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return className + "." + methodName;
    }
}
