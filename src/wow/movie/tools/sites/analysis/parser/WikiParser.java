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
			long scale = 1L;
			String content = node.toString().toLowerCase();
			content = content.substring(content.indexOf("$")+1);
			content = content.substring(0, content.indexOf("<"));
			content = content.replace("&nbsp;", "").trim();
			content = content.replace("–", "-"); // OJO no es el signo menos, es otro caracter similar el que utilizan, lo reemplazo por el - tradicional
			content = content.replace(",", ""); // separadores de miles no me interesan
			
			// Si contiene million, cambiar la escala y eliminar del contenido (sea que escribieron millions o million)
			if (content.contains("million")) {
				scale = 1000000L;
				int pos = content.indexOf("million");
				content = content.substring(0, pos).trim();
			} 
			
			// Definieron un rango? Promediar
			String c = "-"; 
			if (content.contains(c)) {
				// quitar cualquier otro signo peso si es que existe, por ejemplo $100,000-$150,000 (aunque generalmente lo indican como $100.000-150.000
				content = content.replace("$", "");
				// Calcular el promedio
				Double a = new Double(content.split(c)[0]);
				Double b = new Double(content.split(c)[1]);
				content = new Double((a+b)/2).toString();
			}
			
			// Retornar el valor considerando la escala
			Double d = new Double(content) * scale;
			return d.longValue();
		} catch (Exception e) {
			return -1;
		}
	}
}
