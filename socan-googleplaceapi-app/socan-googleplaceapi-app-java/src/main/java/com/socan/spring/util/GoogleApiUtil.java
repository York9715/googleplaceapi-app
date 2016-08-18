package com.socan.spring.util;

import javax.persistence.Column;

import com.socan.spring.model.BusinessUnitsFromApi;

import se.walkercrou.places.Place;

public class GoogleApiUtil {
	public static BusinessUnitsFromApi setBusinessUnitsFromApi(BusinessUnitsFromApi businessUnit,Place place){
		if (businessUnit==null)
			businessUnit= new BusinessUnitsFromApi();
	
		businessUnit.setName(place.getName());
		businessUnit.setPhone(place.getPhoneNumber());
		businessUnit.setInternationalPhone(place.getInternationalPhoneNumber());
		businessUnit.setWebsite(place.getWebsite());
		businessUnit.setAlwaysPpened(Boolean.toString(place.isAlwaysOpened()));
		//businessUnit.setStatus(place.getStatus());
		businessUnit.setGooglePlaceUrl(place.getGoogleUrl());
		businessUnit.setPrice(place.getPrice().toString());
		businessUnit.setAddress(place.getAddress());
		businessUnit.setVicinity(place.getVicinity());
		businessUnit.setReviews("");
		businessUnit.setHours((place.getHours()).toString());
		
		return businessUnit;
	}
}
