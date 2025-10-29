# README — Lancement rapide

Projet lancé exclusivement via `Main.java` (chemin et seuil CP sont codés en dur).

## Prérequis
- Java 17+
- Maven 3.6+
- (Optionnel) Graphviz pour convertir les `.dot` en images

## Build
```bash
mvn clean package
```
## Important de modifier le chemin source directement dans le main 

## RUN 

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```
