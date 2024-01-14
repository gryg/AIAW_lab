package Lab3;

import java.util.List;

public class Action {
    String name;
    List<String> preconditions;
    List<String> addEffects;
    List<String> deleteEffects;

    public Action(String name, List<String> preconditions, List<String> addEffects, List<String> deleteEffects) {
        this.name = name;
        this.preconditions = preconditions;
        this.addEffects = addEffects;
        this.deleteEffects = deleteEffects;
    }

    public boolean applicable(State state) {
        return state.satisfies(preconditions);
    }

    public void apply(State state) {
        state.applyAction(this);
    }
}
