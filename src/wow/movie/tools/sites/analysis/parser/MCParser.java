package wow.movie.tools.sites.analysis.parser;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class MCParser extends WebParser implements PublicParser, CriticsParser {
	
	
	public MCParser(String url) {
		super(url);
		name = "MC";
	}
	
	@Override
	protected void load() throws Exception {
		Document doc = Jsoup.connect(url).get();
		
		// Puntaje 
		Elements elements = doc.getElementsByClass("metascore_anchor"); 
		for (Element element : elements) {
			Iterator<Node> it = element.childNodes().iterator();
			while (it.hasNext()) {
				Node node = it.next();
				if (node.childNodes().size()==0 || node.attr("class")==null || !node.attr("class").startsWith("metascore_w") )
					continue;
				// Usuario
				if (node.attr("class").startsWith("metascore_w user")) {
					publicScore = Float.parseFloat((String)node.childNodes().get(0).toString());
				} else {
					// 
					criticsScore = 1f * Float.parseFloat((String)node.childNodes().get(0).toString()) / 10;
				}
			}
		};

		elements = doc.getElementsByClass("based_on");
		for (Element element : elements) {
			if (element.childNodes()==null || element.childNodes().size()==0)
				continue;
			String node = element.childNodes().get(0).toString().toLowerCase().replace(" ", "").replace("basedon", "");
			// criticos
			if (node.contains("criticreviews")) {
				node = node.substring(0, node.indexOf("criticreviews"));
				criticsCount = Long.parseLong(node.replace(".", "").replace(",", ""));
			}
			// public			
			if (node.contains("ratings")) {
				node = node.substring(0, node.indexOf("ratings"));
				publicCount = Long.parseLong(node.replace(".", "").replace(",", ""));
			}
			
		}

	}
	

}
