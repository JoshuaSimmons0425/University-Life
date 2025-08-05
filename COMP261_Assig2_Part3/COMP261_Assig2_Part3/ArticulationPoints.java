import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

//=============================================================================
//   Finding Articulation Points
//   Finds and returns a collection of all the articulation points in the undirected
//   graph.
//   Uses the algorithm from the lectures, but modified to cope with a not completely
//   connected graph. For a not fully connected graph, an articulation point is one
//   that would break a currently connected component into two or more components
//=============================================================================

public class ArticulationPoints{
    
    
    /**
     * Return a collection of all the Stops in the graph that are articulation points.
     */
    public static Collection<Stop> findArticulationPoints(Graph graph) {
        Map<Stop, Integer> depth = new HashMap<>(); // Maps each Stop to its depth in the DFS tree
        Set<Stop> aPoints = new HashSet<>(); // Set of the articulation points that were found while traversing the graph
        int numSubTrees; // Number of DFS subtrees rooted at the current Stop (used to check if root is an articulation point)
        
        // Iterate through all Stops in the graph
        for (Stop stop: graph.getStops()){ 
           // If Stop has not been visited in DFS
           if (!depth.containsKey(stop)){ 
              numSubTrees = 0; 
              depth.put(stop, 0); // Assign depth 0 to root of DFS
              
              // Explore all neighbours of the current Stop
              for (Stop neighbour: stop.getNeighbours()){
                  
                  // If neighbour hasn't been visited, start DFS
                  if (!depth.containsKey(neighbour)){
                      recArtPts(neighbour, 1, stop, depth, aPoints); 
                      numSubTrees++; // Count each DFS subtree
                  }  
              }
              
              // If root has more than one subtree, it's an articulation point
              if (numSubTrees > 1){
                   aPoints.add(stop);
              }
           }
        }

        return aPoints; 
    }
    
    /**
     * Returns the reachback of each node in conjunction to their ancestry nodes to identify whether they're articulation points 
     */
    public static int recArtPts(Stop current, int d, Stop parent, Map<Stop, Integer> depth, Set<Stop> aPoints){
        depth.put(current, d); // Put the current node's depth in the map
        int reachBack = d; // Initialise the reach-back value to current depth
        
        // Traverse all neighbours of the current stop
        for(Stop neighbour: current.getNeighbours()){
            if(neighbour.equals(parent)){continue;} // Skip the parent node to avoid trivial cycle
            else if(depth.containsKey(neighbour)){ // If neighbour is already visited update reach-back to the smallest depth seen
                reachBack = Math.min(depth.get(neighbour), reachBack);
            }
            else{ // Recursively visit unvisted neighbour
                int childReach = recArtPts(neighbour, d+1, current, depth, aPoints);
                if(childReach >= depth.get(current)){aPoints.add(current);} // If child cannout reach an ancestor of current, then current is an articulation point
                reachBack = Math.min(childReach, reachBack); // Update reach-back based on childś reach-back
            }
        }
        return reachBack;
    }
}
