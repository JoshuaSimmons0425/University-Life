/**
 * AStar search uses a priority queue of partial paths
 * that the search is building.
 * Each partial path needs several pieces of information
 * to specify the path to that point, its cost so far, and
 * its estimated total cost
 */

public class SearchQueueItem implements Comparable<SearchQueueItem> {
    Stop stop;
    Edge fromEdge;
    double costSoFar;
    int timeSoFar;
    double estimatedTotal;
    
    public SearchQueueItem(Stop stop, Edge fromEdge, double costSoFar, int timeSoFar, double estimatedTotal){
        this.stop = stop;
        this.fromEdge = fromEdge;
        this.costSoFar = costSoFar;
        this.timeSoFar = timeSoFar;
        this.estimatedTotal = estimatedTotal;
    }

    // stub needed for file to compile.
    public int compareTo(SearchQueueItem other) {
        return Double.compare(this.estimatedTotal, other.estimatedTotal);
    }
}
