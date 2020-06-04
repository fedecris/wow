package wow.movie.tools.sites.analysis;

import java.net.URLEncoder;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SitesManager {

	public static final String SITE_IMDB 	= "imdb.com";
	public static final String SITE_RT 		= "rottentomatoes.com";
	public static final String SITE_MC 		= "metacritic.com";
	public static final String SITE_FA 		= "filmaffinity.com";
	public static final String SITE_BOM		= "www.boxofficemojo.com";

	/** Site -> URL con la info */
	public HashMap<String, String> links = new HashMap<String, String>(); 
	
	/* =============== PROTECTED ================= */
	
	protected static final String[] CRITERIA_IMDB 		= new String[] { "http", "imdb.com/", 					"/title/" };
	protected static final String[] CRITERIA_RT 		= new String[] { "http", "rottentomatoes.com/", 		"/m/" };
	protected static final String[] CRITERIA_MC 		= new String[] { "http", "metacritic.com/", 			"/movie/" };
	protected static final String[] CRITERIA_FA 		= new String[] { "http", "filmaffinity.com/", 			"/film" };
	protected static final String[] CRITERIA_BOM		= new String[] { "http", "boxofficemojo.com/", 			"/release/" };
	
	protected static String[] sites = new String[] {SITE_IMDB, SITE_RT, SITE_MC, SITE_FA, SITE_BOM };
		
	static HashMap<String, String[]> siteCriteria = new HashMap<String, String[]>();
	
	static {
		siteCriteria.put(SITE_IMDB, CRITERIA_IMDB);
		siteCriteria.put(SITE_RT, 	CRITERIA_RT);
		siteCriteria.put(SITE_MC, 	CRITERIA_MC);
		siteCriteria.put(SITE_FA, 	CRITERIA_FA);
		siteCriteria.put(SITE_BOM, 	CRITERIA_BOM);
	}
	
	/** BaseURL */
	protected String googleQuery = "https://www.google.com/search?q=";
	
	public void getLinksForMovie(String movie) {
		
		try {

			// Parsear cada sitio
			for (String site : sites) {
				String query = URLEncoder.encode(movie + " site:" + site, "UTF-8");
				
				// Conectando a google
				//System.out.println("Accediendo a " + googleQuery + query + ".");
				Document doc = Jsoup.connect(googleQuery + query).get();
				
				// Procesando resultado
				Elements elements = doc.getElementsByTag("a");
				int matches = 0;
				for (Element element : elements) {
					// Buscar enlace analizando todos los criterios de matcheo
					matches = 0;
					for (String criteria : siteCriteria.get(site)) {
						// System.out.println("     " + element.attr("href"));
						if (!element.attr("href").contains(criteria))
							break;
						matches++;
					}
					if (matches==siteCriteria.get(site).length) {
						String target = element.attr("href").toString();
						// Por si pasa algo así: http://www.metacritic.com/movie/pirates-of-the-caribbean-on-stranger-tides/user-reviews?page=1
						if (target.split("/").length==6)
							target = target.substring(0, target.lastIndexOf("/"));
						// Reemplazar http por https si corresponde
						target = target.replace("http:", "https:");
						System.out.println(" Enlace " + target);		
						links.put(site, target);
						break;
					} 
				}		
				if (matches<siteCriteria.get(site).length) {
					System.out.println(" No encontré el enlace para " + site + "! BUUHUHU!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
