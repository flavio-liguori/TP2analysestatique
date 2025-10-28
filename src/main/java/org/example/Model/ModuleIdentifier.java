package org.example.Model;

import org.example.Model.Cluster;
import java.util.*;

public class ModuleIdentifier {

    public static List<Module> identifyModules(Cluster dendrogram,
                                               Map<String, Double> coupling,
                                               double cp) {
        int totalClasses = dendrogram.getClasses().size();
        int maxModules = totalClasses / 2;

        System.out.println("=== Identification des modules ===");
        System.out.println("Nombre total de classes : " + totalClasses);
        System.out.println("Nombre maximum de modules : " + maxModules);
        System.out.println("Seuil de couplage (CP) : " + cp);

        List<Module> modules = new ArrayList<>();
        List<Cluster> candidates = new ArrayList<>();
        candidates.add(dendrogram);

        while (candidates.size() < maxModules) {
            Cluster toSplit = findLowestCouplingCluster(candidates);

            if (toSplit == null || toSplit.isLeaf()) {
                break;
            }

            candidates.remove(toSplit);
            if (toSplit.getLeft() != null) {
                candidates.add(toSplit.getLeft());
            }
            if (toSplit.getRight() != null) {
                candidates.add(toSplit.getRight());
            }
        }

        int moduleId = 1;
        for (Cluster candidate : candidates) {
            double avgCoupling = computeIntraClusterCoupling(candidate, coupling);

            if (avgCoupling >= cp || candidate.getClasses().size() == 1) {
                Module module = new Module(moduleId++,
                        new HashSet<>(candidate.getClasses()),
                        avgCoupling);
                modules.add(module);
                System.out.println("Module " + module.getId() + " : " +
                        module.getClasses() +
                        " (couplage moyen : " +
                        String.format("%.4f", avgCoupling) + ")");
            }
        }

        System.out.println("\nNombre de modules identifiés : " + modules.size());
        return modules;
    }

    private static Cluster findLowestCouplingCluster(List<Cluster> clusters) {
        Cluster lowest = null;
        double minCoupling = Double.MAX_VALUE;

        for (Cluster c : clusters) {
            if (!c.isLeaf() && c.getMergeCoupling() < minCoupling) {
                minCoupling = c.getMergeCoupling();
                lowest = c;
            }
        }

        return lowest;
    }

    private static double computeIntraClusterCoupling(Cluster cluster,
                                                      Map<String, Double> coupling) {
        List<String> classes = new ArrayList<>(cluster.getClasses());

        if (classes.size() <= 1) {
            return 0.0;
        }

        double sum = 0.0;
        int count = 0;

        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                String key = classes.get(i).compareTo(classes.get(j)) < 0 ?
                        classes.get(i) + "-" + classes.get(j) :
                        classes.get(j) + "-" + classes.get(i);

                if (coupling.containsKey(key)) {
                    sum += coupling.get(key);
                    count++;
                }
            }
        }

        return count > 0 ? sum / count : 0.0;
    }

    public static class Module {
        private int id;
        private Set<String> classes;
        private double averageCoupling;

        public Module(int id, Set<String> classes, double averageCoupling) {
            this.id = id;
            this.classes = classes;
            this.averageCoupling = averageCoupling;
        }

        public int getId() {
            return id;
        }

        public Set<String> getClasses() {
            return classes;
        }

        public double getAverageCoupling() {
            return averageCoupling;
        }

        @Override
        public String toString() {
            return "Module " + id + " [" + classes.size() + " classes] " +
                    "(couplage: " + String.format("%.4f", averageCoupling) + "): " +
                    classes;
        }
    }
}
