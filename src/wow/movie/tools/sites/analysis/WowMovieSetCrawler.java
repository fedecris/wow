package wow.movie.tools.sites.analysis;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import wow.movie.tools.sites.analysis.parser.CriticsParser;
import wow.movie.tools.sites.analysis.parser.PublicParser;

/**
 * Recibe un json con los nombres de peliculas (title) el año de cada una (year) y el criterio de busqueda para Google Search
 * Si todo va bien, la clase hace el resto y genera el json con toda la información necesaria para ser volcada a WowMovieSetVideoGenerator.
 * { 
 * 	"movies":[
 * 				{
 * 					"search":"The Matrix 1999"
 * 					"title":"The Matrix",
 * 					"year":1999
 * 				},
 *   			{
 *   				"search":"Pirates of the Caribbean The Curse Of The Black Pearl 2003",
 * 					"title":"The Curse Of The Black Pearl",
 * 					"year":2003
 * 				}
 * 			]
 * } 
 */
public class WowMovieSetCrawler {

	/** Espera entre cada busqueda de google */
	protected static final int MIN_GS_WAIT_SECS = 5;
	/** Espera adicional random */
	protected static final int RND_GS_WAIT_SECS = 5;
	/** Espera entre cada pelicula */
	protected static final int MOVIE_WAIT_SECS = 5;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		
		if (args.length<2) {
			System.out.println("Te falta el path y nombre del archivo .json pah");
			System.exit(1);
		}
		
		String path = args[0];
		String file = args[1];
		
