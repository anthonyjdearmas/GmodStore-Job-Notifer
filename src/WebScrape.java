import java.awt.Desktop;
import java.awt.EventQueue;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/** @author Anthony DeArmas **/

public class WebScrape {
	
	/** The URL we are web scraping. It will always be the GmodStore Job Market.*/
	final static String url = "https://www.gmodstore.com/jobmarket";
	
	/** Document field used to web scrape data.*/
	static Document doc = new Document("");
	
	/** Latest Job ID.
	 *  Gets changed when getLatestID() is called*/
	static Integer id_latest = 0;
	
	/** Access GmodStore url and store it into doc field once.*/
	public static void main(String[] args) {
        try {
        	final Document gmodstore= Jsoup.connect(url).get();
        	doc = gmodstore;        	        	
        } catch (Exception e) {
            e.printStackTrace();
        }   
	}
	
	/** Return the website Document of a job given a valid job ID.
	 *  Note: A valid job ID is a job that exists on the GmodStore already
	 * 
	 *  Precondition: ID must be less than or equal to latest job ID.*/
	public static Document getJob(int ID) {
		assert ID <= (int) getLatestID();
		String job_url = "https://www.gmodstore.com/jobmarket/jobs/"+ID;
		Document theJob = new Document("");
		try {
			final Document gmodstore_job = Jsoup.connect(job_url).get();
			theJob = gmodstore_job;
		} catch (Exception e) {
			getJob(ID -1);
		}
		return theJob;
	}
	
	
	/** Returns ID of the latest job.*/
    public static Integer getLatestID() {
        try {
            final Document doc= Jsoup.connect(url).get();

            Element recent_listing= doc.select("a.item-listing__link").first();
            String recent_link= recent_listing.attr("href");
            Integer id_value = Integer.parseInt(recent_link.substring(recent_link.lastIndexOf('/') + 1));
            
            if (id_value != id_latest) {
            	id_latest = id_value;
            }
        } catch (Exception e) {
            return -1;
        }
        return id_latest;
    }
    
    /** Opens a job in your default browser.*/
    public static void OpenJob(int ID) {
    	String job_url = "https://www.gmodstore.com/jobmarket/jobs/" + ID;
    	
        try {
            Desktop.getDesktop().browse(new URI(job_url));
        } catch (Exception e) {
    		OpenJob(ID - 1);
        }
    	
    }
    
    /** Returns the next valid ID that comes after the given ID.
     * 
     * It gauges whether or not the next ID is valid by pining the job url.
     * If going to the job results in an exception, it will deem the ID to be invalid.*/
    public static int getNextID(int ID) {
    	assert ID <= getLatestID();
    	
    	String ping_url = "https://www.gmodstore.com/jobmarket/jobs/" + (ID - 1);
    	try {
    		final Document gmodstore_job = Jsoup.connect(ping_url).get();
    	} catch (Exception e) {
    		return getNextID(ID - 1);
    	}
    	return (ID - 1);
    }
    
    // Getters past this point are used to get information about specific jobs other than the latest job
    /**Returns a job's name*/
    public static String getJobName(int ID) {
    	Document selected_job = getJob(ID);
    	String jobname = "";
    	try {
    		String job_name_fetch = selected_job.select("h1").first().text();
    		jobname = job_name_fetch.substring(4, job_name_fetch.length());
	    } catch (Exception e) {
	    	getJobName(ID - 1);
	    }
    	
    	return jobname;
    }   
    
    /** Returns a job's budget*/
    public static String getJobBudget(int ID) {
    	Document selected_job = getJob(ID);
    	String budget = "";
    	try {
    		budget = selected_job.select("div.card-text").first().text();
	    } catch (Exception e) {
    		getJobBudget(ID - 1);
	    }
    	
    	return budget;
    }
    
    /** Returns a job's category*/
    public static String getJobCategory(int ID) {
    	Document selected_job = getJob(ID);
    	String category = "";
    	try {
    		category = selected_job.select("div.card-text").get(1).text();
	    } catch (Exception e) {
	    	getJobCategory(ID - 1);
	    }
    	
    	return category;
    } 
    
    /** Returns a job's number of applicants*/
    public static String getJobApplicants(int ID) {
    	Document selected_job = getJob(ID);
    	String applicants = "";
    	try {
    		applicants = selected_job.select("div.card-text").get(2).text();
	    } catch (Exception e) {
	    	getJobApplicants(ID - 1);
	    }
    	
    	return applicants;
    } 
    
    /** Returns a job's number of views*/
    public static String getJobViews(int ID) {
    	Document selected_job = getJob(ID);
    	String views = "";
    	try {
    		views = selected_job.select("div.card-text").get(3).text();
	    } catch (Exception e) {
	    	getJobViews(ID - 1);
	    }
    	
    	return views;
    }   
    
    /** Returns a job's due date*/
    public static String getJobDate(int ID) {
    	Document selected_job = getJob(ID);
    	String date = "";
    	try {
    		Element duedate_fetch = selected_job.select("div.job__duedate__text").first();
    		String duedate_euro_str = duedate_fetch.toString();
    		String duedate_euro = duedate_euro_str.substring(52, 71);
    		date = duedate_euro.substring(5, 7) + "/" + duedate_euro.substring(8, 10) + "/" + duedate_euro.substring(0, 4);
    		
	    } catch (Exception e) {
	    	getJobDate(ID -1);
	    }
    	
    	return date;
    }
    
    

    
}
