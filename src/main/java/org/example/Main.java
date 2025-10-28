package org.example;

import org.example.Model.*;
import org.example.analysis.*;
import org.example.Model.ModuleIdentifier.Module;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("========================================");
        System.out.println("  TP2 - Identification de Modules");
        System.out.println("========================================\n");

        // === EXERCICE 1 : Graphe de couplage ===
        System.out.println("--- EXERCICE 1 : Graphe de couplage ---\n");

        List<Call> calls = Analyzer.extractCalls("/home/etudiant/Documents/sample-java-project/src/main");
        System.out.println("Nombre d'appels extraits : " + calls.size() + "\n");

        Map<String, Double> coupling = CouplingCalculator.computeCoupling(calls);
        CouplingGraphGenerator.exportToDot(coupling);

        System.out.println("Couplage entre classes :");
        coupling.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(e -> System.out.println("  " + e.getKey() + " = " +
                        String.format("%.4f", e.getValue())));

        System.out.println("\n========================================\n");

        // === EXERCICE 2 : Clustering hiérarchique ===
        System.out.println("--- EXERCICE 2 : Clustering et Modules ---\n");

        Cluster dendrogram = HierarchicalClustering.performClustering(coupling);

        System.out.println("\nDendrogramme :");
        HierarchicalClustering.printDendrogram(dendrogram, 0);

        DendrogramGenerator.exportDendrogramToDot(dendrogram, "dendrogram.dot");

        System.out.println("\n========================================\n");

        double cp = 0.05;
        List<Module> modules = ModuleIdentifier.identifyModules(dendrogram, coupling, cp);

        System.out.println("\n=== MODULES IDENTIFIÉS ===");
        for (Module module : modules) {
            System.out.println(module);
        }

        DendrogramGenerator.exportModulesToDot(modules, "modules.dot");

        System.out.println("\n========================================");
        System.out.println("Fichiers générés :");
        System.out.println("  - coupling_graph.dot");
        System.out.println("  - dendrogram.dot");
        System.out.println("  - modules.dot");
        System.out.println("========================================");
    }
}
