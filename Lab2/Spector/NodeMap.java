package Spector;

import java.util.HashMap;
import java.util.Map;

// clasa care gestioneaza harta nodurilor si interactiunile
public class NodeMap {
    private static final Log log = LoggerFactory.getLogger(NodeMap.class);
    private HashMap<Integer, Map<Integer, Node>> nodeMap = new HashMap<>();

    // obtine un nod la (x, y). creeaza nodul daca nu exista
    public Node getNode(int x, int y) {
        return getNodeHelper(x, y, true);
    }

    // obtine sau creeaza un nod in functie de 'create'
    private Node getNodeHelper(int x, int y, boolean create) {
        // verifica daca avem deja un map pentru x
        Map<Integer, Node> yMap = nodeMap.getOrDefault(x, new HashMap<>());

        // daca nu avem map pentru x, il adaugam
        if (!nodeMap.containsKey(x)) {
            nodeMap.put(x, yMap);
        }

        // verifica daca avem nodul la y in map-ul pentru x
        Node node = yMap.get(y);
        if (node == null && create) {
            // daca nu exista si trebuie creat, il facem
            log.debug("creem nod nou la (" + x + ", " + y + ")");
            Node newNode = new Node(this, x, y);
            yMap.put(y, newNode);
            return newNode;
        }

        // returneaza nodul existent sau null daca nu trebuie creat
        return node;
    }

    // verifica daca toate nodurile au fost vizitate
    public boolean isComplete() {
        // parcurge toate map-urile si verifica nodurile
        for (Map<Integer, Node> yMap : nodeMap.values()) {
            for (Node node : yMap.values()) {
                if (!node.isVisited()) {
                    return false; // gaseste un nod nevizitat
                }
            }
        }
        return true; // toate nodurile au fost vizitate
    }
}
