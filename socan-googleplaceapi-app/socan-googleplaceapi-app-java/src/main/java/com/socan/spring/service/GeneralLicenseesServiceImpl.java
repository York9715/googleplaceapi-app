package com.socan.spring.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socan.spring.dao.GeneralLicenseesDao;
import com.socan.spring.model.GeneralLicensees;

@Service("generalLicenseesService")
@Transactional
public class GeneralLicenseesServiceImpl implements GeneralLicenseesService   {
	
	@Autowired
	private GeneralLicenseesDao dao;
	
	public void saveGeneralLicensees(GeneralLicensees generalLicensees){
		dao.saveGeneralLicensees(generalLicensees);
	}

	public List<GeneralLicensees> findAllGeneralLicensees(){
		return dao.findAllGeneralLicensees();
	}

	public List<GeneralLicensees> findAllGeneralLicenseesByName(String name){
		return dao.findAllGeneralLicenseesByName(name);
	}

	
	public void deleteEmployeeById(long id){
		dao.deleteGeneralLicenseesById(id);
	}

	public GeneralLicensees findById(long id){
		return dao.findById(id);
	}

	public void updateGeneralLicensees(GeneralLicensees generalLicensees){
		dao.updateGeneralLicensees(generalLicensees);
	}

}
