package com.socan.spring.service;

import java.util.List;

import com.socan.spring.model.GeneralLicensees;

public interface GeneralLicenseesService {
	void saveGeneralLicensees(GeneralLicensees generalLicensees);

	List<GeneralLicensees> findAllGeneralLicensees();

	List<GeneralLicensees> findAllGeneralLicenseesByName(String name);
	
	void deleteEmployeeById(long id);

	GeneralLicensees findById(long id);

	void updateGeneralLicensees(GeneralLicensees generalLicensees);
	
}
