package Lab1;

import java.util.*;

public class EightPuzzle {
    // starea finala pe care vrem sa o atingem
    private static final int[] GOAL = { 1, 2, 3, 8, 0, 4, 7, 6, 5 };

    static class PuzzleState implements Comparable<PuzzleState> {
        final int[] board;
        final int zeroIndex; // pozitia spatilui liber
        final int moves; // numarul de mutari
        final PuzzleState parent; // starea anterioara
        final int priority; // prioritate pentru queue, mai mic e mai bine

        PuzzleState(int[] board, int moves, PuzzleState parent) {
            this.board = Arrays.copyOf(board, board.length);
            this.zeroIndex = findZeroIndex(board); // gaseste spatilul liber
            this.moves = moves;
            this.parent = parent;
            this.priority = moves + calculeazaManhattanDistance(); // calculeaza cost total
        }

        // gaseste spatilul liber, daca nu e, e o problema
        private int findZeroIndex(int[] board) {
            for (int i = 0; i < board.length; i++) {
                if (board[i] == 0) {
                    return i;
                }
            }
            throw new IllegalArgumentException("No zero found in board");
        }

        // distanta Manhattan: suma distantelor fiecarui patratel fata de unde ar trebui
        // sa fie
        private int calculeazaManhattanDistance() {
            int distance = 0;
            for (int i = 0; i < board.length; i++) {
                int val = board[i];
                if (val != 0) { // ignora spatilul liber
                    int targetX = (val - 1) % 3;
                    int targetY = (val - 1) / 3;
                    int x = i % 3;
                    int y = i / 3;
                    distance += Math.abs(x - targetX) + Math.abs(y - targetY);
                }
            }
            return distance;
        }

        // verifica daca am atins scopul
        boolean isGoal() {
            return Arrays.equals(board, GOAL);
        }

        // genereaza urmatoarele mutari posibile
        List<PuzzleState> genereazaUrmatorii() {
            List<PuzzleState> successors = new ArrayList<>();
            int[] dx = { -1, 1, 0, 0 }; // Sus, Jos, Stanga, Dreapta
            int[] dy = { 0, 0, -1, 1 };

            for (int i = 0; i < 4; i++) {
                int newX = zeroIndex % 3 + dx[i];
                int newY = zeroIndex / 3 + dy[i];
                int newIndex = newY * 3 + newX;

                // verifica daca mutarea e valida
                if (newX >= 0 && newX < 3 && newY >= 0 && newY < 3) {
                    int[] newBoard = Arrays.copyOf(board, board.length);
                    newBoard[zeroIndex] = newBoard[newIndex]; // interschimba cu spatilul liber
                    newBoard[newIndex] = 0;
                    successors.add(new PuzzleState(newBoard, moves + 1, this)); // creaza o noua stare cu o mutare in
                                                                                // plus
                }
            }
            return successors; // toate starile posibile urmatoare
        }

        // pentru coada de prioritati sa decida ce stare sa exploreze mai intai
        @Override
        public int compareTo(PuzzleState other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    // metoda principala de rezolvare, foloseste algoritmul A*
    public static List<PuzzleState> rezolva(int[] start) {
        Queue<PuzzleState> openSet = new PriorityQueue<>(); // starile de explorat
        Set<String> closedSet = new HashSet<>(); // urmarim ce am vazut deja
        openSet.add(new PuzzleState(start, 0, null)); // incepem cu starea initiala

        // bucla principala, continuam pana gasim solutia sau pana ramanem fara stari
        while (!openSet.isEmpty()) {
            PuzzleState current = openSet.poll(); // ia starea cu prioritatea cea mai mica

            // am ajuns la scop? daca da, reconstruieste drumul parcurs
            if (current.isGoal()) {
                return reconstruiesteDrumul(current);
            }

            // marcheaza starea curenta ca vazuta
            closedSet.add(Arrays.toString(current.board));

            // exploreaza vecinii, dar numai daca nu i-am vazut deja
            for (PuzzleState neighbor : current.genereazaUrmatorii()) {
                if (!closedSet.contains(Arrays.toString(neighbor.board))) {
                    openSet.add(neighbor);
                }
            }
        }
        return Collections.emptyList(); // nu s-a gasit solutia
    }

    // reconstruieste drumul de la starea finala la cea initiala
    private static List<PuzzleState> reconstruiesteDrumul(PuzzleState goalState) {
        LinkedList<PuzzleState> path = new LinkedList<>();
        PuzzleState current = goalState;
        while (current != null) {
            path.addFirst(current);
            current = current.parent;
        }
        return path;
    }

    public static void main(String[] args) {
        // seteaza starea initiala si incearca sa o rezolve
        int[] start = { 2, 8, 3, 1, 6, 4, 7, 5, 0 }; // configuratia initiala a puzzle-ului
        List<PuzzleState> solution = rezolva(start);

        // daca avem o solutie, o afisam
        if (solution.isEmpty()) {
            System.out.println("Nu s-a gasit solutia.");
        } else {
            for (PuzzleState state : solution) {
                System.out.println(Arrays.toString(state.board));
            }
        }
    }
}