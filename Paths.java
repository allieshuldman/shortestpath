
package student;
import java.util.HashMap;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import models.Edge;
import models.Node;
import models.ReturnStage;

/** This class contains Dijkstra's shortest-path algorithm and some other methods. */
public class Paths {

    /** Return the shortest path from start to end, or the empty list if a path
     * does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
    public static List<Node> shortestPath(Node start, Node end) {
        /* TODO Read note A7 FAQs on the course piazza for ALL details. */
        Heap<Node> F= new Heap<Node>(); // As in lecture slides

        // map contains an entry for each node in S or F. Thus,
        // |map| = |S| + |F|.
        // For each such key-node, the value part contains the shortest known
        // distance to the node and the node's backpointer on that shortest path.
        HashMap<Node, SFdata> map= new HashMap<Node, SFdata>();

        F.add(start, 0);
        map.put(start, new SFdata(0, null));
        // invariant: as in lecture slides, together with def of F and map
        while (F.size() != 0) {
            Node f= F.poll();
            if (f == end) return constructPath(end, map);
            int fDist= map.get(f).distance;
            
            for (Edge e : f.getExits()) {// for each neighbor w of f
                Node w= e.getOther(f);
                int newWdist= fDist + e.length;
                SFdata wData= map.get(w);

                if (wData == null) { //if w not in S or F
                    map.put(w, new SFdata(newWdist, f));
                    F.add(w, newWdist);
                } else if (newWdist < wData.distance) {
                    wData.distance= newWdist;
                    wData.backPointer= f;
                    F.updatePriority(w, newWdist);
                }
            }
        }

        // no path from start to end
        return new LinkedList<Node>();
    }
    
    /** Return the shortest path from start to end, adjusting path length weights
     * for whether or not they have a speed upgrade or is a hostile planet. If there
     * is a speed upgrade, the path weight is decreased whereas if the path leads
     * to a hostile planet, the weight is increased.  Returns the empty list if a 
     * path does not exist. */
    public static void shortestPathSpace(Node start, Node end, ReturnStage state) {
        /* TODO Read note A7 FAQs on the course piazza for ALL details. */
    	Heap<Node> F= new Heap<Node>(); // As in lecture slides

        // map contains an entry for each node in S or F. Thus,
        // |map| = |S| + |F|.
        // For each such key-node, the value part contains the shortest known
        // distance to the node and the node's backpointer on that shortest path.
        HashMap<Node, SFdata> map= new HashMap<Node, SFdata>();

        F.add(start, 0);
        map.put(start, new SFdata(0, null));
        int hostilesVisited = 0;
        // invariant: as in lecture slides, together with def of F and map
        while (F.size() != 0) {
            Node f= F.poll();
            if (f == end) {
            	go(constructPath(end, map), state);
            	return;
            }
            int fDist= map.get(f).distance;
            
            for (Edge e : f.getExits()) {// for each neighbor w of f
                Node w= e.getOther(f);
                int newWdist= fDist + e.length;
                //decreases weight if there is a speed upgrade
                if(w.hasSpeedUpgrade()) {
                	newWdist = Math.max(10, (newWdist - 100));
                }
                //increases weight if the planet is hostile
	            if(w.isHostile()) {
	            	newWdist = (int) (newWdist * (1.75 * (hostilesVisited + 1)));
	            }
                SFdata wData= map.get(w);
                
                if (wData == null) { //if w not in S or F
                	map.put(w, new SFdata(newWdist, f));
                    F.add(w, newWdist);
                } else if (newWdist < wData.distance) {
                	if(w.isHostile()) {
                		hostilesVisited++;
                	}
                    wData.distance= newWdist;
                    wData.backPointer= f;
                    F.updatePriority(w, newWdist);
                }
            }
        }

        // no path from start to end
        return;
    	
    	
    }
    
    /** Travels along the list of nodes, grabbing a speed upgrade if there is
     * one.
     */
    public static void go(List<Node> nodes, ReturnStage state) {
    	//Remove Planet X from the list
    	nodes.remove(state.currentNode());
    	//Travel to each node
    	for(Node n : nodes) {
    		state.moveTo(n);
    		if(n.hasSpeedUpgrade()) {
    			state.grabSpeedUpgrade();
    		}
    	}
    }


    /** Return the path from the start node to node end.
     *  Precondition: nData contains all the necessary information about
     *  the path. */
    public static List<Node> constructPath(Node end, HashMap<Node, SFdata> nData) {
        LinkedList<Node> path= new LinkedList<Node>();
        Node p= end;
        // invariant: All the nodes from p's successor to the end are in
        //            path, in reverse order.
        while (p != null) {
            path.addFirst(p);
            p= nData.get(p).backPointer;
        }
        return path;
    }

    /** Return the sum of the weights of the edges on path path. */
    public static int pathDistance(List<Node> path) {
        if (path.size() == 0) return 0;
        synchronized(path) {
            Iterator<Node> iter= path.iterator();
            Node p= iter.next();  // First node on path
            int s= 0;
            // invariant: s = sum of weights of edges from start to p
            while (iter.hasNext()) {
                Node q= iter.next();
                s= s + p.getConnect(q).length;
                p= q;
            }
            return s;
        }
    }

    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the distance
     *  of this node from the start node. */
    private static class SFdata {
        private Node backPointer; // backpointer on path from start node to this one
        private int distance; // distance from start node to this one

        /** Constructor: an instance with distance d from the start node and
         *  backpointer p.*/
        private SFdata(int d, Node p) {
            distance= d;     // Distance from start node to this one.
            backPointer= p;  // Backpointer on the path (null if start node)
        }

        /** return a representation of this instance. */
        public String toString() {
            return "dist " + distance + ", bckptr " + backPointer;
        }
    }
}
