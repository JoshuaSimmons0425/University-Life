import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

//=============================================================================
//   Finding Components
//   Finds all the strongly connected subgraphs in the graph
//   Constructs a Map recording the number of the subgraph for each Stop
//=============================================================================

public class Components{

    // Based on Kosaraju's_algorithm
    // https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
    // Use a visited set to record which stops have been visited

    
    public static Map<Stop,Integer> findComponents(Graph graph) {
        Set<Stop> visited = new HashSet<>(); // Set to track visited stops
        List<Stop> postOrder = new ArrayList<>(); // List to store stops in post-order of DFS
        
        // Step 1: Perform DFS to compute post-order on the original graph
        for(Stop stop : graph.getStops()){
            if(!visited.contains(stop)){
                dfsPostOrder(stop, visited, postOrder);
            }
        }
        
        // Step 2: Cleat the visited set for reuse in the second DFS
        visited.clear();
        Map<Stop, Integer> components = new HashMap<>(); // Map to associate each stop with a component ID
        int componentId = 0; // Component ID initilisation
        
        //Step 3: Process stops in reverse post-order to identify components in the transposed graph
        for(int i = postOrder.size() - 1; i >= 0; i--){
            Stop stop = postOrder.get(i);
            if(!visited.contains(stop)){
                // Start a DFS from this unvisited node in the transposed graph
                dfsAssignComponent(stop, visited, components, componentId);
                componentId++; // Increment component ID for the next SCC
            }
        }
        
        return components; // Returning the map of stops to their corresponding IDs
    }
    
    // Helper method to perform DFS and fill postOrder list
    public static void dfsPostOrder(Stop current, Set<Stop> visited, List<Stop> postOrder){
        visited.add(current); // Mark the current stop as visited
        for(Edge edge: current.getEdgesOut()){ // Traverse all outgoing edges
            Stop neighbour = edge.toStop(); // Get the destination stop of the edge
            if(!visited.contains(neighbour)){
                dfsPostOrder(neighbour, visited, postOrder); // Recursive DFS call
            }
        }
        postOrder.add(current); // Add stop to list after visting all descendents
    }
    
    // Helper method to assign component IDs in the transposed graph
    public static void dfsAssignComponent(Stop current, Set<Stop> visited, Map<Stop, Integer> components, int id){
        visited.add(current); // Mark the stop as visited
        components.put(current, id); // Assign the current stop to a component
        for(Edge edge: current.getEdgesIn()){ // Traverse all incoming edges (transposed graph)
            Stop neighbour = edge.fromStop(); // Get the source stop of the edge (reverse direction)
            if(!visited.contains(neighbour)){
                dfsAssignComponent(neighbour, visited, components, id); // Recursive DFS call
            }
        }
    }
}
