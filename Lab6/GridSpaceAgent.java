package Lab6;

import java.util.*;

public class GridSpaceAgent {
    private static final int NORTH = 0;
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int WEST = 3;
    private int[][] grid;
    private int x;
    private int y;
    private int direction;

    // constructorul agentului, seteaza pozitia initiala si directia
    public GridSpaceAgent(int startX, int startY, int[][] gridSpace) {
        x = startX;
        y = startY;
        grid = gridSpace;
        direction = NORTH; // directia initiala e spre nord
    }

    public void move() {
        // bucla infinita, agentul se misca continuu
        while (true) {
            if (canMove()) {
                takeStep();
            } else {
                turnRight(); // daca agentul nu poate merge inainte, o ia la dreapta
            }
            // logica de a pastra mereu limita la stanga
            turnLeft();
            if (canMove()) {
                takeStep();
            } else {
                turnRight(); // daca agentul nu poate merge la stanga, isi reorienteaza directia initiala
            }
            // Note: aici e bucla infinita, agentul va continua sa se miste pana cand
            // programul e oprit
        }
    }

    private boolean canMove() {
        // verifica daca celula urmatoare in directia curenta e libera
        switch (direction) {
            case NORTH:
                return isFree(x - 1, y);
            case EAST:
                return isFree(x, y + 1);
            case SOUTH:
                return isFree(x + 1, y);
            case WEST:
                return isFree(x, y - 1);
        }
        return false;
    }

    private void takeStep() {
        // executa miscarea bazata pe directia curenta
        switch (direction) {
            case NORTH:
                x--;
                break;
            case EAST:
                y++;
                break;
            case SOUTH:
                x++;
                break;
            case WEST:
                y--;
                break;
        }
    }

    private void turnRight() {
        // schimba directia in sensul acelor de ceasornic
        direction = (direction + 1) % 4;
    }

    private void turnLeft() {
        // schimba directia in sens invers acelor de ceasornic
        direction = (direction + 3) % 4;
    }

    private boolean isFree(int nextX, int nextY) {
        // verifica daca celula e in interiorul grilei si nu e limita
        return nextX >= 0 && nextX < grid.length && nextY >= 0 && nextY < grid[nextX].length && grid[nextX][nextY] == 0;
    }

    public static void main(String[] args) {
        // 0 reprezinta celule libere, 1 reprezinta limite sau obstacole
        int[][] gridSpace = {
                { 0, 1, 0, 0, 0, 0 },
                { 0, 1, 0, 1, 1, 1 },
                { 0, 1, 0, 1, 0, 0 },
                { 0, 0, 0, 1, 0, 0 },
                { 1, 1, 1, 1, 0, 1 },
                { 0, 0, 0, 0, 0, 1 }
        };

        // pozitia initiala a agentului
        GridSpaceAgent agent = new GridSpaceAgent(2, 0, gridSpace);
        agent.move(); // nota: agentul incepe sa se miste intr-o bucla infinita aici
    }
}
