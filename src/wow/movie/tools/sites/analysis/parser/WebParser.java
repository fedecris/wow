package wow.movie.tools.sites.analysis.parser;



public abstract class WebParser {
	
	/** URL a parsear */
	protected String url;
	
	/** Nombre del parser */
	protected String name;
	
	public WebParser(String url) {
		this.url = url;
	}
	
	protected float publicScore = -1;
	protected long publicCount = -1;
	protected float criticsScore = -1;
	protected long criticsCount = -1;
	protected long budget = -1;
	protected long boxOffice = -1;
	protected String director = "";
	
	public float getPublicScore() {
		return publicScore;
	}

	public long getPublicCount() {
		return publicCount;
	}

	public float getCriticsScore() {
		return criticsScore;
	}

	public long getCriticsCount() {
		return criticsCount;
	}
	
	public long getBudget() {
		return budget;
	}
	
	public long getBoxOffice() {
		return boxOffice;
	}
	
	public String getDirector() {
		return director;
	}
	
	public String getURL() {
		return url;
	}
	
	public String getName() {
		return name;
	}
	
	/** Procesado a realizar ad-hoc segun parser */
	protected abstract void load() throws Exception;
	
	/** Recuperar la información remota */
	public void execute() {
		try {			
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

}
