package wow.movie.tools.sites.analysis.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * BoxOfficeMojo parser
 * @author usuario
 *
 */
public class BOMParser extends WebParser implements BoxOfficeParser {

	public BOMParser(String url) {
		super(url);
		name = "BOM";
	}

	@Override
	protected void load() throws Exception {
		Document doc = Jsoup.connect(url).get();
		
		// Budget
		Elements elements = doc.getElementsByTag("span");
		for (Element element : elements) {
			if (element.childNodes()==null)
				return;
			if (element.childNodes().size()>0 && element.childNodes().get(0).toString().equalsIgnoreCase("Budget")) {
				//System.out.println(element.toString() + " " + element.childNodes().size() + " " + element.siblingNodes().size());
				//System.out.println("---");	
				budget = Long.parseLong(element.siblingNodes().get(0).childNodes().get(0).childNodes().get(0).toString().replace(",", "").replace(".", "").replace("$", ""));
			}
		}
		

		// BoxOffice
		elements = doc.getElementsByTag("span");
		for (Element element : elements) {
			if (element.childNodes()==null)
				return;
			if (element.childNodes().size()>1 && element.childNodes().get(1).toString().trim().contains("Worldwide")) {
				//System.out.println(element.toString() + " " + element.childNodes().size() + " " + element.siblingNodes().size());
				boxOffice = Long.parseLong(element.siblingNodes().get(4).childNodes().get(1).childNodes().get(0).childNodes().get(0).toString().replace(",", "").replace(".", "").replace("$", ""));
			}
		}
		
	}
	
	
}
