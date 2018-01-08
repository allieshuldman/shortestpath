package student;

import java.util.HashMap;

import models.NodeStatus;
import models.RescueStage;
import models.ReturnStage;
import models.Spaceship;
import student.Paths;

/** An instance implements the methods needed to complete the mission */
public class MySpaceship extends Spaceship {

	/**
	 * Explore the galaxy, trying to find the missing spaceship that has crashed
	 * on Planet X in as little time as possible. Once you find the missing
	 * spaceship, you must return from the function in order to symbolize that
	 * you've rescued it. If you continue to move after finding the spaceship
	 * rather than returning, it will not count. If you return from this
	 * function while not on Planet X, it will count as a failure.
	 * 
	 * At every step, you only know your current planet's ID and the ID of all
	 * neighboring planets, as well as the ping from the missing spaceship.
	 * 
	 * In order to get information about the current state, use functions
	 * currentLocation(), neighbors(), and getPing() in RescueStage. You know
	 * you are standing on Planet X when foundSpaceship() is true.
	 * 
	 * Use function moveTo(long id) in RescueStage to move to a neighboring
	 * planet by its ID. Doing this will change state to reflect your new
	 * position.
	 */
	@Override
	public void rescue(RescueStage state) {
		// TODO : Find the missing spaceship
		
		//HashMap contains the node and whether or not the planet has been visited
		//If the boolean is true, the planet has been visited
		HashMap<NodeStatus, Boolean> map= new HashMap<NodeStatus, Boolean>();
								
		while(!state.foundSpaceship()) {
			//Immediate neighbors of the current node
			Heap<NodeStatus> neighbors = new Heap<NodeStatus>();
										
			//Make a min heap of all the neighbors of the current node
			for(NodeStatus n : state.neighbors()) {
				if(!map.containsKey(n)) {
					map.put(n, false);
				}
				neighbors.add(n,  (1 - n.getPingToTarget()));
			}
										
			//Go to the next node as long as it has not been visited
			NodeStatus next = neighbors.poll();
			while(neighbors.size() >= 1 && map.get(next).booleanValue()) {
				next = neighbors.poll();
			}
			map.put(next, true);
			state.moveTo(next.getId());
		}
	}
	

	/**
	 * Get back to Earth, avoiding hostile troops and searching for speed
	 * upgrades on the way. Traveling through 3 or more planets that are hostile
	 * will prevent you from ever returning to Earth.
	 *
	 * You now have access to the entire underlying graph, which can be accessed
	 * through ReturnStage. currentNode() and getEarth() will return Node objects
	 * of interest, and getNodes() will return a collection of all nodes in the
	 * graph.
	 *
	 * You may use state.grabSpeedUpgrade() to get a speed upgrade if there is
	 * one, and can check whether a planet is hostile using the isHostile
	 * function in the Node class.
	 *
	 * You must return from this function while on Earth. Returning from the
	 * wrong location will be considered a failed run.
	 *
	 * You will always be able to return to Earth without passing through three
	 * hostile planets. However, returning to Earth faster will result in a
	 * better score, so you should look for ways to optimize your return.
	 */
	@Override
	public void returnToEarth(ReturnStage state) {
		// TODO: Return to Earth

		Paths.shortestPathSpace(state.currentNode(), state.getEarth(), state);
		
	}

}