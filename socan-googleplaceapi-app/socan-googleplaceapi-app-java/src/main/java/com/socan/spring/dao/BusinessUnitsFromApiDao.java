package com.socan.spring.dao;

import java.util.List;

import com.socan.spring.model.BusinessUnitsFromApi;

public interface BusinessUnitsFromApiDao {

	void saveBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi);
	
	List<BusinessUnitsFromApi> findAllBusinessUnitsFromApi();
	
	void deleteBusinessUnitsFromApiById(int id);
	
	BusinessUnitsFromApi findById(int ssn);
	
	void updateBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi);

}