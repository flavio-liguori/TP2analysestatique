package org.example;

import org.example.Model.*;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.code.CtInvocation;

import java.util.*;

public class Analyzer {

    public static List<Call> extractCalls(String sourcePath) {
        Launcher launcher = new Launcher();
        launcher.addInputResource(sourcePath);
        launcher.buildModel();

        CtModel model = launcher.getModel();
        List<Call> calls = new ArrayList<>();

        // Parcourt toutes les invocations de méthode
        for (CtInvocation<?> inv : model.getElements(new TypeFilter<>(CtInvocation.class))) {
            if (inv.getExecutable() == null) continue;
            CtMethod<?> caller = inv.getParent(CtMethod.class);
            if (caller == null || inv.getExecutable().getDeclaringType() == null) continue;

            String classCaller = caller.getDeclaringType().getQualifiedName();
            String methodCaller = caller.getSimpleName();

            String classCallee = inv.getExecutable().getDeclaringType().getQualifiedName();
            String methodCallee = inv.getExecutable().getSimpleName();

            calls.add(new Call(new Method(classCaller, methodCaller),
                    new Method(classCallee, methodCallee)));
        }

        return calls;
    }
}
