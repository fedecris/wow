package wow.movie.tools.sites.analysis.parser;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


public class WikiParser extends WebParser implements BoxOfficeParser {

	public WikiParser(String url) {
		super(url);
		name = "Wiki";
	}
	
	
	@Override
	protected void load() throws Exception {
		Document doc = Jsoup.connect(url).get();
		
		// Budget
		Elements elements = doc.getElementsByClass("infobox vevent");
		for (Element element : elements) {
			if (element.childNodes()==null)
				return;
			Iterator<Node> it = element.childNodes().iterator();
			while (it.hasNext()) {
				Node node = it.next();
				Iterator<Node> it2 = node.childNodes().iterator(); // tiene 17 childs
				while (it2.hasNext()) {
						Node node2 = it2.next();
						Iterator<Node> it3 = node2.childNodes().iterator();
						boolean budgetTitleFound = false;
						boolean boxOfficeTitleFound = false;
						while (it3.hasNext()) {
							Node node3 = it3.next();
							String content = node3.toString().toLowerCase();
							// En la iteracion anterior encontro el title, asi que ahora esta el budget
							if (budgetTitleFound) {
								budget = node2long(node3); 
								budgetTitleFound = false;
							}
							if (boxOfficeTitleFound) {
								boxOffice = node2long(node3);
								boxOfficeTitleFound = false;
							}
							if (content.contains("budget")) {
								budgetTitleFound = true;
							}
							if (content.contains("box office")) {
								boxOfficeTitleFound = true;
							} 
						}
					
				node.toString();
				}
			}
		}
	}
	
	protected long node2long(Node node) {
		try {
			String content = node.toString().toLowerCase();
			content = content.substring(content.indexOf("$")+1);
			content = content.substring(0, content.indexOf("<"));
			content = content.trim();
			
			// Contiene million el valor?
			if (!content.contains("million")) {
				Double d = new Double(content);
				return d.longValue();
			}
			
			// Contiene million?
			content = content.substring(0, content.indexOf("million")-1);
			Double d = new Double(content) * 1000000;
			return d.longValue();
		} catch (Exception e) {
			return -1;
		}
	}
}
