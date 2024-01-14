package Lab4;

import java.util.*;

class WumpusWorldAgent {
    private Random rand = new Random(); 
    private static final int GRID_SIZE = 4;
    private static final char PIT = 'P';
    private static final char GOLD = 'G';
    private static final char WUMPUS = 'W';
    private static final char STENCH = 'S';
    private static final char BREEZE = 'B';
    private static final char EMPTY = '.';

    private char[][] world;
    private AgentPosition agentPosition;
    private Direction direction;
    private boolean hasArrow;
    private boolean hasGold;
    private Set<String> visited;
    private Stack<Action> actionStack;

    private enum Direction {
        UP, RIGHT, DOWN, LEFT
    }

    private class AgentPosition {
        int x, y;

        AgentPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class Action {
        int x, y;
        Direction direction;

        Action(int x, int y, Direction direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }

    public WumpusWorldAgent() {
        world = new char[GRID_SIZE][GRID_SIZE];
        visited = new HashSet<>();
        actionStack = new Stack<>();
        agentPosition = new AgentPosition(0, 0);
        direction = Direction.RIGHT;
        hasArrow = true;
        hasGold = false;
        initializeWorld();
        placeBreezesAndStenches();
    }

    private void initializeWorld() {
        // Initialize the world with empty cells
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                world[i][j] = EMPTY;
            }
        }
        Random rand = new Random();

        // plaseaza pits, wumpus, gold, sanse sunt 20%, 5%, 5%
        placeRandomly(PIT, 0.2); 
        placeRandomly(WUMPUS, 0.05); 
        placeRandomly(GOLD, 0.05); 

        // starting square si adiacentele sunt safe
        world[0][0] = EMPTY;
        makeAdjacentSafe(0, 0);

        visited.add(agentPosition.x + "-" + agentPosition.y);
    }

    private void placeRandomly(char item, double probability) {
    int attempts = 0;
    while (attempts < 100) {
        int i = rand.nextInt(GRID_SIZE);
        int j = rand.nextInt(GRID_SIZE);
        if (world[i][j] == EMPTY) {
            world[i][j] = item;
            if (item == WUMPUS || item == GOLD) {
                // asigura ca exista un gold si un wumpus
                break;
            }
        }
        attempts++;
    }
}

    private void makeAdjacentSafe(int x, int y) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx, ny = y + dy;
                if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE) {
                    if (world[nx][ny] != GOLD && world[nx][ny] != WUMPUS) {
                        world[nx][ny] = EMPTY;
                    }
                }
            }
        }
    }

    private void placeBreezesAndStenches() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (world[i][j] == PIT) {
                    addPerceptToAdjacent(i, j, BREEZE);
                } else if (world[i][j] == WUMPUS) {
                    addPerceptToAdjacent(i, j, STENCH);
                }
            }
        }
    }

    private void addPerceptToAdjacent(int x, int y, char percept) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (Math.abs(dx) + Math.abs(dy) == 1) {
                    int nx = x + dx, ny = y + dy;
                    if (nx >= 0 && nx < GRID_SIZE && ny >= 0 && ny < GRID_SIZE && world[nx][ny] == '.') {
                        world[nx][ny] = percept;
                    }
                }
            }
        }
    }

    private boolean isSafe(int x, int y) {
        if (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE) {
            return world[x][y] != PIT && !visited.contains(x + "-" + y);
        }
        return false;
    }

    private void moveForward() {
        int newX = agentPosition.x;
        int newY = agentPosition.y;

        switch (direction) {
            case UP:
                newX--;
                break;
            case RIGHT:
                newY++;
                break;
            case DOWN:
                newX++;
                break;
            case LEFT:
                newY--;
                break;
        }

        if (isSafe(newX, newY)) {
            agentPosition.x = newX;
            agentPosition.y = newY;
            visited.add(agentPosition.x + "-" + agentPosition.y);
            actionStack.push(new Action(agentPosition.x, agentPosition.y, direction));
        }
    }

    private void turnLeft() {
        direction = Direction.values()[(direction.ordinal() + 3) % 4];
        actionStack.push(new Action(agentPosition.x, agentPosition.y, direction));
    }

    private void turnRight() {
        direction = Direction.values()[(direction.ordinal() + 1) % 4];
        actionStack.push(new Action(agentPosition.x, agentPosition.y, direction));
    }

    private void grabGold() {
        if (world[agentPosition.x][agentPosition.y] == GOLD) {
            hasGold = true;
            world[agentPosition.x][agentPosition.y] = '.';
            System.out.println("Agent grabs the gold!");
        }
    }

    private void shootArrow() {
        if (!hasArrow)
            return;

        hasArrow = false;
        int arrowX = agentPosition.x;
        int arrowY = agentPosition.y;

        while (arrowX >= 0 && arrowX < GRID_SIZE && arrowY >= 0 && arrowY < GRID_SIZE) {
            if (world[arrowX][arrowY] == WUMPUS) {
                System.out.println("Agent shoots the Wumpus!");
                world[arrowX][arrowY] = '.';
                break;
            }

            switch (direction) {
                case UP:
                    arrowX--;
                    break;
                case RIGHT:
                    arrowY++;
                    break;
                case DOWN:
                    arrowX++;
                    break;
                case LEFT:
                    arrowY--;
                    break;
            }
        }
    }

    private boolean canClimb() {
        return agentPosition.x == 0 && agentPosition.y == 0 && hasGold;
    }

    private void backtrack() {
        if (!actionStack.isEmpty()) {
            Action lastAction = actionStack.pop();
            agentPosition.x = lastAction.x;
            agentPosition.y = lastAction.y;
            direction = lastAction.direction;
        }
    }

    private void visualizeWorld() {
        System.out.println("Current World (Move " + actionStack.size() + "):");
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (i == agentPosition.x && j == agentPosition.y) {
                    System.out.print("A ");
                } else {
                    System.out.print(world[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void makeDecision() {
        if (world[agentPosition.x][agentPosition.y] == GOLD) {
            grabGold();
        } else if (world[agentPosition.x][agentPosition.y] == STENCH && hasArrow) {
            shootArrow();
        } else {
            chooseAction();
        }
    }

    private void chooseAction() {
        if (canMoveForward()) {
            moveForward();
        } else {
            if (!tryTurning()) {
                backtrack();
            }
        }
    }

    private boolean canMoveForward() {
        int newX = agentPosition.x;
        int newY = agentPosition.y;

        switch (direction) {
            case UP:
                newX--;
                break;
            case RIGHT:
                newY++;
                break;
            case DOWN:
                newX++;
                break;
            case LEFT:
                newY--;
                break;
        }

        return isSafe(newX, newY);
    }

    private boolean tryTurning() {
        Direction originalDirection = direction;
        turnLeft();
        if (canMoveForward()) {
            return true;
        }

        direction = originalDirection; // Reset la directia initiala
        turnRight();
        return canMoveForward();
    }

    public void runAgent() {
        while (true) {
            visualizeWorld();
            makeDecision();

            if (canClimb()) {
                System.out.println("Agent climbs out of the cave with the gold!");
                break;
            }

            if (world[agentPosition.x][agentPosition.y] == WUMPUS) {
                System.out.println("Agent is eaten by the Wumpus!");
                break;
            }

            if (world[agentPosition.x][agentPosition.y] == PIT) {
                System.out.println("Agent falls into a pit and dies!");
                break;
            }
        }
    }

    public static void main(String[] args) {
        WumpusWorldAgent agent = new WumpusWorldAgent();
        agent.runAgent();
    }
}