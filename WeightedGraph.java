package graph;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


/**
 * <P>This class represents a general "directed graph", which could 
 * be used for any purpose.  The graph is viewed as a collection 
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P> 
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of 
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 */
public class WeightedGraph<V> {

	/* STUDENTS:  You decide what data structure(s) to use to
	 * implement this class.
	 * 
	 * You may use any data structures you like, and any Java 
	 * collections that we learned about this semester.  Remember 
	 * that you are implementing a weighted, directed graph.
	 */
	
	//the map containing the vertexes and a map with their adjacencies with corresponding weights
	Map<V, Map<V, Integer>> weightedGraph; 
	
	/* Collection of observers.  Be sure to initialize this list
	 * in the constructor.  The method "addObserver" will be
	 * called to populate this collection.  Your graph algorithms 
	 * (DFS, BFS, and Dijkstra) will notify these observers to let 
	 * them know how the algorithms are progressing. 
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;
	

	/** Initialize the data structures to "empty", including
	 * the collection of GraphAlgorithmObservers (observerList).
	 */
	public WeightedGraph() {
		weightedGraph = new HashMap<>();
		observerList = new ArrayList<GraphAlgorithmObserver<V>>();
	}

	/** Add a GraphAlgorithmObserver to the collection maintained
	 * by this graph (observerList).
	 * 
	 * @param observer
	 */
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		observerList.add(observer);	
	}

	/** Add a vertex to the graph.  If the vertex is already in the
	 * graph, throw an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	public void addVertex(V vertex) {
		if(this.containsVertex(vertex)) {
			throw new IllegalArgumentException();
		} else {
			weightedGraph.put(vertex, new HashMap<>()); 
			//adds the vertex and empty map(empty because an edge hasn't been added to vertex)
		}
	}
	
	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	public boolean containsVertex(V vertex) {
		if(weightedGraph.containsKey(vertex)) {
			return true;
		}
		return false;
	}

	/** 
	 * <P>Add an edge from one vertex of the graph to another, with
	 * the weight specified.</P>
	 * 
	 * <P>The two vertices must already be present in the graph.</P>
	 * 
	 * <P>This method throws an IllegalArgumentExeption in three
	 * cases:</P>
	 * <P>1. The "from" vertex is not already in the graph.</P>
	 * <P>2. The "to" vertex is not already in the graph.</P>
	 * <P>3. The weight is less than 0.</P>
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	public void addEdge(V from, V to, Integer weight) {
		if(weight < 0) {
			throw new IllegalArgumentException(); //throws exception if weight is negative
		}
		
		if(this.containsVertex(from) && this.containsVertex(to)) {
			weightedGraph.get(from).put(to, weight);
		} else {
			throw new IllegalArgumentException(); //throws exception if both verticies are not in map
		}
	}

	/** 
	 * <P>Returns weight of the edge connecting one vertex
	 * to another.  Returns null if the edge does not
	 * exist.</P>
	 * 
	 * <P>Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.</P>
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	public Integer getWeight(V from, V to) {
		Integer weight = null;
		if(this.containsVertex(from) && this.containsVertex(to)) {
			weight = weightedGraph.get(from).get(to);
		} else {
			throw new IllegalArgumentException();
		}
		return weight;
	}

	/** 
	 * <P>This method will perform a Breadth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without processing further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoBFS(V start, V end) {
		//notifies the observers that bfs has started
		for(GraphAlgorithmObserver<V> observers : observerList) {
			observers.notifyBFSHasBegun();
		}
		
		Set<V> visitedSet = new HashSet<>(); //list of visited verticies 
		//queue of the verticies that have been discovered
		Queue<V> discovered = new LinkedList<V>(); 
		discovered.add(start);
		
		while(!discovered.isEmpty()) {
			V out = discovered.element(); //the head of the queue, which is taken out
			discovered.remove(out);
			if(!visitedSet.contains(out)) { //could be duplicate
				
				//notifies the observers that the vertex has been visited 
				for(GraphAlgorithmObserver<V> observers : observerList) {
					observers.notifyVisit(out);
				}
				
				visitedSet.add(out); //adds the vertex to the visited set
				
				//adds each successor to the discovered queue if it hasn't been visited
				for(V successor : weightedGraph.get(out).keySet()) {
					if(!visitedSet.contains(successor)) {
						discovered.add(successor);
					}
				}
			}
			
			//if the vertex just visited is the end vertex, notifies the observers
			if(out.equals(end)) {
				for(GraphAlgorithmObserver<V> observers : observerList) {
					observers.notifySearchIsOver();
				}
				break; //ends the loop, because you're done
			}
		}
	}
	
	/** 
	 * <P>This method will perform a Depth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without visiting further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	public void DoDFS(V start, V end) {
		//notifies the observers that dfs has started
		for(GraphAlgorithmObserver<V> observers : observerList) {
			observers.notifyDFSHasBegun();
		}
		
		Set<V> visitedSet = new HashSet<>(); //list of visited verticies 
		//stack of the verticies that have been discovered
		Stack<V> discovered = new Stack<>();
		discovered.push(start);
		
		while(!discovered.empty()) {
			V out = discovered.pop(); //returns the object at the top of the stack
			if(!visitedSet.contains(out)) {
				
				//notifies the observers that the vertex has been visited 
				for(GraphAlgorithmObserver<V> observers : observerList) {
					observers.notifyVisit(out);
				}
				
				visitedSet.add(out); //adds the vertex to the visited set
				
				
				//adds each successor to the discovered queue if it hasn't been visited
				for(V successor : weightedGraph.get(out).keySet()) {
					if(!visitedSet.contains(successor) ) {
						discovered.push(successor);
					}
				}
			}
			
			//if the vertex just visited is the end vertex, notifies the observers
			if(out.equals(end)) {
				for(GraphAlgorithmObserver<V> observers : observerList) {
					observers.notifySearchIsOver();
				}
				break; //ends the loop, because you're done
			}
		}
	}
	
	/** 
	 * <P>Perform Dijkstra's algorithm, beginning at the "start"
	 * vertex.</P>
	 * 
	 * <P>The algorithm DOES NOT terminate when the "end" vertex
	 * is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.</P>
	 * 
	 * <P>Before the algorithm begins, this method goes through 
	 * the collection of Observers, calling notifyDijkstraHasBegun 
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this 
	 * method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex.  Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, 
	 * passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path 
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	public void DoDijsktra(V start, V end) {
		//notifies the observers that dijkstra has started
		for(GraphAlgorithmObserver<V> observers : observerList) {
			observers.notifyDijkstraHasBegun();
		}

		//the set with all of "finished" verticies in Dijkstra's Algorithm
		Set<V> finishedSet = new HashSet<>(); 
		finishedSet.add(start);
		for(GraphAlgorithmObserver<V> observers : observerList) {
			observers.notifyDijkstraVertexFinished(start, 0);
		}
		
		//the map that will hold the vertex with corresponding lowest cost
		Map<V, Integer> cost = new HashMap<>();
		cost.put(start, 0);
		//the map that will hold the vertex with corresponding predecessor
		Map<V, V> predecessor = new HashMap<>();
		predecessor.put(start, null);

		//the list with the path with the lowest cost
		List<V> lowestCostPath = new ArrayList<>(); 
		

		
		for(V vertex : weightedGraph.keySet()) {
			if(!vertex.equals(start)) {
				//similar to making empty Dijkstra's Algorithm table
				cost.put(vertex, Integer.MAX_VALUE); 
				predecessor.put(vertex, null);
			}
		}
		
		Integer smallestCost = 0; //the smallest cost currently
		V smallVertex = start; //the vertex with the smallest cost currently
		while(finishedSet.size() != weightedGraph.size()) {
			
			for(V vertex : weightedGraph.get(smallVertex).keySet()) {
				if(!finishedSet.contains(vertex)) {
					//if the cost found is less than the cost mapped with the vertex, replace it
					if(smallestCost + getWeight(smallVertex, vertex) < cost.get(vertex)) {
						cost.put(vertex, smallestCost + getWeight(smallVertex, vertex));
						predecessor.put(vertex, smallVertex);
					}
				}
			}
			smallestCost = Integer.MAX_VALUE;
			for(V vertex : cost.keySet()) {
				if(!finishedSet.contains(vertex)) {
					Integer temp = cost.get(vertex); //holder for the smallest cost in the cost map
					if(temp < smallestCost) { //changes if cost ends up being smaller
						smallestCost = temp;
						smallVertex = vertex;
					}
				}
			}
			
			finishedSet.add(smallVertex);
			//notifies observers that the smallest cost vertex is done
			for(GraphAlgorithmObserver<V> observers : observerList) {
				observers.notifyDijkstraVertexFinished(smallVertex, smallestCost);
			}
		}
		
		V curr = end; //the current vertex in the shortest path starting backwards
		//add to the front of the list until it reaches start vertex
		while(curr != null) {
			lowestCostPath.add(0, curr); 
			curr = predecessor.get(lowestCostPath.get(0));
		}
		
		//notifies observers that Dijkstra is done
		for(GraphAlgorithmObserver<V> observers : observerList) {
			observers.notifyDijkstraIsOver(lowestCostPath);
		}
	}
	
	
}
