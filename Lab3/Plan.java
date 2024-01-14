package Lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// clasa pentru gestionarea planului de actiuni
public class Plan {
    private List<Action> actions; // lista de actiuni ale planului
    private List<CausalLink> causalLinks; // legaturi cauzale intre actiuni
    private List<OrderConstraint> orderings; // constrangeri de ordine pentru actiuni

    // constructor pentru initializarea listelor
    public Plan() {
        actions = new ArrayList<>();
        causalLinks = new ArrayList<>();
        orderings = new ArrayList<>();
    }

    // adauga o actiune la plan si actualizeaza ordinea si legaturile cauzale
    public void addAction(Action action) {
        actions.add(action);
        updateOrderingsAndCausalLinks(action);
        resolveThreats();
    }

    // actualizeaza ordinea si legaturile cauzale pentru o actiune noua
    private void updateOrderingsAndCausalLinks(Action newAction) {
        for (String precondition : newAction.preconditions) {
            for (Action action : actions) {
                if (action.addEffects.contains(precondition)) {
                    addCausalLink(action, precondition, newAction);
                    addOrdering(action, newAction);
                }
            }
        }
    }

    // verifica daca o preconditie este satisfacuta de actiunile existente
    public boolean isSatisfied(String precondition) {
        return actions.stream().anyMatch(action -> action.addEffects.contains(precondition));
    }

    // adauga o legatura cauzala intre actiuni
    public void addCausalLink(Action from, String precondition, Action to) {
        causalLinks.add(new CausalLink(from, precondition, to));
    }

    // adauga o constrangere de ordine intre doua actiuni
    public void addOrdering(Action before, Action after) {
        if (before != after && !orderings.contains(new OrderConstraint(before, after))) {
            orderings.add(new OrderConstraint(before, after));
        }
    }

    // verifica validitatea planului (legaturi cauzale si constrangeri de ordine)
    public boolean isValid() {
        return checkCausalLinks() && checkOrderingConstraints();
    }

    // verifica daca legaturile cauzale sunt valide
    private boolean checkCausalLinks() {
        for (CausalLink link : causalLinks) {
            if (!actions.contains(link.to) || !actions.contains(link.from)) {
                return false;
            }
        }
        return true;
    }

    // verifica daca constrangerile de ordine sunt valide
    private boolean checkOrderingConstraints() {
        for (OrderConstraint oc : orderings) {
            if (orderings.contains(new OrderConstraint(oc.after, oc.before))) {
                return false;
            }
        }
        return true;
    }

    // rezolva amenintarile la adresa legaturilor cauzale
    private void resolveThreats() {
        for (CausalLink link : causalLinks) {
            for (Action action : actions) {
                // verifica daca actiunea curenta afecteaza legatura cauzala
                if (action != link.from && action != link.to && action.deleteEffects.contains(link.condition)) {
                    // reordoneaza actiunile pentru a evita conflictul
                    if (!orderings.contains(new OrderConstraint(link.from, action))) {
                        addOrdering(link.from, action);
                    }
                    if (!orderings.contains(new OrderConstraint(action, link.to))) {
                        addOrdering(action, link.to);
                    }
                }
            }
        }
    }

    // returneaza o copie a listei de actiuni
    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }
}