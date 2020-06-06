package wow.movie.tools.subs.analysis;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * 
 * Genera un video con las ocurrencias de la palabra buscada, basandose en el srt especificado, generando recortes del video especificado
 * (ver argumentos esperados)
 * 
 * Formato de referencia:
 * 
 * 20
 * 00:03:03,814 --> 00:03:07,820
 * Once an idea has taken hold of the brain
 * it's almost impossible to eradicate. 
 * 
 */

public class WordFFMpegScriptGenerator {

	/** === ARGUMENTOS A RECIBIR DESDE LA TERMINAL === */	
	/** 0) Path de los archivos */
	protected static String path = "";
	/** 1) Nombre del archivo de video */
	protected static String videoFile = "";
	/** 2) Nombre del archivo de subtitulo */
	protected static String subsFile = "";
	/** 3) Palabra a buscar */
	protected static String criteria = "";
	/** 4) Segundos "hacia atras" adicionales para conseguir un keyframe */
	protected static int secondsBack = 20; // NO PUEDE SER MAYOR A 59 SEGUNDOS
	/** 5) estos milisegundos al inicio ayudan a recuperar casos donde el subtitulo esta desfasado */
	protected static int extraStartMS = 500;
	/** 6) estos milisegundos al final ayudan a recuperar casos donde el subtitulo esta desfasado */
	protected static int extraEndMS = 100;
	/** 7) Distancia Y de los textos a imprimir en pantalla  */
	protected static int yDistance = 50;
	
	
	/** === ESTOS NO SERIA NECESARIO TOCAR DEMASAIADO === */	
	/** Nombre del script a generar */
	protected static String SCRIPT_FILENAME = "generator.sh";
	/** Nombre de la lista a generar */
	protected static String LIST_FILENAME = "list.txt";
	/** Ubicacion de la tipografia */
	protected static String FONT_FILE = "/home/usuario/.local/share/fonts/JosefinSans-Regular.ttf";
	/** Formato del arthivo srt: Nro de subtitulo */
	protected static int SRT_NUMBER = 0;
	/** Formato del arthivo srt: Timestamp */
	protected static int SRT_TIMESTAMP = 1;
	/** Formato del arthivo srt: Primer linea de texto */
	protected static int SRT_FIRST_TEXT = 2;
	/** Leading zeroes en numero convertido a texto */
	protected static int LEADING_ZEROES = 3;
	
