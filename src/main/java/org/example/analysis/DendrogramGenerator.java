package org.example.analysis;

import org.example.Model.Cluster;
import org.example.Model.ModuleIdentifier.Module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DendrogramGenerator {

    public static void exportDendrogramToDot(Cluster root, String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph Dendrogram {\n");
        sb.append("  node [shape=box];\n");
        sb.append("  rankdir=TB;\n\n");

        AtomicInteger nodeCounter = new AtomicInteger(0);
        buildDotTree(root, sb, nodeCounter);

        sb.append("}");
        Files.writeString(Path.of(filename), sb.toString());
        System.out.println("Dendrogramme exporté vers : " + filename);
    }

    private static String buildDotTree(Cluster cluster, StringBuilder sb,
                                       AtomicInteger counter) {
        String nodeId = "node" + counter.getAndIncrement();

        if (cluster.isLeaf()) {
            String className = cluster.getClasses().iterator().next();
            String shortName = className.substring(className.lastIndexOf('.') + 1);
            sb.append("  ").append(nodeId)
                    .append(" [label=\"").append(shortName)
                    .append("\", style=filled, fillcolor=lightblue];\n");
        } else {
            sb.append("  ").append(nodeId)
                    .append(" [label=\"").append(String.format("%.4f", cluster.getMergeCoupling()))
                    .append("\", style=filled, fillcolor=lightgray];\n");

            if (cluster.getLeft() != null) {
                String leftId = buildDotTree(cluster.getLeft(), sb, counter);
                sb.append("  ").append(nodeId).append(" -> ").append(leftId).append(";\n");
            }

            if (cluster.getRight() != null) {
                String rightId = buildDotTree(cluster.getRight(), sb, counter);
                sb.append("  ").append(nodeId).append(" -> ").append(rightId).append(";\n");
            }
        }

        return nodeId;
    }

    public static void exportModulesToDot(List<Module> modules, String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("graph Modules {\n");
        sb.append("  node [shape=box, style=filled];\n\n");

        String[] colors = {"lightblue", "lightgreen", "lightyellow",
                "lightpink", "lightcoral", "lightcyan"};

        for (Module module : modules) {
            String color = colors[module.getId() % colors.length];

            sb.append("  subgraph cluster_").append(module.getId()).append(" {\n");
            sb.append("    label=\"Module ").append(module.getId())
                    .append(" (CP=").append(String.format("%.4f", module.getAverageCoupling()))
                    .append(")\";\n");
            sb.append("    style=filled;\n");
            sb.append("    color=").append(color).append(";\n");

            for (String className : module.getClasses()) {
                String shortName = className.substring(className.lastIndexOf('.') + 1);
                sb.append("    \"").append(shortName).append("\";\n");
            }

            sb.append("  }\n\n");
        }

        sb.append("}");
        Files.writeString(Path.of(filename), sb.toString());
        System.out.println("Modules exportés vers : " + filename);
    }
}
