package org.example.Model;

import org.example.Model.Cluster;
import java.util.*;

public class HierarchicalClustering {

    public static Cluster performClustering(Map<String, Double> coupling) {
        Set<String> allClasses = extractAllClasses(coupling);

        List<Cluster> clusters = new ArrayList<>();
        for (String className : allClasses) {
            clusters.add(new Cluster(className));
        }

        System.out.println("=== Début du clustering hiérarchique ===");
        System.out.println("Nombre de classes initiales : " + clusters.size());

        while (clusters.size() > 1) {
            ClusterPair bestPair = findBestPairToMerge(clusters, coupling);

            if (bestPair == null) {
                bestPair = new ClusterPair(clusters.get(0), clusters.get(1), 0.0);
            }

            System.out.println("\nFusion : " + bestPair.c1.getClasses() +
                    " + " + bestPair.c2.getClasses() +
                    " (couplage = " + String.format("%.4f", bestPair.coupling) + ")");

            Cluster merged = new Cluster(bestPair.c1, bestPair.c2, bestPair.coupling);

            clusters.remove(bestPair.c1);
            clusters.remove(bestPair.c2);
            clusters.add(merged);

            System.out.println("Clusters restants : " + clusters.size());
        }

        System.out.println("\n=== Clustering terminé ===\n");
        return clusters.get(0);
    }

    private static ClusterPair findBestPairToMerge(List<Cluster> clusters,
                                                   Map<String, Double> coupling) {
        ClusterPair bestPair = null;
        double maxCoupling = -1;

        for (int i = 0; i < clusters.size(); i++) {
            for (int j = i + 1; j < clusters.size(); j++) {
                Cluster c1 = clusters.get(i);
                Cluster c2 = clusters.get(j);

                double avgCoupling = computeAverageCoupling(c1, c2, coupling);

                if (avgCoupling > maxCoupling) {
                    maxCoupling = avgCoupling;
                    bestPair = new ClusterPair(c1, c2, avgCoupling);
                }
            }
        }

        return bestPair;
    }

    private static double computeAverageCoupling(Cluster c1, Cluster c2,
                                                 Map<String, Double> coupling) {
        double sum = 0.0;
        int count = 0;

        for (String class1 : c1.getClasses()) {
            for (String class2 : c2.getClasses()) {
                String key = class1.compareTo(class2) < 0 ?
                        class1 + "-" + class2 : class2 + "-" + class1;

                if (coupling.containsKey(key)) {
                    sum += coupling.get(key);
                    count++;
                }
            }
        }

        return count > 0 ? sum / count : 0.0;
    }

    private static Set<String> extractAllClasses(Map<String, Double> coupling) {
        Set<String> classes = new HashSet<>();
        for (String key : coupling.keySet()) {
            String[] parts = key.split("-");
            classes.add(parts[0]);
            classes.add(parts[1]);
        }
        return classes;
    }

    private static class ClusterPair {
        Cluster c1;
        Cluster c2;
        double coupling;

        ClusterPair(Cluster c1, Cluster c2, double coupling) {
            this.c1 = c1;
            this.c2 = c2;
            this.coupling = coupling;
        }
    }

    public static void printDendrogram(Cluster root, int level) {
        String indent = "  ".repeat(level);

        if (root.isLeaf()) {
            System.out.println(indent + "└─ " + root.getClasses());
        } else {
            System.out.println(indent + "├─ [Couplage: " +
                    String.format("%.4f", root.getMergeCoupling()) + "]");
            if (root.getLeft() != null) {
                printDendrogram(root.getLeft(), level + 1);
            }
            if (root.getRight() != null) {
                printDendrogram(root.getRight(), level + 1);
            }
        }
    }
}
