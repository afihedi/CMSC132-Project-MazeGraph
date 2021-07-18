package graph;
import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

/** 
 * <P>The MazeGraph is an extension of WeightedGraph.  
 * The constructor converts a Maze into a graph.</P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/* STUDENTS:  SEE THE PROJECT DESCRIPTION FOR A MUCH
	 * MORE DETAILED EXPLANATION ABOUT HOW TO WRITE
	 * THIS CONSTRUCTOR
	 */
	
	/** 
	 * <P>Construct the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * <P>Each juncture in the maze should be added as a
	 * vertex to this graph.</P>
	 * 
	 * <P>For every pair of adjacent junctures (A and B) which
	 * are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight
	 * to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can
	 * be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight",
	 * etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The 
	 * Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. should be used to obtain the weights.)</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		super(); //constructor that instantiates the weighted graph class
		
		//adds junctures as vertecies to the weighted graph to fit the dimensions of the maze
		for(int y = 0; y < maze.getMazeHeight(); y++) {
			for(int x = 0; x < maze.getMazeWidth(); x++) {
				this.addVertex(new Juncture(x,y));
			}
		}
		
		//adds the edges for each juncture to adjacent junctures if there's no wall
		for(int y = 0; y < maze.getMazeHeight(); y++) {
			for(int x = 0; x < maze.getMazeWidth(); x++) {
				
				Juncture juncture = new Juncture(x,y); //the juncture currently being handled 
				Juncture left = new Juncture(x-1, y); //the juncture left of the current one
				Juncture right = new Juncture(x+1, y); //the juncture right of the current one
				Juncture above = new Juncture(x, y-1); //the juncture above the current one
				Juncture below = new Juncture(x, y+1); //the juncture below the current one
				
				//if the left juncture is within the maze
				if(this.containsVertex(left) && !maze.isWallToLeft(juncture) ) { 
					this.addEdge(juncture, left, maze.getWeightToLeft(juncture));
				}
				//if the right juncture is within the maze
				if(this.containsVertex(right) && !maze.isWallToRight(juncture)) { 
					this.addEdge(juncture, right, maze.getWeightToRight(juncture));
				}
				//if the above juncture is within the maze
				if(this.containsVertex(above) && !maze.isWallAbove(juncture)) {
					this.addEdge(juncture, above, maze.getWeightAbove(juncture));
				}
				//if the below juncture is within the maze
				if(this.containsVertex(below) && !maze.isWallBelow(juncture)) { 
					this.addEdge(juncture, below, maze.getWeightBelow(juncture));
				}
			}
		}
		
	}
}
