package org.example.analysis;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CouplingGraphGenerator {

    public static void exportToDot(Map<String, Double> coupling) throws IOException {
        StringBuilder sb = new StringBuilder("graph Coupling {\n");
        for (var e : coupling.entrySet()) {
            String[] nodes = e.getKey().split("-");
            sb.append("  \"").append(nodes[0]).append("\" -- \"").append(nodes[1])
                    .append("\" [label=\"").append(String.format("%.2f", e.getValue())).append("\"];\n");
        }
        sb.append("}");
        Files.writeString(Path.of("coupling_graph.dot"), sb.toString());
    }
}
