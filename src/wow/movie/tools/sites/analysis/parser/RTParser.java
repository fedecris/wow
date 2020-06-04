package wow.movie.tools.sites.analysis.parser;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class RTParser extends WebParser implements PublicParser, CriticsParser {

	public RTParser(String url) {
		super(url);
		name = "RT";
	}

	@Override
	protected void load() throws Exception {
		Document doc = Jsoup.connect(url).get();
		
		// CRITICOS
		Elements elements = doc.getElementsByTag("script");
		for (Element element : elements) {
			if (!"application/ld+json".equals(element.attr("type"))) {
				continue;
			}
			JSONObject jo = (JSONObject)new JSONParser().parse(element.childNodes().get(0).toString());
			if (!jo.containsKey("aggregateRating"))
				continue;
			JSONObject critics = (JSONObject)jo.get("aggregateRating");
			criticsScore = 1f * (Long)critics.get("ratingValue") / 10; // Float.parseFloat(element.childNodes().get(0).toString());
			criticsCount = (Long)critics.get("reviewCount");  			
		};
		
		// PUBLICO
		elements = doc.getElementsByTag("script");
		String key = "root.RottenTomatoes.context.scoreInfo = ";
		for (Element element : elements) {
			if (!"text/javascript".equals(element.attr("type"))) {
				continue;
			}
			if (!element.childNodes().get(0).toString().contains(key))
					return; 
			String content = element.childNodes().get(0).toString();
			int start = content.indexOf(key);
			int end = content.substring(start).indexOf("\n") + start;
			start=start+"root.RottenTomatoes.context.scoreInfo = ".length();
			//System.out.println(content.substring(start, end));
			
			JSONObject jo = (JSONObject)new JSONParser().parse(content.substring(start, end).replace(";", " "));
			JSONObject audience = (JSONObject)jo.get("audienceAll");
			publicScore = 1f * Long.parseLong((String)audience.get("score")) / 10;
			publicCount = (Long)audience.get("ratingCount");  			
		};
		
	}
}