		try {
			// Parsear entrada
			JSONObject jo = (JSONObject)new JSONParser().parse(new FileReader(url(path, file)));			
			JSONArray ja = (JSONArray) jo.get("movies");
			long totalVotes = 0, totalReviews = 0;
			long totalBudget = 0, totalBoxOffice = 0;
			for (int i=0; i<ja.size(); i++){
				
				// Ya fue buscada la informacion?
				Map movie = (Map)ja.get(i);
				String title = (String)movie.get("title");
				String search = (String)movie.get("search");
				int year = ((Long)movie.get("year")).intValue();
				boolean fetched = (movie.get("fetched") !=null && (Boolean)movie.get("fetched"));
				if (fetched) {
					System.out.println("Omitiendo " + title + " (" + year + ")");
					continue;
				}
				
				// Crear el crawler y recuperar la info
				MovieCrawler crawler = new MovieCrawler(search, year);			
				crawler.fetch(new Double(MIN_GS_WAIT_SECS + RND_GS_WAIT_SECS * Math.random()).intValue());
				
				// Director/es
				String director = crawler.directorParser.getDirector();
				
				// Calcular totales por pelicula PUBLICO
				int publicSites = 0, criticSites = 0;
				double publicScore = 0f, criticsScore = 0f;
				long publicCount = 0, criticsCount = 0;
				JSONArray arrScore = new JSONArray();
				JSONArray arrCount = new JSONArray();
				for (PublicParser parser : crawler.publicParsers) {
					if (parser.getPublicScore() > 0) {
						publicSites++;
						publicScore = publicScore + parser.getPublicScore();
						publicCount = publicCount + parser.getPublicCount();
						
						Map mapScore = new LinkedHashMap();
						Map mapCount = new LinkedHashMap();
						mapScore.put(parser.getName(), parser.getPublicScore());
						mapCount.put(parser.getName(), parser.getPublicCount());
						arrScore.add(mapScore);
						arrCount.add(mapCount);
					}
				}
				totalVotes = totalVotes + publicCount;
				movie.put("detailPublicScores", arrScore);
				movie.put("detailPublicCounts", arrCount);
				
				// Calcular totales por pelicula CRITICOS
				JSONArray arrScoreC = new JSONArray();
				JSONArray arrCountC = new JSONArray();
				for (CriticsParser parser : crawler.criticsParsers) {
					if (parser.getCriticsScore() > 0) {
						criticSites++;
						criticsScore = criticsScore + parser.getCriticsScore();
						criticsCount = criticsCount + parser.getCriticsCount();	
						
						Map mapScoreC = new LinkedHashMap();
						Map mapCountC = new LinkedHashMap();
						mapScoreC.put(parser.getName(), parser.getCriticsScore());
						mapCountC.put(parser.getName(), parser.getCriticsCount());
						arrScoreC.add(mapScoreC);
						arrCountC.add(mapCountC);
					}
				}	
				totalReviews = totalReviews + criticsCount;
				movie.put("detailCriticsScores", arrScoreC);
				movie.put("detailCriticsCounts", arrCountC);
				
				// Totales $
				totalBudget = totalBudget + crawler.boxOfficeParser.getBudget();
				totalBoxOffice = totalBoxOffice + crawler.boxOfficeParser.getBoxOffice();
				
				// Calculos finales
				publicScore = publicScore / publicSites;
				criticsScore = criticsScore / criticSites;
				double roi = ((crawler.boxOfficeParser.getBoxOffice()/1000000f - crawler.boxOfficeParser.getBudget()/1000000f) / (crawler.boxOfficeParser.getBudget()/1000000f)) * 100;
				double finalScore = (1d * publicScore + 1d * criticsScore) / 2;
				double publicScoreW = getWeightedScore(movie, publicCount, "Public");
				double criticsScoreW = getWeightedScore(movie, criticsCount, "Critics");
				double finalScoreW = (1d * publicScoreW + 1d * criticsScoreW) / 2;
				System.out.println("");
				System.out.println(crawler.movie + " (" + crawler.year + ") ");
				System.out.println(crawler.directorParser.getDirector());
				System.out.println(  "Public:    " + publicScore  + ", " + publicCount  + " votes" );
				System.out.println(  "Public(W) :" + publicScoreW);
				System.out.println(  "Critics:   " + criticsScore + ", " + criticsCount + " reviews" );
				System.out.println(  "Critics(W):" + criticsScoreW);
				System.out.println(  "Final:     " + finalScore );
				System.out.println(  "Final(W):  " + finalScoreW);
				System.out.println(  "Budget:    " + crawler.boxOfficeParser.getBudget() );
				System.out.println(  "BoxOffice: " + crawler.boxOfficeParser.getBoxOffice() );
				System.out.println(  "R.O.I.:    " + roi);
				System.out.println();
				
				
				// Guardarlo en la estructura
				movie.put("fetched", true);
				movie.put("director", director);
				movie.put("publicScore", publicScore);
				movie.put("criticsScore", criticsScore);
				movie.put("publicCount", publicCount);
				movie.put("criticsCount", criticsCount);
				movie.put("finalScore", finalScore);
				movie.put("budget", crawler.boxOfficeParser.getBudget());
				movie.put("boxOffice", crawler.boxOfficeParser.getBoxOffice());
				movie.put("roi", roi);
				movie.put("publicScoreW", publicScoreW);
				movie.put("criticsScoreW", criticsScoreW);
				movie.put("finalScoreW", finalScoreW);
				
				// Descansar un ratito para evitar google sorry 
				System.out.print("Crawling en ");
				for (int w=MOVIE_WAIT_SECS; w>=0; w--) {
					System.out.print(w + " ");
					Thread.sleep(1000);
				}
				System.out.println(  "\n--------------------------------------------------------------------------------------------------------\n");
				
				
			}
			
			// Totales de todo el movieSet
			jo.put("totalVotes", totalVotes);
			jo.put("totalReviews", totalReviews);
			jo.put("totalBudget", totalBudget);
			jo.put("totalBoxOffice", totalBoxOffice);
			jo.put("totalMovies", ((JSONArray)jo.get("movies")).size());
			
			// Usar Gson para convertir a pretty print
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        JsonElement jsonElement =  new JsonParser().parse(jo.toString());

			// Escribir a archivo 
	        PrintWriter pw = new PrintWriter(url(path, file.replace(".json", "")+"_data.json")); 
	        pw.write(gson.toJson(jsonElement)); 
	          
	        pw.flush(); 
	        pw.close(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** Concatena path y file adecudamente */
	public static String url(String path, String file) {
		return path + File.separator + file;
	}

	/** Calcula el puntaje ponderado por votos, sin sobrepasar un límite maximo de votos */
	protected static Double getWeightedScore(Map movie, long totalVotes, String type) {
		// Maximo de votos atribuibles a un sitio  
		long maxVotes = totalVotes / ((JSONArray)movie.get("detail" + type + "Scores")).size();

		// Iterar y calcular el ponderado, sin sobrepasar el limite maximo (considerando el numero de sitios
		double totalScore = 0d;
		long totalCount = 0;
		JSONArray scores = ((JSONArray)movie.get("detail" + type + "Scores"));
		JSONArray counts = ((JSONArray)movie.get("detail" + type + "Counts"));
		for (int i=0; i < scores.size(); i++) {
			// Votos
			Map score = (Map)scores.get(i);
			Map vote = (Map)counts.get(i);
			Double scoreSite = null;
			try {
				scoreSite = (Double)(score.get(score.keySet().toArray()[0])); 
			} catch (Exception e) { 
				scoreSite = new Double((Float)score.get(score.keySet().toArray()[0]));
			};
			Long countSite = (Long)vote.get(vote.keySet().toArray()[0]);
			totalScore = totalScore + scoreSite * ( Math.min(countSite, maxVotes) );
			totalCount = totalCount + ( Math.min(countSite, maxVotes) );
		}
		return totalScore / totalCount; 
	}
	
	
}