	public static void main(String[] args) {

		// Cadena con el contenido del script .sh
		StringBuffer scriptContent = new StringBuffer("");
		// Cadena con la lista de archivitos mp4 a concatenar
		StringBuffer listContent = new StringBuffer("");
		// Almacena la ultima linea con el tiempo del subtitulo leida
		String intervalLine = "";
		try {
			if (args.length<4) {
				System.out.println("Y los argumentos?");
				System.exit(1);
			}
			
			// Asignar argumentos a variables
			path = args[0];
			videoFile = args[1];
			subsFile = args[2];
			criteria = args[3];
			if (args.length>4)
				secondsBack = Integer.parseInt(args[4]);
			if (args.length>5)
				extraStartMS = Integer.parseInt(args[5]);
			if (args.length>5)
				extraStartMS = Integer.parseInt(args[5]);
			if (args.length>6)
				extraEndMS = Integer.parseInt(args[6]);
			if (args.length>7)
				yDistance = Integer.parseInt(args[7]);			
			
			scriptContent.append("# Auto generated. " + new Date() + "\n");
			scriptContent.append("# Arguments: " + path + " " + videoFile + " " + subsFile + " " + criteria + "\n");
			scriptContent.append("# Arguments: " + "secondsBack="+secondsBack + " " +"extraStartMS="+extraStartMS + " " + "extraEndMS="+extraEndMS + "yDistance=" + yDistance + "\n");
			scriptContent.append("\n");
			
			
			File subtitulo = new File(path + "/" + subsFile);
			Scanner scanner = new Scanner(subtitulo);
			// Recorrer las lineas
			int fileNo=1;
			int occurrences = 0;
			ArrayList<String> chunkLines = new ArrayList<String>();
			while (scanner.hasNextLine()) {
				
				// Leer la linea la linea. Se supone que los subtitulos son separados por una linea en blanco
				String line = scanner.nextLine();
				
				// Ir agregando las lineas correspondientes a un bloque de subtitulo hasta llegar a un espacio en blanco 
				if (line.length()>0) {
					chunkLines.add(line);
					continue;
				} else {
					// Tomar del chunk la información relevante (tiempo y contenido)
					if (chunkLines.size()<2) {
						System.out.println("WARNING! Chunk mal formado cerca de " + chunkLines.get(chunkLines.size()-1));
						continue;
					}									
					intervalLine = chunkLines.get(SRT_TIMESTAMP).replace(",", ".");  // ffmpeg require punto para los milisegundos 
					line = getTextLine(chunkLines);					
					// Reiniciar chunkLines 
					chunkLines = new ArrayList<String>();
				}
			
				// Guardarme una copia de la linea sin modificaciones
				String pristineLine = line.trim();
				line = WordCountInFile.simplifyLine(line);
				// Tenemos match?
				if (line.contains(criteria.toLowerCase())) {
					// Se deben contar la cantidad de palabras en la frase que coinciden con el criterio (quizas hay mas de una )
					int cant = matchesInLine(line);
					occurrences+=cant;			
					
					// Imprimir lo que vamos a generando en el archivo
					System.out.println(intervalLine + " " + "\t (File " + printableNumber(fileNo) + ", " + cant + " matches - " + printableNumber(occurrences) + " total) \t" + pristineLine);
				 			
					// Definir el intervalo a crear.  posicion 0 es desde y posicion 1 es hasta
					String[] interval = defineStartEnd(intervalLine);

					// Subarchivo de video a generar
					String outputFileName = getVideoFileName(fileNo++);
					
					// Pal archivo con los cachos a concatenar 
					listContent.append("file ").append(outputFileName).append("\n");

					// Pal archivo .sh
					scriptContent.append("# File " + (fileNo-1) + ", matches count " + occurrences + ": " + intervalLine + " - " + pristineLine + "\n");
					
					// Copiar una parte, considerando antelacion para conseguir un keyframe
					scriptContent.append("ffmpeg -y -i " + videoFile + " -ss " + interval[0] + " -to " + interval[1] + " -vcodec copy -acodec copy tempo.mp4 \n");

					// Procesar solo el intervalo del subtitulo (mas adecuaciones), incluyendo el numero de ocurrencia
					scriptContent.append(	"ffmpeg -y -i tempo.mp4 " +
										" -vf drawtext=\"" + FONT_FILE + ": text='" + capitalize(criteria) + " count\\: "+ occurrences +"': " + 
										drawText("white", 100, 20, "50", ""+yDistance) + "\" " + 
										"-ss 00:00:" + secondsBack + " tempo2.mp4 \n");

					// Procesar incorporando el subtitulo					
					scriptContent.append(	"ffmpeg -i tempo2.mp4 " +
										" -vf drawtext=\"" + FONT_FILE + ": text=\\\"" + escapeLine(pristineLine) + "\\\": " +
										drawText("white", 50, 10, "50", "(h-text_h-"+yDistance+")" )+ "\" " +
										outputFileName + "\n");
					
					// Nueva linea
					scriptContent.append("\n" );					
				}
			}
		
			// Concatenar y eliminar archivos temporales
			scriptContent.append("# ffmpeg -f concat -safe 0 -i " + LIST_FILENAME + " -c copy " + videoFile + "_Concat.mp4 \n"); // Esta forma de concatenar genera chisporroteo en kdenlive ¿?
			scriptContent.append("ffmpeg " + getInputFileNames(fileNo-1) + " -filter_complex \"" + getAVArguments(fileNo-1) + " concat=n=" + (fileNo-1) + ":v=1:a=1 [v] [a]\" -map \"[v]\" -map \"[a]\" " + videoFile + "_Concat.mp4 \n");
			scriptContent.append("rm -f tempo.mp4 \n" );		
			scriptContent.append("rm -f tempo2.mp4 \n" );
			
			// Escribir la lista 
			PrintWriter out = new PrintWriter(url(path, LIST_FILENAME));
			out.print(listContent);
			out.flush();
			out.close();

			// Escribir el .sh
			PrintWriter out2 = new PrintWriter(url(path, SCRIPT_FILENAME));
			out2.print(scriptContent);
			out2.flush();
			out2.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/** Concatena path y file adecudamente */
	public static String url(String path, String file) {
		return path + File.separator + file;
	}
	
	/** Escapea la linea */
	public static String escapeLine(String line) {
		return line.replace(":", "").replace("\'", "\\\\\\\\\\\'").replace(",", "\\,");
	}
	
	/** Mayuscula primer letra */
	protected static String capitalize(String word) {
		return word.toUpperCase().substring(0, 1) + word.substring(1);
	}
	
	/** Argumentos del dibujatexto */
	protected static String drawText(String color, int size, int boxBorder, String x, String y) {
		return 	"fontcolor=" + color + 
				": fontsize=" + size + 
				(boxBorder>0 ? ":box=1: boxcolor=black@0.5: boxborderw="+ boxBorder : "") + 
				": x=" + x +
				": y=" + y;
	}
	
	/** Ocurrencias de la palabra en la linea */
	protected static int matchesInLine(String line) {
		String[] words = line.split(" ");
		int cant=0;
		for (String word : words) {
			if (word.contains(criteria.toLowerCase())) {
				cant++;
			}
		}
		return cant;
	}
	
	/** Define desde y hasta del chunk, considerando un formato de tipo 02:08:16,840 --> 02:08:18,470 */
	protected static String[] defineStartEnd(String intervalLine) {
		// 02:08:16,840 --> 02:08:18,470
		String desde = intervalLine.split(" ")[0];
		String hasta = intervalLine.split(" ")[2];	
		// Armo un timestamp para luego restarle lo necesario. Ademas le restamos los segundos y el extra por desfasaje 
		Timestamp start = Timestamp.valueOf("2020-01-01 " + desde); 
		Long xtra = start.getTime() - secondsBack * 1000 - extraStartMS;
		Timestamp newStart = new Timestamp(xtra);
		desde = newStart.toString().toString().substring(11);
		// Armo un timestamp para luego restarle lo necesario. Le sumamos el extra por desfasaje unicamente 
		Timestamp end = Timestamp.valueOf("2020-01-01 " + hasta); 
		xtra = end.getTime() + extraStartMS;
		Timestamp newEnd = new Timestamp(xtra);
		hasta = newEnd.toString().toString().substring(11);
		return new String[] { desde, hasta };		
	}
	
	
	/** Es una linea de timestamp de tipo 02:08:16,840 --> 02:08:18,470 ? */
	@Deprecated
	protected static boolean isTimestampLine(String line) {
		try {
			// Analizar caracteristicas de la linea
			line = line.trim();
			String[] words = line.split(" ");
			String start = words[0];
			String end = words[2];
			boolean has3words = (words.length==3);
			boolean startsWithDigit = (Character.isDigit(start.charAt(0)) && Character.isDigit(end.charAt(0)));
			boolean isLongEnough = (start.length()>=8 && end.length()>=8);
			boolean cotainsTwoPoints = (start.contains(":") && end.contains(":"));
			return has3words && startsWithDigit && isLongEnough && cotainsTwoPoints;
		} catch (Exception e) {
			return false;
		}
	}
	
	/** Retorna las lineas de texto concatenadas en una unica linea, considerando que puede haber varias lineas de texto (1, 2 o mas) */
	protected static String getTextLine(ArrayList<String> chunkLines) {
		StringBuffer retValue = new StringBuffer();
		int i=0;
		for (String line : chunkLines) {
			if (i++<SRT_FIRST_TEXT) {
				continue;
			}
			retValue.append(line).append(" ");
		}
		return retValue.toString();
	}
	
	protected static String printableNumber(int number) {
		StringBuffer retValue = new StringBuffer();
		int amt = LEADING_ZEROES - String.valueOf(number).length();
		for (int i=0; i<amt; i++)
			retValue.append("0");
		return retValue.append(number).toString();
	}
	
	/** 
	 * Quita partes de mas que estan fuera de la frase con el match
	 * Por ejemplo:
	 * 		If you're gonna build three complete dream levels.  Excuse me.
	 * Se quita:
	 * 		Excuse me.
	 * 
	 * Problemas: Resolver casos de tipo: 
	 * 		Do you still dream, Mr. Cobb?
	 * 
	 * Todavía no completamente utilizable
	 *  */
	@Deprecated
	protected static String simplifySentence(String line) {
		// Buscar la posicion de la palabra referencial
		int center = line.indexOf(criteria);
		
		// Quedarme con la posicion de la frase hasta el punto posterior
		int end = line.replace("!", ".").replace(":", ".").replace("- ", ". ").replace("?", ".").substring(center).indexOf(".");
		if (end<0)
			end = line.length();
		else
			end = center + end + 2; // Considerar el simbolo de puntuacion 

		// Quedarme con la posicion de la frase hasta el punto anterior
		int start = line.replace("!", ".").replace(":", ".").replace("- ", ". ").replace("?", ".").substring(0, center).lastIndexOf(".");
		if (start<0)
			start = 0;
		else 
			start = start+2; // Ignorar el punto
		
		return line.substring(start, end).trim();
	}
	
	/** Retorna el nombre del archivo fileNo-esimo */
	protected static String getVideoFileName(int fileNo) {
		return videoFile + "_" + (printableNumber(fileNo)) + ".mp4";
	}
	

	/** Retorna todos los inputo files names para el ffmpeg */
	protected static String getInputFileNames(int fileCount) {
		StringBuffer retValue = new StringBuffer();
		for (int i=1; i<=fileCount; i++) {
			retValue.append(" -i " + getVideoFileName(i) );
		}	
		return retValue.toString();
	}
	
	/** Retorna los argumentos v y a para ffmpeg por cada archivo de entrada */
	protected static String getAVArguments(int fileCount) {
		StringBuffer retValue = new StringBuffer();
		for (int i=0; i<fileCount; i++) {
			retValue.append(" ["+i+":v] ["+i+":a] ");
		}	
		return retValue.toString();
	}
}
