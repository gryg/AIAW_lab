package Spector;

// fabrica de logger, creeaza instante de log
public class LoggerFactory {
	// primeste o clasa si creeaza un logger pt ea
	public static Log getLogger(Class<?> clazz) {
		return new Log() {
			@Override
			public void debug(String message) {
				System.out.println(message); // afiseaza mesajul in consola
			}
		};
	}
}
