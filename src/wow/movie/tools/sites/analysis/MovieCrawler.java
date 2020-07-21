package wow.movie.tools.sites.analysis;

import java.util.ArrayList;

import wow.movie.tools.sites.analysis.parser.BOMParser;
import wow.movie.tools.sites.analysis.parser.BoxOfficeParser;
import wow.movie.tools.sites.analysis.parser.CriticsParser;
import wow.movie.tools.sites.analysis.parser.DirectorParser;
import wow.movie.tools.sites.analysis.parser.FAParser;
import wow.movie.tools.sites.analysis.parser.IMDBParser;
import wow.movie.tools.sites.analysis.parser.MCParser;
import wow.movie.tools.sites.analysis.parser.PublicParser;
import wow.movie.tools.sites.analysis.parser.RTParser;
import wow.movie.tools.sites.analysis.parser.WikiParser;
import wow.movie.tools.sites.utils.Log;

public class MovieCrawler {
	
	public ArrayList<PublicParser> publicParsers = new ArrayList<PublicParser>();
	public ArrayList<CriticsParser> criticsParsers = new ArrayList<CriticsParser>();
	public BoxOfficeParser boxOfficeParser = null;
	public DirectorParser directorParser = null;

	public String movie = null;
	public Integer year = null;
	
	protected static StringBuffer logContainer = null;
	
	public MovieCrawler(String movie, int year, StringBuffer logContainer) {
		this.movie = movie;
		this.year = year;
		this.logContainer = logContainer;
	}
	
	/** Segundos entre busqueda y busqueda en Google */
	public void fetch(int delaySecs) {
		
		log("%s", "");
		log("%s", "Feching info for " + movie + "(" + year + ")");
		
		SitesManager cs = new SitesManager();
		cs.getLinksForMovie(movie + " " + year, delaySecs);
		
		IMDBParser imdb = new IMDBParser(cs.links.get(SitesManager.SITE_IMDB));
		imdb.execute();
		publicParsers.add(imdb);
		directorParser = imdb;
		log("%s\t%s\t%s", "IMDB:", ""+imdb.getPublicScore(), ""+imdb.getPublicCount());

		RTParser rt = new RTParser(cs.links.get(SitesManager.SITE_RT));
		rt.execute();
		publicParsers.add(rt);
		criticsParsers.add(rt);
		log("%s\t%s\t%s\t%s\t%s", "RT:", ""+rt.getPublicScore(), ""+rt.getPublicCount(), ""+rt.getCriticsScore(), ""+rt.getCriticsCount());		
		
		MCParser mc = new MCParser(cs.links.get(SitesManager.SITE_MC));
		mc.execute();
		publicParsers.add(mc);
		criticsParsers.add(mc);		
		log("%s\t%s\t%s\t%s\t%s", "MC:", ""+mc.getPublicScore(), ""+mc.getPublicCount(), ""+mc.getCriticsScore(), ""+mc.getCriticsCount());
		
		FAParser fa = new FAParser(cs.links.get(SitesManager.SITE_FA));
		fa.execute();
		publicParsers.add(fa);
		criticsParsers.add(fa);		
		log("%s\t%s\t%s\t%s\t%s", "FA:", ""+fa.getPublicScore(), ""+fa.getPublicCount(), ""+fa.getCriticsScore(), ""+fa.getCriticsCount());
		
		BOMParser bom = new BOMParser(cs.links.get(SitesManager.SITE_BOM));
		bom.execute();
		boxOfficeParser = bom;
		log("%s\t%s\t%s", "BOM:", ""+bom.getBudget(), ""+bom.getBoxOffice());
			
		// Asiste a BOM dado que en ocasiones no tiene el budget
		WikiParser wiki = new WikiParser(cs.links.get(SitesManager.SITE_WIKI));
		wiki.execute();
		log("%s\t%s\t%s", "WIKI:", ""+wiki.getBudget(), ""+wiki.getBoxOffice());
		bom.setBudget(Math.max(bom.getBudget(), wiki.getBudget()));
		bom.setBoxOffice(Math.max(bom.getBoxOffice(), wiki.getBoxOffice()));	
	}
	
	
	
    protected static void log(String msg, String... vals) {
        Log.log(logContainer, String.format(msg, vals) + "\n");
    }
	
}
