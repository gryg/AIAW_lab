package Lab3;

public class CausalLink {
    Action from;
    String condition;
    Action to;

    public CausalLink(Action from, String condition, Action to) {
        this.from = from;
        this.condition = condition;
        this.to = to;
    }
}
