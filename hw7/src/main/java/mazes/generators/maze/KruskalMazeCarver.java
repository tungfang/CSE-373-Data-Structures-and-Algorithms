package mazes.generators.maze;

// import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.Graph;
import datastructures.interfaces.ISet;
import mazes.entities.Maze;
import mazes.entities.Room;
import mazes.entities.Wall;
import java.util.Random;
// import misc.exceptions.NotYetImplementedException;

/**
 * Carves out a maze based on Kruskal's algorithm.
 *
 * See the spec for more details.
 */
public class KruskalMazeCarver implements MazeCarver {
    @Override
    public ISet<Wall> returnWallsToRemove(Maze maze) {
        // Note: make sure that the input maze remains unmodified after this method is over.
        //
        // In particular, if you call 'wall.setDistance()' at any point, make sure to
        // call 'wall.resetDistanceToOriginal()' on the same wall before returning.
        ISet<Room> vertices = maze.getRooms(); // each room as a vertex
        ISet<Wall> edges = maze.getWalls(); // each wall as an edge
        Random rand = new Random();
        for (Wall edge : edges) {
            edge.setDistance(rand.nextDouble()); // assign each wall (edge) a random weight
        }

        // run Kruskal's algorithm to get walls that need to be removed
        Graph<Room, Wall> graph = new Graph<>(vertices, edges);
        ISet<Wall> result = graph.findMinimumSpanningTree();

        // repair the input maze
        for (Wall edge : edges) {
            edge.resetDistanceToOriginal();
        }
        return result;
    }
}
