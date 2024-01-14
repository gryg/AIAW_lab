package Lab3;

import java.util.*;

// clasa planificator pentru lumea blocurilor
public class POP {
    private List<Action> actions; // lista de actiuni disponibile
    private State initialState; // starea initiala
    private State goalState; // starea scop
    private Plan plan; // planul care va fi construit

    // constructor pentru initializarea planificatorului
    public POP(List<Action> actions, State initialState, State goalState) {
        this.actions = actions;
        this.initialState = initialState;
        this.goalState = goalState;
        this.plan = new Plan();
    }

    // metoda pentru generarea planului de rezolvare
    public Plan solve() {
        State currentState = new State(initialState.predicates); // copie a starii initiale
        Set<String> goals = new HashSet<>(goalState.predicates); // obiectivele de atins

        // bucla pentru construirea planului
        while (!goals.isEmpty()) {
            boolean progress = false;
            for (String goal : new ArrayList<>(goals)) {
                for (Action action : actions) {
                    // verifica daca actiunea curenta conduce spre atingerea unui obiectiv
                    if (action.addEffects.contains(goal) && action.applicable(currentState)) {
                        plan.addAction(action); // adauga actiunea la plan
                        action.apply(currentState); // aplica actiunea
                        goals.remove(goal); // indeparteaza obiectivul atins
                        progress = true;
                        break;
                    }
                }
            }
            if (!progress) {
                throw new IllegalStateException("No further actions can satisfy the remaining goals.");
            }
        }
        return plan; // returneaza planul final
    }
}
