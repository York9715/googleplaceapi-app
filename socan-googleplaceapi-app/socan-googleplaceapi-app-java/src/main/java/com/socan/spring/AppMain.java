package com.socan.spring;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.joda.time.LocalDate;
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
	
	public static void main(String args[]) {
		AppMain client= new AppMain();
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		client.dailyRun(context);
		//client.dbTest(null);
		context.close();
	}
	
	
	public static void dailyRun(AbstractApplicationContext context) {
		System.out.println(new Date());
		GooglePlaces client =null;
		double radius=1000;	//5 kilometer
		
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

			checkBusinessUnits(places,context);
		}
	}

	public static int checkBusinessUnits(List<Place> places,AbstractApplicationContext context) {
		
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
	
	
	public  void dbTest(String args[]) {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//GoogleApiUtil.setBusinessUnitsFromApi(b, place)
		
		GeneralLicenseesService service1 = (GeneralLicenseesService) context.getBean("generalLicenseesService");
		List<GeneralLicensees> generalLicensees=service1.findAllGeneralLicenseesByName("Mile House");
		System.out.println(generalLicensees);
				
		BusinessUnitsFromApiService businessUnitsFromApiService = (BusinessUnitsFromApiService) context.getBean("businessUnitsFromApiService");
		BusinessUnitsFromApi businessUnit= new BusinessUnitsFromApi();
	
		businessUnitsFromApiService.saveBusinessUnitsFromApi(businessUnit);
		
		BusinessUnitsFromApi businessUnit2= new BusinessUnitsFromApi();
		businessUnit2.setName("name22");
		businessUnitsFromApiService.saveBusinessUnitsFromApi(businessUnit2);
		
		
	}
}
