package com.socan.spring.service;

import java.util.List;
import com.socan.spring.model.BusinessUnitsFromApi;

public interface BusinessUnitsFromApiService {
	void saveBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi);
	
	List<BusinessUnitsFromApi> findAllBusinessUnitsFromApi();
	
	void deleteBusinessUnitsFromApiById(int id);
	
	BusinessUnitsFromApi findById(int id);
	
	void updateBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi);
	
}
