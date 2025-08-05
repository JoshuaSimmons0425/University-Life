/**
 * Implements the A* search algorithm to find the shortest path
 *  in a graph between a start node and a goal node.
 * If start or goal are null, it returns null
 * If start and goal are the same, it returns an empty path
 * Otherwise, it returns a Path consisting of a list of Edges that will
 * connect the start node to the goal node.
 */

import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;



public class AStar {

    /**
     * Finds the shortest path between two stops
     */
    public static List<Edge> findShortestPath(Stop start, Stop goal) {
        Map<Stop, Edge> backPointer = new HashMap<>(); // Stores the backpointers of each best edge
        Map<Stop, Double> costSoFar = new HashMap<>(); // Stores the cheapest known cost to get to each Stop
        Queue<SearchQueueItem> fringe = new PriorityQueue<>(); // Stores Stops to explore, prioitised by estimated total cost
        Set<Stop> visited = new HashSet<>();
        fringe.add(new SearchQueueItem(start, null, 0, 0, estimateTime(start, goal))); // Adding the start node to the fringe with heuristic cost (h(n))
        costSoFar.put(start, 0.0); // Initializing the cost 
        if (start == null || goal == null){
            return null; // Return null if start or goal is invalid
        }
        if (start.equals(goal)){
            return Collections.emptyList(); // Return empty path if start equals goal
        } 
        // Main loop for A* algorithm
        while(!fringe.isEmpty()){
            // Remove and process the Stop with the lowest estimated total cost
            SearchQueueItem currentItem = fringe.poll();
            Stop currentStop = currentItem.stop;
            if(visited.contains(currentStop)){
                continue; // Skip the stop if already visited
            }
            visited.add(currentStop); // Add stop to visited set
            if(currentStop.equals(goal)){
                break; // Stop the search when goal is reached
            }
            // Explore all neighbouring stops connected by its edges
            for(Edge edge: currentStop.getEdgesOut()){
                Stop neighbour = edge.toStop();
                // Calculate potential wait time
                int waitPenalty = shouldAddWaitPenalty(currentItem.fromEdge, edge) ? 600 : 0;
                int edgeTravelTime = edge.getTravelTime();
                int newTimeSoFar = currentItem.timeSoFar + waitPenalty + edgeTravelTime;
                double newCost = newTimeSoFar; // Actual travel time as the cost // g(n)
                // Update the cost if its cheaper than the path found from previous edge
                if(!costSoFar.containsKey(neighbour) || newCost < costSoFar.get(neighbour)){
                    costSoFar.put(neighbour, newCost);
                    double priority = newCost + estimateTime(neighbour, goal); // Total estimated cost: f(n) = g(n) + h(n)
                    fringe.add(new SearchQueueItem(neighbour, edge, newCost, newTimeSoFar, priority)); // Add neighbour to fringe for further searching
                    backPointer.put(neighbour, edge); // Record the best known edge towards this neighbour
                }
            }
        }
        // Reconstruct the path from goal to start using the backpointers
        return reconstructPath(start, goal, backPointer);
    }
    
    /**
     * Reconstructs the shortest path by using the back pointers
     */
    private static List<Edge> reconstructPath(Stop start, Stop goal, Map<Stop, Edge> backPointer){
        List<Edge> shortestPath = new ArrayList<>();  // Store the final path of edges from start to goal
        Stop current = goal; // Start from goal
        while(!current.equals(start)){
            Edge edge = backPointer.get(current);
            shortestPath.add(edge);
            current = edge.fromStop(); // Move backwards along the path
        }
        Collections.reverse(shortestPath); // Reverse the reconstructed path 
        return shortestPath;
    }
    /**
     * Helper method to determine if a wait penalty should be applied when transiting transport types and/or lines
     */
    
    private static boolean shouldAddWaitPenalty(Edge prev, Edge next) {
        if (prev == null) return false; // No penalty for the first move
        if (prev.transpType().equals("walking") && !next.transpType().equals("walking")) {
            return true; // Walking → transport
        }
        if (!prev.transpType().equals(next.transpType())) {
            return true; // Switching transport types (e.g. Bus → Train)
        }
        if (prev.line() != null && next.line() != null && !prev.line().equals(next.line())) {
            return true; // Same mode, different line
        }
        return false;
    }
    
    /**
     * For Part 4: New heuristic helper method of getting the time between two stops based on train speed
     */
    private static double estimateTime(Stop from, Stop to){
        double dist = from.distanceTo(to);
        return dist / Transport.TRAIN_SPEED_MPS;
    }
}
