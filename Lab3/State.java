package Lab3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class State {
    Set<String> predicates;

    public State(Set<String> predicates) {
        this.predicates = new HashSet<>(predicates);
    }

    public boolean satisfies(List<String> preconditions) {
        return predicates.containsAll(preconditions);
    }

    public void applyAction(Action action) {
        predicates.removeAll(action.deleteEffects);
        predicates.addAll(action.addEffects);
    }
}
