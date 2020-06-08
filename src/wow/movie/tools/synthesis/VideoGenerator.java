package wow.movie.tools.synthesis;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * A partir del JSon cargado con información mediante MovieSetCrawler, genera el script para crear los videos con la info superimpuesta
 * 
 * Nombres de los archivos: "trailer_secuencia_scaleFactor_trimStartSeconds.mp4"
 * 
 * Ejemplo:
 * 	trailer_00_1_30.mp4
 * 	trailer_01_1.35_15.mp4
 *  trailer_02_1_20.mp4
 *  trailer_03_1.5_0.mp4
 *  trailer_04_1.35_20.mp4
 * 
 */
public class VideoGenerator {

	/** === ARGUMENTOS A RECIBIR DESDE LA TERMINAL === */
	/** 0) Path de los archivos */
	protected static String path = "";
	/** 1) Nombre archivo entrada */
	protected static String inputFile = "";
	/** 2) Nombre de larchivo de salida */
	public static String scriptFileName = "";

	/** Duracion de cada clip. DEBE SER MENOR O IGUAL A 59 Segundos */
	protected static int clipDurationSec = 30;
	/** Recortar el inicio del video original cierta cantidad de segundos */
	protected static int trimStartSec = 20;
	/** Instante de inicio de la Secuencia de titulo (box y texto) */
	protected static int titleAppearSec = 2;
	/** Instante de inicio de la Secuencia de la información (box y texto) */
	protected static int infoAppearSec = 3;
	/** Tiempo mostrar nueva info */
	protected static int infoIncrementSec = 2;
	/** Tiempo a ocultar el texto/caja antes de que termine el video */
	protected static int hideBeforeEndSec = 1;
	
	
	/** Letra de la info */
	protected static String infoFont = "/home/usuario/Descargas/2020/fonts/Josefin_Sans/static/JosefinSans-SemiBold.ttf";
	/** Letra del movie title */
	protected static String titleFont = "/home/usuario/Descargas/2020/fonts/Nunito/Nunito-Regular.ttf";
	/** Letra del movie title */
	protected static float currentTrailerScale = 1f;
	/** Color fondo puntaje */
	protected static String scoreColor = "#293250";
	/** Color fondo roi */
	protected static String roiColor = "#6dd47e";
	
