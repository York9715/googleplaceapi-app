package com.socan.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.socan.spring.dao.*;
import com.socan.spring.model.*;

public class BusinessUnitsServiceImpl implements BusinessUnitsService{
	@Autowired
	private BusinessUnitsFromApiDao businessUnitsFromApiDao;
	
	@Autowired
	private GeneralLicenseesDao generalLicenseesDao;

	@Autowired
	private MusicSubscriberDao musicSubscriberDao;
	
	public boolean isLicensedBusinessUnit(BusinessUnitsFromApi businessUnitsFromApi){
		
		String name=businessUnitsFromApi.getName();
		String address =businessUnitsFromApi.getAddress();
		String phone =businessUnitsFromApi.getPhone();
		
		//1. Check SOCAN Music Subscriber Dataset_Masked Supplier.csv
		List<MusicSubscriber> listMusicSubscriber=musicSubscriberDao.findByCustomerName(name);
		
		if (listMusicSubscriber !=null && listMusicSubscriber.size() > 0) {
			return true;
		}
		
		//2. Check SOCAN General Licensees_Masked CustID.csv
		List<GeneralLicensees> listGeneralLicensees=generalLicenseesDao.findAllGeneralLicenseesByName(name);
		if (listGeneralLicensees !=null && listGeneralLicensees.size() > 0) {
			return true;
		}
		
		return false;
	}
	
}
