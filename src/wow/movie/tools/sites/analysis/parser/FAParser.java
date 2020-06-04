package wow.movie.tools.sites.analysis.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * FilmAffinity parser
 * @author usuario
 *
 */
public class FAParser extends WebParser implements PublicParser, CriticsParser {

	public FAParser(String url) {
		super(url);
		name = "FA";
}

	@Override
	protected void load() throws Exception {
		Document doc = Jsoup.connect(url).get();
		// Votos y cantidad del publico
		Elements elements = doc.getElementsByAttributeValue("itemprop", "ratingCount");
		for (Element element : elements) {
			publicCount = Long.parseLong(element.childNodes().get(0).toString().replace(",", "").replace(".", ""));
		};
		elements = doc.getElementsByAttributeValue("itemprop", "ratingValue");
		for (Element element : elements) {
			publicScore = Float.parseFloat(element.childNodes().get(0).toString().replace(",", "").replace(".", "")) / 10;
		};		

		// Criticos
		int positive = 0, neutral = 0, negative = 0;
		int value;
		elements = doc.getElementsByClass("leg");
		for (Element element : elements) {
			if (element.childNodes().get(0)==null || element.childNodes().get(3)==null)
				continue;
			value = Integer.parseInt(element.childNodes().get(0).toString().replace("\n","").replace(",", "").replace(".", ""));
			if (element.childNodes().get(3).toString().toLowerCase().contains("positiv")) 
				positive = value;
			else if (element.childNodes().get(3).toString().toLowerCase().contains("neutral"))
				neutral = value;
			else if (element.childNodes().get(3).toString().toLowerCase().contains("negativ"))
				negative = value;			
		}
		criticsCount = positive + neutral + negative;
		if (criticsCount>0) {
			criticsScore = ( 10f * positive + 5f * neutral ) / criticsCount;
		}
	}

}
