package com.socan.spring.dao;

import java.util.List;

import com.socan.spring.model.*;

public interface GeneralLicenseesDao {
	
	void saveGeneralLicensees(GeneralLicensees generalLicensees);
	
	List<GeneralLicensees> findAllGeneralLicensees();
	
	void deleteGeneralLicenseesById(long id);
	
	GeneralLicensees findById(long id);
	
	List<GeneralLicensees> findAllGeneralLicenseesByName(String name);
	
	void updateGeneralLicensees(GeneralLicensees generalLicensees);
}