	public static void main(String[] args) {
		
		// Cadena con el contenido del script .sh
		StringBuffer scriptContent = new StringBuffer("");
		if (args.length<3) {
			System.out.println("Y el path? Y el nombre del json de entrada? Y el nombre del sh de salida?");
			System.exit(1);
		}
		
		// Asignar argumentos a variables
		path = args[0];
		inputFile = args[1];
		scriptFileName = args[2];
		
		try {
			// Numero de clip y nombre a recuperar
			String clipNo = "";
			String clipName = "";
			int firstClipTrimSec = 0;
			
			// Parsear entrada
			JSONObject jo = (JSONObject)new JSONParser().parse(new FileReader(url(path, inputFile)));
			JSONArray ja = (JSONArray) jo.get("movies");
			for (int i=0; i<ja.size(); i++){
				
				// Ya fue buscada la informacion?
				Map movie = (Map)ja.get(i);
				String title = (String)movie.get("title");
				int year = ((Long)movie.get("year")).intValue();
				boolean fetched = (movie.get("fetched") !=null && (Boolean)movie.get("fetched"));
				if (!fetched) {
					System.out.println("Omitiendo " + title + " (" + year + "). Todavia no se recupero la informacion.");
					continue;
				}
				clipNo = (i<10?"0":"") + i;
				clipName = getClipName(clipNo);
				int curInfoAppear = infoAppearSec;
				trimStartSec = Integer.parseInt(getTrimStartSecs(clipNo));
				if (i==0) {
					firstClipTrimSec = trimStartSec;
				}
				
				// Video de entrada y generalidades
				scriptContent.append("# === CLIP NUMERO " + clipNo + " === \n");
				scriptContent.append("ffmpeg -y -i " + clipName + " -an -vf \" ");
				scriptContent.append("fade=type=out:duration=1:start_time=" + (clipDurationSec+trimStartSec-1) + ", ");
				scriptContent.append("scale=iw*" + getScaleRatio(clipNo) + ":ih*" + getScaleRatio(clipNo) + ":force_original_aspect_ratio=increase, crop=1920:1080, "); 
				
				// Box rojo del titulo 
				scriptContent.append("drawbox=enable='between(t," + (titleAppearSec+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=20:y=920:h=140:w=iw-(2*20):color=#AA0000@0.4:t=max, ");
				scriptContent.append("drawbox=enable='between(t," + (titleAppearSec+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=20:y=920:h=140:w=iw-(2*20):color=black@0.4, ");
				
				// Titulo y director
				scriptContent.append("drawtext=enable='between(t," + (titleAppearSec-1+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + titleFont + ":text=" + getTitle(movie) + ":fontcolor=white:fontsize=75:x=max((w-(t-2.5-"+trimStartSec+")*w/1.2)\\,40):y=940:shadowcolor=black:shadowx=2:shadowy=2, ");
				scriptContent.append("drawtext=enable='between(t," + (titleAppearSec-1+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + titleFont + ":text=" + getDirector(movie) + ":fontcolor=white:fontsize=45:x=max((w-(t-2.5-"+trimStartSec+")*w/1.2)\\,40):y=1010:shadowcolor=black:shadowx=2:shadowy=2, ");
				
				// Box gris de la info
				scriptContent.append("drawbox=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=100:y=100:w=(iw-200):h=(ih-300):color=black@0.75:t=max, "); 
				scriptContent.append("drawbox=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=100:y=100:w=(iw-200):h=(ih-300):color=white@0.75:thickness=5, "); 
				curInfoAppear+=infoIncrementSec;
				
				// Public Score
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='Public\\: " + getPublicScore(movie) + "':fontcolor=white:fontsize=100:x=150:y=150:shadowcolor=black:shadowx=2:shadowy=2, ");
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='" + getPublicCount(movie) + "':fontcolor=white:fontsize=40:x=150:y=250:shadowcolor=black:shadowx=2:shadowy=2, ");
				curInfoAppear+=infoIncrementSec;
				
				// Critics Score
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='Critics\\: " + getCriticsScore(movie) + "':fontcolor=white:fontsize=100:x=150:y=425:shadowcolor=black:shadowx=2:shadowy=2, ");
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='" + getCriticsCount(movie) + "':fontcolor=white:fontsize=40:x=150:y=525:shadowcolor=black:shadowx=2:shadowy=2, ");
				curInfoAppear+=infoIncrementSec;
				
				// Final Score
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='Final\\: " + getFinalScore(movie) + "':fontcolor=white:fontsize=150:x=150:y=700:shadowcolor=black:shadowx=2:shadowy=2:box=1:boxborderw=12:boxcolor="+scoreColor+"@0.6, ");
				//	scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='Public vs Critics difference\\: " + getPvC(movie) + "':fontcolor=white:fontsize=40:x=150:y=650:shadowcolor=black:shadowx=2:shadowy=2, ");
				curInfoAppear+=infoIncrementSec;
				
				// Budget
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='Budget\\: " + getBudget(movie) + "':fontcolor=white:fontsize=100:x=1770-tw:y=150:shadowcolor=black:shadowx=2:shadowy=2, ");
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='2020\\: " + getMoney2020(movie, "budget") + "':fontcolor=white:fontsize=40:x=1770-tw:y=250:shadowcolor=black:shadowx=2:shadowy=2, ");
				curInfoAppear+=infoIncrementSec;
				
				// BoxOffice
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='Gross\\: " + getBoxOffice(movie) + "':fontcolor=white:fontsize=100:x=1770-tw:y=425:shadowcolor=black:shadowx=2:shadowy=2, ");
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='2020\\: " + getMoney2020(movie, "boxOffice") + "':fontcolor=white:fontsize=40:x=1770-tw:y=525:shadowcolor=black:shadowx=2:shadowy=2, ");
				curInfoAppear+=infoIncrementSec;
				
				// ROI
				scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='R.O.I.\\: " + getROI(movie) + "':fontcolor=white:fontsize=150:x=1770-tw:y=700:shadowcolor=black:shadowx=2:shadowy=2:box=1:boxborderw=12:boxcolor="+roiColor+"@0.6 ");
				//	scriptContent.append("drawtext=enable='between(t," + (curInfoAppear+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='(Gross revenue)':fontcolor=white:fontsize=40:x=1770-tw:y=650:shadowcolor=black:shadowx=2:shadowy=2 ");
				
				// Fin
				scriptContent.append("\" -ss " + addSeconds("00:00:00", trimStartSec) + " -to " + addSeconds("00:00:" + clipDurationSec, trimStartSec) + " clip_" + (i<10?"0":"") + i + ".mp4 \n");
							
			}
			
			
			

			// === GRAFICO FINAL === 
			scriptContent.append("# === CLIP DE GRAFICO FINAL === \n");

			String lastClip = clipNo;			// me guardo el ultimo clip para la intro
			int lastClipTrimSec = trimStartSec;	// me guardo el trim clip para la intro
			
			// Usamos el primer clip ya recortado, lo procesamos desde el principio
			clipNo = "00";				// uso el primer clip  para el grafico dado que fue un clip que se vio hace mucho
			trimStartSec=firstClipTrimSec;
			clipDurationSec=30;
			hideBeforeEndSec=0;

			
			
			// Blur
			scriptContent.append("ffmpeg -y -i " + getClipName(clipNo) + " -an -vf \" ");
			scriptContent.append("scale=iw*" + getScaleRatio(clipNo) + ":ih*" + getScaleRatio(clipNo) + ":force_original_aspect_ratio=increase, crop=1920:1080, ");
			scriptContent.append("gblur=sigma=20:steps=1\" tmp.mp4; ");			

			// Box gris del titulo 
			scriptContent.append("ffmpeg -y -i tmp.mp4 -an -vf \" "); //
			scriptContent.append("drawbox=enable='between(t," + (titleAppearSec+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=20:y=20:h=ih-(2*20):w=iw-(2*20):color=black@0.4:t=max, ");
			scriptContent.append("drawbox=enable='between(t," + (titleAppearSec+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=20:y=20:h=ih-(2*20):w=iw-(2*20):color=white@0.4, ");
			
			// Posicion e incremento de texto y boxes
			int movieCount = ja.size();
			int longTitle = 50;
			int alto = 1000, inicioY = 40;
			int incrementoY = alto / movieCount;
			int finalY = inicioY + ((movieCount-1) * incrementoY);  
			inicioY = inicioY + (alto - finalY)/2;   // reajustar para adecuar posicion vertical
			int currentY = inicioY; 
			
			// Limites del grafico
			int minX = 80, maxX = 1840;
			double maxROI = -99999999;
			double maxScore = -99999999;
			for (int i=0; i<movieCount; i++) {
				Map movie = (Map)ja.get(i);
				double curROI = (Double)movie.get("roi");
				if (curROI>maxROI) {
					maxROI = curROI;
				}				
				double curScore = (Double)movie.get("finalScore");
				if (curScore>maxScore) {
					maxScore=curScore;
				}
			}
			
			// Tamaño texto y barras en funcion de numero de pelis
			int fontSize = 80;
			int barH = 70;
			if (movieCount>=10) { 
				fontSize = 65;
				barH = 55;
			}
			if (movieCount>=18) {
				fontSize = 50;
				barH = 40;
			}
			
			// Visualizar titulos incrementalmente
			float animDuration = 2;
			float currentDisp = 0f;
			float dispInc = animDuration / movieCount;
			for (int i=0; i<movieCount; i++) {
				Map movie = (Map)ja.get(i);
				String title = getTitleRecortado(movie, longTitle);

				// ROI Bar
				scriptContent.append("drawbox=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1+animDuration) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=(iw/2):y="+currentY+":h="+barH+":w="+getBarW(movie, "roi", maxX/2, maxROI)+":color="+roiColor+"@0.6:t=max, ");
				
				// Score Bar
				scriptContent.append("drawbox=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1+animDuration) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=(iw/2)-"+getBarW(movie, "finalScore", maxX/2, maxScore)+":y="+currentY+":h="+barH+":w="+getBarW(movie, "finalScore", maxX/2, maxScore)+":color="+scoreColor+"@0.6:t=max, ");

				// Titulo de la peli
				scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + titleFont + ":text=" + title + ":fontcolor=white:fontsize="+fontSize+":x=(w-text_w)/2:y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2, ");

				// ROI Value
				scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1+animDuration) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='" + getROI(movie) + "':fontcolor=yellow:fontsize="+fontSize/2+":x=(w/2)+"+getBarW(movie, "roi", maxX/2, maxROI)+"-(text_w):y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2, ");

				// Score Value
				scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1+animDuration) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text='" + getFinalScore(movie) + "':fontcolor=yellow:fontsize="+fontSize/2+":x=(w/2)-"+getBarW(movie, "finalScore", maxX/2, maxScore)+":y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2 " + (i<movieCount-1?", ":" "));
				
				currentY += incrementoY;
				currentDisp+=dispInc;
			}

			// Fin
			scriptContent.append("\" -ss " + addSeconds("00:00:00", trimStartSec) + " -to " + addSeconds("00:00:" + clipDurationSec, trimStartSec) + " clip_final.mp4 \n");

			
			
			
			
			// === INTRO ===
			scriptContent.append("# === CLIP DE INTRODUCCION === \n");
			
			// Usamos el primer clip procesandolo completamente
			trimStartSec=lastClipTrimSec;
			clipDurationSec=15;
			hideBeforeEndSec=0;
			clipNo = lastClip;  // uso el ultimo clip dado que es el que se va a ver dentro de mucho tiempo con respecto a la introduccion
			
			// Blur
			scriptContent.append("ffmpeg -y -i " + getClipName(clipNo) + " -an -vf \" ");
			scriptContent.append("scale=iw*" + getScaleRatio(clipNo) + ":ih*" + getScaleRatio(clipNo) + ":force_original_aspect_ratio=increase, crop=1920:1080, ");
			scriptContent.append("gblur=sigma=20:steps=1\" tmp.mp4; ");			

			// Box gris del titulo 
			scriptContent.append("ffmpeg -y -i tmp.mp4 -an -vf \" "); //
			scriptContent.append("drawbox=enable='between(t," + (titleAppearSec+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=120:y=100:h=ih-(2*100):w=iw-(2*120):color=black@0.4:t=max, ");
			scriptContent.append("drawbox=enable='between(t," + (titleAppearSec+trimStartSec) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':x=120:y=100:h=ih-(2*100):w=iw-(2*120):color=white@0.4, ");
			
			// Posicion e incremento de texto (5 renglones)
			alto = 700;
			int renglones = 5;
			inicioY = 200;
			incrementoY = alto / renglones;
			currentY = inicioY; 
			
			// Tamaño texto
			fontSize = 100;
			
			// Visualizar información incrementalmente
			animDuration = 5;
			currentDisp = 0f;
			dispInc = animDuration / renglones;
			
			// Titulo de la peli
			scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text=" + getTotalFilms(jo) + ":fontcolor=white:fontsize="+fontSize+":x=(w-text_w)/2:y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2, "); 
			currentY += incrementoY;
			currentDisp+=dispInc;
			scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text=" + getTotalReviews(jo) + ":fontcolor=white:fontsize="+fontSize+":x=(w-text_w)/2:y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2, ");
			currentY += incrementoY;
			currentDisp+=dispInc;
			scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text=" + getTotalVotes(jo) + ":fontcolor=white:fontsize="+fontSize+":x=(w-text_w)/2:y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2, ");
			currentY += incrementoY;
			currentDisp+=dispInc;
			scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text=" + getTotalBudget(jo) + ":fontcolor=white:fontsize="+fontSize+":x=(w-text_w)/2:y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2, ");
			currentY += incrementoY;
			currentDisp+=dispInc;
			scriptContent.append("drawtext=enable='between(t," + (currentDisp+titleAppearSec+trimStartSec+1) + "," + (trimStartSec+clipDurationSec-hideBeforeEndSec) + ")':fontfile=" + infoFont + ":text=" + getTotalBoxOffice(jo) + ":fontcolor=white:fontsize="+fontSize+":x=(w-text_w)/2:y="+currentY+":shadowcolor=black:shadowx=2:shadowy=2 ");
			
			// Fin
			scriptContent.append("\" -ss " + addSeconds("00:00:00", trimStartSec) + " -to " + addSeconds("00:00:" + clipDurationSec, trimStartSec) + " clip_inicial.mp4 \n");
			

			// Escribir el .sh
			PrintWriter out = new PrintWriter(url(path, scriptFileName));
			out.print(scriptContent);
			out.flush();
			out.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getTitle(Map movie) {
		return sanitize((String)movie.get("title") + " (" + (Long)movie.get("year") + ")");
	}
	
	public static String getDirector(Map movie) {
		return sanitize((String)movie.get("director"));
	}
	
	public static String getPublicScore(Map movie) {
		return bigDecimalFormat(new BigDecimal((Double)movie.get("publicScore")), 2, 1L);
	}
	
	public static String getCriticsScore(Map movie) {
		return bigDecimalFormat(new BigDecimal((Double)movie.get("criticsScore")), 2, 1L); 
	}
	
	public static String getPublicCount(Map movie) {
		Long divideFactor = 1000000L;
		int decimals = 2;
		Long count = (Long)movie.get("publicCount");
		if (count<1000000L) {
			divideFactor = 1L;
			decimals = 0;
		}
		String retValue = bigDecimalFormat(new BigDecimal((Long)movie.get("publicCount")), decimals, divideFactor) + (divideFactor==1L?"":"M");
		// Si no  visualizamos por millones, aplicar separadores de miles
		if (divideFactor == 1) {
			retValue = thousandSeparator(retValue);
		}
		return sanitize(retValue + " votes");
	}
	
	public static String getCriticsCount(Map movie) {
		return sanitize(thousandSeparator(bigDecimalFormat(new BigDecimal((Long)movie.get("criticsCount")), 0, 1L)) + " reviews");
	}
	
	public static String getFinalScore(Map movie) {
		return bigDecimalFormat(new BigDecimal((Double)movie.get("finalScore")), 2, 1L);
	}
	
	public static String getBudget(Map movie) {
		return sanitize("$" + thousandSeparator(bigDecimalFormat(new BigDecimal((Long)movie.get("budget")), 0, 1000000L)) + "M");
	}
	
	public static String getBoxOffice(Map movie) {
		return sanitize("$" + thousandSeparator(bigDecimalFormat(new BigDecimal((Long)movie.get("boxOffice")), 0, 1000000L)) + "M");
	}

	public static String getROI(Map movie) {
		return sanitize(bigDecimalFormat(new BigDecimal((Double)movie.get("roi")), 0, 1L) + "%");
	}
	
	public static String getPvC(Map movie) {
		return sanitize(bigDecimalFormat(new BigDecimal(Math.abs((Double)movie.get("publicScore")-(Double)movie.get("criticsScore"))), 2, 1L));
	}
	
	public static String getMoney2020(Map movie, String field) {
		int year = ((Long)movie.get("year")).intValue();
		float rate = Inflation.rates2020.get(year);
		BigDecimal amount = new BigDecimal((Long)movie.get(field));
		amount = amount.multiply(new BigDecimal(rate));
		return sanitize("$" + bigDecimalFormat(amount, 0, 1000000L) + "M");
	}
		
	public static String bigDecimalFormat(BigDecimal bd, int decimals, Long divideFactor) {
		bd = bd.divide(new BigDecimal(divideFactor));
		bd = bd.setScale(decimals, BigDecimal.ROUND_HALF_UP);
		return bd.toString();	
	}
	
	/** Reemplazos y mas reemplazos */
	public static String sanitize(String text) {
		return text.replace(",", "\\,").replace(":", "\\:").replace("\'", "\\\\\\\\\\\'").replace("$", "\\$").replace("%", "\\\\\\%");
	}
	
	
	/** Concatena path y file adecudamente */
	public static String url(String path, String file) {
		return path + File.separator + file;
	}
	
	public static String addSeconds(String desde, int shiftSecs) {
		// Armo un timestamp para luego sumarle lo necesario.  
		Timestamp start = Timestamp.valueOf("2020-01-01 " + desde); 
		Long xtra = start.getTime() + shiftSecs * 1000;
		Timestamp newStart = new Timestamp(xtra);
		return newStart.toString().toString().substring(11);
	}
	
	public static String getClipName(String clipNo) {
		return "trailer_" + clipNo + "_" + getScaleRatio(clipNo) + "_" + getTrimStartSecs(clipNo) + ".mp4";
	}
	
	
	/** Considera el formato: trailer_01_1.35_20.mp4 */
	public static String getScaleRatio(String clipNo) {
        File f = new File(path);
        String[] pathnames = f.list();

        // For each pathname in the pathnames array
        for (String name : pathnames) {
        	if (name.startsWith("trailer_" + clipNo + "_") && name.endsWith(".mp4") ) {
        		String[] parts = name.split("_");
        		return parts[2];
        	}
        }
        // Por defecto devuelve 1
		return "1";
	}
	
	/** Considera el formato: trailer_01_1.35_20.mp4 */
	public static String getTrimStartSecs(String clipNo) {
        File f = new File(path);
        String[] pathnames = f.list();

        // For each pathname in the pathnames array
        for (String name : pathnames) {
        	if (name.startsWith("trailer_" + clipNo + "_") && name.endsWith(".mp4") ) {
        		String[] parts = name.split("_");
        		return parts[3].replace(".mp4", "");
        	}
        }
        // Por defecto devuelve 30
		return "30";
		
	}
	
	
	public static String getTitleRecortado(Map movie, int maxLen) {
		String title = (String)movie.get("title");
		int to = Math.min(maxLen, title.length());
		return sanitize(title.substring(0, to));
	}
	
	public static String getBarW(Map movie, String field, int maxW, double maxVal) {
		double value = (Double)movie.get(field);
		double factor = value / maxVal;
		double result = maxW * factor;
		return ""+result;
	}
	
	public static String getTotalFilms(JSONObject jo) {
		return sanitize(jo.get("totalMovies") + " films");
	}
	
	public static String getTotalReviews(JSONObject jo) {
		return sanitize(thousandSeparator((Long)jo.get("totalReviews")) + " reviews");
	}
	
	public static String getTotalVotes(JSONObject jo) {
		return sanitize(thousandSeparator((Long)jo.get("totalVotes")) + " votes");
	}
	
	public static String getTotalBudget(JSONObject jo) {
		return sanitize("$" + thousandSeparator((Long)jo.get("totalBudget")) + " budget");
	}

	public static String getTotalBoxOffice(JSONObject jo) {
		return sanitize("$" + thousandSeparator((Long)jo.get("totalBoxOffice")) + " revenue");
	}
	
	public static String thousandSeparator(String longValue) {
		return thousandSeparator(Long.parseLong(longValue));
	}
	
	public static String thousandSeparator(Long value) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator(',');
		formatter.setDecimalFormatSymbols(symbols);
		return ""+formatter.format(value.longValue());
	}
	
}
