import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * Structure for holding stop information
 * Needs to be Comparable in order to be able to order them for input in the interface
 */

public class Stop implements Comparable<Stop> {    
    // location of the stop
    private GisPoint loc;
    private String name;
    private String id;

    // data structure for holding the neighbouring stops on a form of transport
    private Collection<Stop> transportNeighbours = new HashSet<Stop>();

    // data structure for holding a link to the lines that stop is part of   
    private Collection<Line> lines = new HashSet<Line>();
    
    // data structure for holding the neighbouring stops in walking
    private Collection<Stop> walkingNeighbours = new HashSet<Stop>();
    

    /**
     * Constructor for a stop
     * 
     * @param id   4 or 5 digit stop id
     * @param name Long name for the stop
     * @param lat
     * @param lon
     */
    public Stop(double lon, double lat, String name, String id) {
        this.loc = new GisPoint(lon, lat);
        this.name = name;
        this.id = id;
    }

    //--------------------------------------------
    //  Getters and basic properties of a Stop
    //--------------------------------------------

    /**
     * Get the location of the stop
     * @return GisPoint object of location on earth
     */
    public GisPoint getPoint() {
        return this.loc;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    /**
     * Returns distance in meters between this stop and a GisPoint
     */
    public double distanceTo(GisPoint loc) {
        return this.loc.distance(loc);
    }
    
    /**
     * Returns distance in meters between this stop and another stop
     */
    public double distanceTo(Stop toStop) {
        return this.loc.distance(toStop.loc);
    }

    /**
     * Compare by alphabetic order of name,
     * If two stops have the same name, then
     * compare their id's in case they are not the same stop.
     */
    public int compareTo(Stop other){
        int ans = this.name.compareTo(other.name);
        if (ans!=0) {return ans;}
        return this.id.compareTo(other.id);
    }

    /** 
     * Display a stop
     * @return string of the stop information in the format: XXXX: long name at (lon,lat)
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(id). append(": ");
        str.append(name).
            append(" at (").
            append(loc.getLon()).
            append(", ").
            append(loc.getLat()).
            append(")");
        return str.toString();
    }

    /**
     * @param a GisPoint to check if the stop is in an **identical** location
     * @return is this stop in the same location as the given point
     */
    public boolean atLocation(GisPoint point) {
        return this.loc.equals(point);
    }


    //-------------------------
    // Setting and getting the lines through this stop
    //-------------------------

    /**
     * adding a line that goes through this stop
     * @param line
     */
    public void addLine(Line line) {
        this.lines.add(line);
    }

    /**
     * Return the lines that this stop is on 
     */
    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(this.lines);
    }

    //--------------------------------------------
    //  Setting and getting the neighbours of the stop
    //
    //  edges is a collection of the (directed) edges out of the stop, 
    //--------------------------------------------

    public Collection<Stop> getTransportNeighbours() {
        return Collections.unmodifiableCollection(transportNeighbours);
    }

    public Collection<Stop> getWalkingNeighbours() {
        return Collections.unmodifiableCollection(walkingNeighbours);
    }

    public Collection<Stop> getNeighbours() {
        // For cases when you want *everything* (transport + walking)
        Set<Stop> all = new HashSet<>(transportNeighbours);
        all.addAll(walkingNeighbours);
        return all;
    }

    public void addTransportNeighbour(Stop neighbour) {
        transportNeighbours.add(neighbour);
    }

    public void addWalkingNeighbour(Stop neighbour) {
        walkingNeighbours.add(neighbour);
    }
    
    public void removeWalkingNeighbours(){
        walkingNeighbours.clear();
    }
}
