package wow.movie.tools.sites.analysis.parser;

import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IMDBParser extends WebParser implements PublicParser, DirectorParser {

	public IMDBParser(String url) {
		super(url);
		name = "IMDb";
	}
	
	@Override
	public float getCriticsScore() {
		throw new RuntimeException("Not supported");
	}
	
	@Override
	public long getCriticsCount() {
		throw new RuntimeException("Not supported");
	}
		
	protected void load() throws Exception {
		Document doc = Jsoup.connect(url).get();
		
		// Puntaje 
		Elements elements = doc.getElementsByAttributeValue("itemprop", "ratingValue");
		for (Element element : elements) {
			publicScore = Float.parseFloat(element.childNodes().get(0).toString());
		};
		
		// Votos		
		elements = doc.getElementsByAttributeValue("itemprop", "ratingCount");
		for (Element element : elements) {
			publicCount = Long.parseLong(element.childNodes().get(0).toString().replace(",", ""));
		};
		
		// Director
		StringBuffer res = new StringBuffer();
		elements = doc.getElementsByTag("script");
		for (Element element : elements) {
			if (!"application/ld+json".equals(element.attr("type"))) {
				continue;
			}
			JSONObject jo = (JSONObject)new JSONParser().parse(element.childNodes().get(0).toString());
			if (!jo.containsKey("director"))
				continue;
			if (jo.get("director") instanceof JSONObject) {
				JSONObject dir = (JSONObject)jo.get("director");
				director = (String)dir.get("name");
				break;
			} else if (jo.get("director") instanceof JSONArray) {
				JSONArray ja = (JSONArray)jo.get("director");
				Iterator directors = ja.iterator();
				while (directors.hasNext()) {
					res.append(((Map)directors.next()).get("name")).append(", ");	
				}
				director = res.toString().substring(0, res.toString().length()-2);
				break;
			}
		}
		
		
	}


	
	
	
}
