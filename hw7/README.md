# CSE 373 HW7: Generating and solving mazes

## Overview
In this homework, you will implement Kruskal's algorithm to generate mazes and Dijkstra's algorithm to solve them.

You will use these files from prior assignments:

- src/main/java/datastructures/concrete/dictionaries/ChainedHashDictionary.java
- src/main/java/datastructures/concrete/dictionaries/ArrayDictionary.java
- src/main/java/datastructures/concrete/ArrayHeap.java
- src/main/java/datastructures/concrete/ChainedHashSet.java
- src/main/java/datastructures/concrete/DoubleLinkedList.java
- src/main/java/misc/Sorter.java
If you have chosen a new partner for this assignment, choose either of your submissions from and verify that these are functioning properly.

You will be modifying the following files:

- src/main/java/datastructures/concrete/ArrayDisjointSet.java
- src/main/java/datastructures/concrete/Graph.java
- src/main/java/mazes/generators/maze/KruskalMazeCarver.java
Additionally, here are a few more files that you might want to review while completing the assignment (note that this is just a starting point, not necessarily an exhaustive list):

- src/main/java/datastructures/interfaces/IDisjointSet.java
- src/test/java/datastructures/TestArrayDisjointSet.java
- src/test/java/datastructures/TestGraph.java
- src/main/java/datastructures/interfaces/IEdge.java
- src/main/java/mazes/entities/Wall.java
- src/main/java/mazes/entities/Room.java
- src/main/java/mazes/entities/Maze.java
- src/main/java/mazes/gui/MainWindow.java

Please visit the assignment page on the course website for instructions on this assignment.
**https://courses.cs.washington.edu/courses/cse373/19wi/homework/7/**
