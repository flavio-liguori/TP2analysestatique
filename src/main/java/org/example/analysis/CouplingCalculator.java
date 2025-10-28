package org.example.analysis;

import org.example.Model.*;
import java.util.*;

public class CouplingCalculator {

    public static Map<String, Double> computeCoupling(List<Call> calls) {
        Map<String, Integer> interClassCalls = new HashMap<>();
        int totalInterClass = 0;

        for (Call c : calls) {
            String a = c.caller.className;
            String b = c.callee.className;
            if (!a.equals(b)) {
                String key = a.compareTo(b) < 0 ? a + "-" + b : b + "-" + a;
                interClassCalls.put(key, interClassCalls.getOrDefault(key, 0) + 1);
                totalInterClass++;
            }
        }

        Map<String, Double> coupling = new HashMap<>();
        for (Map.Entry<String, Integer> e : interClassCalls.entrySet()) {
            coupling.put(e.getKey(), (double) e.getValue() / totalInterClass);
        }

        return coupling;
    }
}
