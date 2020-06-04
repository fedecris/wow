package wow.movie.tools.subs.analysis;

import java.io.File;
import java.util.Scanner;
import java.util.TreeMap;


public class WordCountInFile {

	// Longitud minima de la palabra a considerar
	public static final int MIN_WORD_LENGTH = 4;
	// Cantidad de ocurrencias minimas a considerar
	public static final int MIN_OCCURRENCE = 1;
	
	// Dada una palabra, cuantas ocurrencias hay 
	protected static final TreeMap<String, Integer> countsByWord = new TreeMap<String, Integer>();
	// Palabras por cantidad de ocurrencia 	
	protected static final TreeMap<Integer, String> countsByQty  = new TreeMap<Integer, String>();
	
	public static void main(String[] args) {
		
		try {
			if (args.length==0) {
				System.out.println("Y el nombre del archivo papá?");
				System.exit(1);
			}
			File file = new File(args[0]);
			
			Scanner scanner = new Scanner(file);
			int i =0;
			while (scanner.hasNextLine()) {
				i++;
				// Parsear la linea
				String line = scanner.nextLine();
				line = simplifyLine(line);
				String[] words = line.split(" ");
				for (String word : words) {
					// Agregarlo si corresponde
					if (word.length() < MIN_WORD_LENGTH || Character.isDigit(word.charAt(0)))
						continue;
					if (countsByWord.get(word)==null)
						countsByWord.put(word, 0);
					countsByWord.put(word, countsByWord.get(word)+1);
				}
			}
			
			System.out.println(i);
						
			// Imprimir cant de cada palabra, calcular max
			int max=-1;
			String maxWord = null; 
			for (String word : countsByWord.keySet()) {
				
			
				// Armar el map de palabras por cantidad
				if (countsByQty.get(countsByWord.get(word)) == null) {
					countsByQty.put(countsByWord.get(word), "");
				}
				countsByQty.put(countsByWord.get(word), countsByQty.get(countsByWord.get(word)) + " " + word);
				
				// Imprimir entrada si hay un minimo razonable
				if (countsByWord.get(word) >= MIN_OCCURRENCE) {
					System.out.println(word + " " + countsByWord.get(word));
				}
				
				// Calcular max
				if (countsByWord.get(word) > max) {
					maxWord = word;
					max = countsByWord.get(word);
				}
			}

			// Imprimir palabras con X cant
			System.out.println("===");
			for (Integer qty : countsByQty.keySet()) {
				if (qty >= MIN_OCCURRENCE) {
					System.out.println(qty + " " + countsByQty.get(qty));
				}
			}
			
			// Imprimir max
			System.out.println("===");
			System.out.println("MAX: " + maxWord + " " + max);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** Simplifica la linea quitandole craceres estramboticos */
	public static String simplifyLine(String line) {
		return line.toLowerCase().replace("|", " ").replace(".", "").replace(",", "").replace("?", "").replace("!", "").replace("-", "").replace("\"", "").replace("'s", " is");
	}
}
