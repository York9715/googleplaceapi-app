package com.socan.spring.dao;

import java.util.List;

import javax.transaction.Transaction;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.socan.spring.model.BusinessUnitsFromApi;

@Repository("businessUnitsFromApiDao")
@Transactional
public class BusinessUnitsFromApiDaoImpl extends AbstractDao implements BusinessUnitsFromApiDao{
	
	public void saveBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi){
		persist(businessUnitsFromApi);
	}
	
	@SuppressWarnings("unchecked")
	public List<BusinessUnitsFromApi> findAllBusinessUnitsFromApi(){
		Criteria criteria = getSession().createCriteria(BusinessUnitsFromApi.class);
		return (List<BusinessUnitsFromApi>) criteria.list();
		
	}
	
	public void deleteBusinessUnitsFromApiById(int id){
		Query query = getSession().createSQLQuery("delete from BusinessUnitsFromApi where id = :id");
		query.setInteger("id", id);
		query.executeUpdate();
	}
	
	public BusinessUnitsFromApi findById(int id){
		Criteria criteria = getSession().createCriteria(BusinessUnitsFromApi.class);
		criteria.add(Restrictions.eq("id",id));
		return (BusinessUnitsFromApi) criteria.uniqueResult();
		
	}
	
	public void updateBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi){
		getSession().update(businessUnitsFromApi);
	}
	
}
