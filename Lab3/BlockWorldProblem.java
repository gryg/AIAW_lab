package Lab3;

import java.util.*;

public class BlockWorldProblem {
        public static void main(String[] args) {
                // definirea starii initiale a problemei
                Set<String> initialStatePredicates = new HashSet<>(Arrays.asList(
                                "ON B TABLE", "ON C A", "ON A TABLE", "CLEAR B", "CLEAR C", "ARM EMPTY"));
                // definirea starii finale dorite
                Set<String> goalStatePredicates = new HashSet<>(Arrays.asList(
                                "ON A B", "ON B C", "ON C TABLE", "CLEAR A", "CLEAR C", "ARM EMPTY"));
                // crearea listei de actiuni posibile
                List<Action> actions = Arrays.asList(
                                // definirea fiecarei actiuni cu preconditiile, efectele adaugate si efectele
                                // sterse
                                new Action("UNSTACK C from A", Arrays.asList("ON C A", "CLEAR C", "ARM EMPTY"),
                                                Arrays.asList("CLEAR A", "ARM HOLDING C"), Arrays.asList("ON C A")),
                                new Action("PUTDOWN C on TABLE", Arrays.asList("ARM HOLDING C"),
                                                Arrays.asList("ON C TABLE", "CLEAR C", "ARM EMPTY"),
                                                Arrays.asList("ARM HOLDING C")),
                                new Action("UNSTACK B from TABLE", Arrays.asList("ON B TABLE", "CLEAR B", "ARM EMPTY"),
                                                Arrays.asList("ARM HOLDING B"), Arrays.asList("ON B TABLE")),
                                new Action("STACK B on C", Arrays.asList("ARM HOLDING B", "CLEAR C"),
                                                Arrays.asList("ON B C", "CLEAR B", "ARM EMPTY"),
                                                Arrays.asList("ARM HOLDING B", "CLEAR C")),
                                new Action("UNSTACK A from TABLE", Arrays.asList("ON A TABLE", "CLEAR A", "ARM EMPTY"),
                                                Arrays.asList("ARM HOLDING A"), Arrays.asList("ON A TABLE")),
                                new Action("STACK A on B", Arrays.asList("ARM HOLDING A", "CLEAR B"),
                                                Arrays.asList("ON A B", "CLEAR A", "ARM EMPTY"),
                                                Arrays.asList("ARM HOLDING A", "CLEAR B")));
                // initializarea starii initiale si a starii finale
                State initialState = new State(initialStatePredicates);
                State goalState = new State(goalStatePredicates);
                // crearea planificatorului cu actiunile, starea initiala si starea finala
                POP pop = new POP(actions, initialState, goalState);
                // incercarea de a gasi o solutie
                try {
                        Plan solutionPlan = pop.solve(); // rezolvarea
                        System.out.println("Solution found with the following actions:");
                        // afisarea actiunilor din planul gasit
                        for (Action action : solutionPlan.getActions()) {
                                System.out.println(action.name);
                        }
                } catch (IllegalStateException e) {
                        // gestionarea cazului in care nu se poate gasi o solutie
                        System.out.println(e.getMessage());
                }
        }
}
