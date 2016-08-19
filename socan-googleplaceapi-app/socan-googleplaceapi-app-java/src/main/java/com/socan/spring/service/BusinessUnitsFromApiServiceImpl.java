package com.socan.spring.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socan.spring.dao.*;
import com.socan.spring.model.*;

@Service("businessUnitsFromApiService")
@Transactional
public class BusinessUnitsFromApiServiceImpl implements BusinessUnitsFromApiService{
	@Autowired
	private BusinessUnitsFromApiDao dao;

	@Autowired
	private BusinessUnitsService businessUnitsService;
	
	public void saveBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi){
		if (!businessUnitsService.hasBeenCheckedBefore(businessUnitsFromApi))
			dao.saveBusinessUnitsFromApi(businessUnitsFromApi);
		else
			System.out.println(businessUnitsFromApi.getName() + " has been checked before");
	}
	
	public List<BusinessUnitsFromApi> findAllBusinessUnitsFromApi(){
		return dao.findAllBusinessUnitsFromApi();
	}
	
	public void deleteBusinessUnitsFromApiById(int id){
		dao.deleteBusinessUnitsFromApiById(id);
	}
	
	public BusinessUnitsFromApi findById(int id){
		return dao.findById(id);
	}
	
	public void updateBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi){
		businessUnitsFromApi.setUpdatedDate(new Date());
		//businessUnitsFromApi.setSearchDate(new Date());
		dao.updateBusinessUnitsFromApi(businessUnitsFromApi);
	}
	
	
	public void addBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi){
		businessUnitsFromApi.setUpdatedDate(new Date());
		businessUnitsFromApi.setSearchDate(new Date());
		dao.updateBusinessUnitsFromApi(businessUnitsFromApi);
	}
		
}
