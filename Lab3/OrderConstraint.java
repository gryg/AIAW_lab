package Lab3;

public class OrderConstraint {
    Action before;
    Action after;

    public OrderConstraint(Action before, Action after) {
        this.before = before;
        this.after = after;
    }

}
