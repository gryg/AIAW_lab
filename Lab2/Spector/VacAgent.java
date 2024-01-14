package Spector;

import java.util.ArrayList;
import java.util.List;

// importam clasele necesare
import vacworld.*;
import agent.*;

public class VacAgent extends Agent {
	// logger pentru debug
	private static final Log log = LoggerFactory.getLogger(VacAgent.class);
	private NodeMap nodeMap;
	private VacPercept currentPercept;
	private Node currentNode;
	private Direction facing;

	// constructor default
	public VacAgent() {
		this(new NodeMap());
	}

	// constructor cu harta
	public VacAgent(NodeMap map) {
		nodeMap = map;

		// incepem in (0,0), orientati spre nord
		currentNode = nodeMap.getNode(0, 0);
		currentNode.setType(NodeType.OPEN);
		currentNode.setVisited(true);
		facing = Direction.NORTH;
	}

	// metoda pentru a procesa perceptii
	@Override
	public void see(Percept p) {
		currentPercept = (VacPercept) p;
	}

	// selectam actiunea agentului
	@Override
	public Action selectAction() {
		// verifica daca in fata e liber
		Node nodeInFront = currentNode.getNode(facing);
		if (currentPercept.seeObstacle()) {
			log.debug("detectat solid in fata");
			nodeInFront.setType(NodeType.SOLID);
		} else {
			log.debug("detectat liber in fata");
			nodeInFront.setType(NodeType.OPEN);
		}

		// verifica murdaria
		if (currentPercept.seeDirt()) {
			currentNode.setDirty(true);
		}

		Action nextAction = determineAction(currentNode);

		// update dupa actiune
		updateStateAfterAction(nextAction);

		return nextAction;
	}

	// alege actiunea pe baza nodului curent
	private Action determineAction(Node node) {
		if (nodeMap.isComplete()) {
			return new ShutOff();
		}

		// prioritate pentru aspirat
		if (node.isDirty()) {
			return new SuckDirt();
		}

		// incearca sa mearga inainte
		Node forwardNode = node.getNode(facing);
		if (forwardNode.isOpen() && !forwardNode.isVisited()) {
			log.debug("merge inainte, e liber si nevizitat");
			return new GoForward();
		}

		// determina directia urmatoare
		RelativeDirection direction = decideNextDirection(node);
		if (direction != null) {
			return direction == RelativeDirection.LEFT ? new TurnLeft() : new TurnRight();
		}

		// gaseste nodul nevizitat cel mai apropiat
		Node closestNode = findClosestUnvisitedNode(node);
		if (closestNode != null) {
			log.debug("nodul cel mai apropiat: " + closestNode);
			return chooseDirectionBasedOnNode(closestNode);
		}

		log.debug("indrumare nedeterminata, intoarce-te la stanga");
		return new TurnLeft();
	}

	// actualizeaza starea dupa actiune
	private void updateStateAfterAction(Action action) {
		if (action instanceof SuckDirt) {
			currentNode.setDirty(false);
		} else if (action instanceof GoForward) {
			currentNode.setVisited(true);
			currentNode = currentNode.getNode(facing);
			if (!currentNode.isOpen()) {
				throw new IllegalStateException("Incercare de miscare in locatie inaccesibila!");
			}
		} else if (action instanceof TurnLeft) {
			facing = facing.rotateLeft();
		} else if (action instanceof TurnRight) {
			facing = facing.rotateRight();
		}
	}

	// decide directia urmatoare
	private RelativeDirection decideNextDirection(Node node) {
		Direction left = facing.rotateLeft();
		Node nodeLeft = node.getNode(left);
		if (nodeLeft.isUnknown()) {
			log.debug("intoarce-te la stanga, e necunoscut");
			return RelativeDirection.LEFT;
		}

		Direction right = facing.rotateRight();
		Node nodeRight = node.getNode(right);
		if (nodeRight.isUnknown()) {
			log.debug("intoarce-te la dreapta, e necunoscut");
			return RelativeDirection.RIGHT;
		}

		return null;
	}

	// gaseste nodul nevizitat cel mai apropiat
	private Node findClosestUnvisitedNode(Node node) {
		List<WeightedNode> potentialNodes = new ArrayList<>();

		addPotentialNode(potentialNodes, node, facing);
		addPotentialNode(potentialNodes, node, facing.rotateLeft());
		addPotentialNode(potentialNodes, node, facing.rotateRight());

		return getClosestNode(potentialNodes);
	}

	// adauga nod posibil
	private void addPotentialNode(List<WeightedNode> nodes, Node current, Direction direction) {
		WeightedNode potentialNode = findPotentialNode(current, direction);
		if (potentialNode != null) {
			nodes.add(potentialNode);
		}
	}

	// determina nodul cel mai apropiat
	private Node getClosestNode(List<WeightedNode> nodes) {
		WeightedNode closest = null;
		for (WeightedNode node : nodes) {
			if (closest == null || node.weight < closest.weight) {
				closest = node;
			}
		}

		return closest != null ? closest.node : null;
	}

	// gaseste nodul potential
	private WeightedNode findPotentialNode(Node current, Direction direction) {
		int dist = 1;
		Node nextNode = current.getNode(direction);
		while (nextNode != null) {
			if (nextNode.isSolid()) {
				return null;
			} else if (nextNode.isUnknown() || !nextNode.isVisited()) {
				return new WeightedNode(dist, nextNode);
			}
			dist++;
			nextNode = nextNode.getNode(direction);
		}
		return null;
	}

	// alege directia pe baza nodului
	private Action chooseDirectionBasedOnNode(Node node) {
		Direction directionToTurn = null;
		if (node.getY() > currentNode.getY()) {
			directionToTurn = Direction.NORTH;
		} else if (node.getX() > currentNode.getX()) {
			directionToTurn = Direction.EAST;
		} else if (node.getX() < currentNode.getX()) {
			directionToTurn = Direction.WEST;
		}

		if (facing == directionToTurn) {
			return new GoForward();
		} else {
			return determineTurnAction(directionToTurn);
		}
	}

	// determina actiunea de intoarcere
	private Action determineTurnAction(Direction direction) {
		if (direction == null) {
			return new TurnLeft(); // intoarcere implicita la stanga daca directia nu e specificata
		}

		switch (direction) {
			case NORTH:
				return facing == Direction.WEST ? new TurnRight() : new TurnLeft();
			case EAST:
				return facing == Direction.NORTH ? new TurnRight() : new TurnLeft();
			case WEST:
				return facing == Direction.NORTH ? new TurnLeft() : new TurnRight();
			default:
				return new TurnLeft();
		}
	}

	// clasa interna pentru noduri cu greutate
	private static class WeightedNode {
		public int weight;
		public Node node;

		public WeightedNode(int weight, Node node) {
			this.weight = weight;
			this.node

					= node;
		}
	}

	// metoda pentru returnarea id-ului agentului
	@Override
	public String getId() {
		return "Spector" + Math.random();
	}
}
