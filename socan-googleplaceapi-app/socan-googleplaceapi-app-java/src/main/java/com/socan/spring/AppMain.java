package com.socan.spring;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Location;
import se.walkercrou.places.Place;

import com.socan.spring.configuration.AppConfig;
import com.socan.spring.model.*;
import com.socan.spring.service.*;
import com.socan.spring.util.GoogleApiUtil;

public class AppMain {
	
	private static final String API_KEY_FILE_NAME = "places_api.key";
	
	AbstractApplicationContext context;
	
    public AppMain() {
    	context = new AnnotationConfigApplicationContext(AppConfig.class);
    }

    
	public static void main(String args[]) {
		AppMain client= new AppMain();		
		client.dailyRun();	
	}
	
	
	@Override
    protected void finalize() throws Throwable {
        try{
        	context.close();
        }catch(Throwable t){
            throw t;
        }finally{
            System.out.println("Calling finalize of Super Class");
            super.finalize();
        }      
    }

	
	public void dailyRun() {
		
		System.out.println(new Date());
		GooglePlaces client =null;
		double radius=5000;	//5 kilometer
		
		//Create GooglePlaces instance
		try{			
			InputStream in = AppMain.class.getResourceAsStream("/" + API_KEY_FILE_NAME);
			
			if (in == null) 
				throw new RuntimeException("API key not found.");
						
			client = new GooglePlaces(IOUtils.toString(in));
			client.setDebugModeEnabled(false);
			//client = new GooglePlaces("AIzaSyCc_txmCyClsXAQNGVTXG-rnGVVZecs254");

		}catch(Exception e){
			e.printStackTrace();
			return ;
		}
		
		//Get post code list
		String sPostCodes="M3B2S6;M2H3H1";
		String[] postCodes=sPostCodes.split(";");
		
		//Search each post code
		for(String postCode : postCodes) {
			
			Location location = client.getLocationByPostCode(postCode);
			if ( location ==  null) {
				System.out.println("null");
			}
			List<Place> places = client.getNearbySocanRelativePlacesByPostCode(postCode, radius);

			checkBusinessUnits(places);
		}		
		
	}

	public int checkBusinessUnits(List<Place> places) {	
		
		GeneralLicenseesService generalLicenseesService = (GeneralLicenseesService) context.getBean("generalLicenseesService");
		BusinessUnitsFromApiService businessUnitsFromApiService = (BusinessUnitsFromApiService) context.getBean("businessUnitsFromApiService");
		BusinessUnitsService  businessUnitsService = (BusinessUnitsService)context.getBean("businessUnitsService");
		
		Place empireStateBuilding = null;
		int idx=0;
		
		for (Place place : places) {			
			idx++;			
			BusinessUnitsFromApi businessUnitsFromApi= new BusinessUnitsFromApi();
			GoogleApiUtil.setBusinessUnitsFromApi(businessUnitsFromApi, place);
			
			if (businessUnitsService.isLicensedBusinessUnit(businessUnitsFromApi))	
				businessUnitsFromApi.setSocanLicensed(true);
			else
				businessUnitsFromApi.setSocanLicensed(false);
			
			//persist to database
			businessUnitsFromApiService.saveBusinessUnitsFromApi(businessUnitsFromApi);	
						
		}
		
		return idx;
	}		

}
