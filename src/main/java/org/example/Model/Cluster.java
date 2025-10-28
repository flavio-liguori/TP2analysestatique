package org.example.Model;

import java.util.*;

public class Cluster {
    private Set<String> classes;
    private Cluster left;
    private Cluster right;
    private double mergeCoupling;

    // Constructeur pour une feuille (classe unique)
    public Cluster(String className) {
        this.classes = new HashSet<>();
        this.classes.add(className);
        this.left = null;
        this.right = null;
        this.mergeCoupling = 0.0;
    }

    // Constructeur pour un nœud interne (fusion de deux clusters)
    public Cluster(Cluster left, Cluster right, double coupling) {
        this.classes = new HashSet<>();
        this.classes.addAll(left.getClasses());
        this.classes.addAll(right.getClasses());
        this.left = left;
        this.right = right;
        this.mergeCoupling = coupling;
    }

    public Set<String> getClasses() {
        return classes;
    }

    public Cluster getLeft() {
        return left;
    }

    public Cluster getRight() {
        return right;
    }

    public double getMergeCoupling() {
        return mergeCoupling;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public String toString() {
        return classes.toString();
    }
}
