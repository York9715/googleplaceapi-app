import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.socan.spring.service.BusinessUnitsFromApiService;
import com.socan.spring.util.GoogleApiUtil;

import se.walkercrou.places.*;

public class GoogleApiClientApp {
	
	private static final String API_KEY_FILE_NAME = "places_api.key";
	
	
	public static void main(String[] args) {
		
		GoogleApiClient client= new GoogleApiClient();
		client.run();
	}
	
	public static void run() {
		System.out.println(new Date());
		GooglePlaces client =null;

		double radius=1000;	//5 kilometer
		
		try{
			InputStream in = GoogleApiClientApp.class.getResourceAsStream("/" + API_KEY_FILE_NAME);
			if (in == null) {
				throw new RuntimeException("API key not found.");
			}
			
			client = new GooglePlaces(IOUtils.toString(in));
			client.setDebugModeEnabled(true);
			//client = new GooglePlaces("AIzaSyCc_txmCyClsXAQNGVTXG-rnGVVZecs254");

		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		
		String postCode="M3B2S6";
    	Location location = client.getLocationByPostCode(postCode);
    	if ( location ==  null) {
    		System.out.println("null");
    	}
		List<Place> places = client.getNearbySocanRelativePlacesByPostCode(postCode, radius);

		Place empireStateBuilding = null;
		int idx=0;
		for (Place place : places) {
			idx++;
			String name=place.getName();
	

			
	        empireStateBuilding = place;
	        Place detailedEmpireStateBuilding = empireStateBuilding.getDetails(); // sends a GET request for more details
	        
		    //System.out.println("ID: " + detailedEmpireStateBuilding.getId());
		    System.out.println("Name(" + idx + "): " + detailedEmpireStateBuilding.getName());
		    System.out.println("Phone: " + detailedEmpireStateBuilding.getPhoneNumber());
		    System.out.println("International Phone: " + empireStateBuilding.getInternationalPhoneNumber());
		    System.out.println("Website: " + detailedEmpireStateBuilding.getWebsite());
		    //System.out.println("Always Opened: " + detailedEmpireStateBuilding.isAlwaysOpened());
		    System.out.println("Status: " + detailedEmpireStateBuilding.getStatus());
		    System.out.println("Google Place URL: " + detailedEmpireStateBuilding.getGoogleUrl());
		    //System.out.println("Price: " + detailedEmpireStateBuilding.getPrice());
		    System.out.println("Address: " + detailedEmpireStateBuilding.getAddress());
		    //System.out.println("Vicinity: " + detailedEmpireStateBuilding.getVicinity());
		    //System.out.println("Reviews: " + detailedEmpireStateBuilding.getReviews().size());
		    //System.out.println("Hours:\n " + detailedEmpireStateBuilding.getHours());
		    System.out.println("\n");
		    
		    
		}

	}
}