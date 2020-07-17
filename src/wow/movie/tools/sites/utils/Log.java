package wow.movie.tools.sites.utils;

public class Log {

	
	public static void log(StringBuffer container, String content) {
		log(container, content, true);
	}
	
	/** Incluir en el log e imprimir a salida std si corresponde */
	public static void log(StringBuffer container, String content, boolean toConsole) {
		if (container==null) {
			container = new StringBuffer();
		}
		container.append(content);
		if (toConsole) {
			System.out.print(content);
		}
	}
	
}
