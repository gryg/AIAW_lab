package Spector;

// enum pt directii; folosit pt a defini directia curenta a agentului
public enum Direction {
	NORTH, // nord
	EAST, // est
	SOUTH, // sud
	WEST; // vest

	// roteste agentul la stanga bazat pe directia curenta
	public Direction rotateLeft() {
		switch (this) {
			case NORTH:
				return WEST; // daca e orientat la nord, acum va fi la vest
			case EAST:
				return NORTH; // la est, acum nord
			case SOUTH:
				return EAST; // la sud, acum est
			case WEST:
				return SOUTH; // la vest, acum sud
		}
		throw new IllegalStateException("Directie necunoscuta la rotirea la stanga."); // eroare daca directia nu e
																						// valida
	}

	// roteste agentul la dreapta
	public Direction rotateRight() {
		switch (this) {
			case NORTH:
				return EAST; // nord -> est
			case EAST:
				return SOUTH; // est -> sud
			case SOUTH:
				return WEST; // sud -> vest
			case WEST:
				return NORTH; // vest -> nord
		}
		throw new IllegalStateException("Directie necunoscuta la rotirea la dreapta."); // eroare daca directia nu e
																						// valida
	}
}
