package com.socan.spring.util;

import javax.persistence.Column;

import com.socan.spring.model.BusinessUnitsFromApi;

import se.walkercrou.places.Place;

public class GoogleApiUtil {
	public static BusinessUnitsFromApi setBusinessUnitsFromApi(BusinessUnitsFromApi businessUnit,Place place){
		if (businessUnit==null)
			businessUnit= new BusinessUnitsFromApi();
		if (place==null)
			return businessUnit;
	
		if (place.getName()!=null) businessUnit.setName(place.getName());
		if (place.getPhoneNumber()!=null) businessUnit.setPhone(place.getPhoneNumber());
		if (place.getInternationalPhoneNumber()!=null) businessUnit.setInternationalPhone(place.getInternationalPhoneNumber());
		if (place.getWebsite()!=null) businessUnit.setWebsite(place.getWebsite());
		businessUnit.setAlwaysPpened(Boolean.toString(place.isAlwaysOpened()));
		//if (place.getStatus()!=null) businessUnit.setStatus(place.getStatus());
		if (place.getGoogleUrl()!=null) businessUnit.setGooglePlaceUrl(place.getGoogleUrl());
		if (place.getPrice()!=null) businessUnit.setPrice(place.getPrice().toString());
		if (place.getAddress()!=null) businessUnit.setAddress(place.getAddress());
		if (place.getVicinity()!=null) businessUnit.setVicinity(place.getVicinity());
		if (place.getReviews()!=null) businessUnit.setReviews("");
		if (place.getHours()!=null) businessUnit.setHours((place.getHours()).toString());
		
		return businessUnit;
	}
}
