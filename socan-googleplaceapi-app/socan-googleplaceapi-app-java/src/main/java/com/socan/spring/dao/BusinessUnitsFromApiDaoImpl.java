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
import com.socan.spring.model.GeneralLicensees;

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
	
	public List<BusinessUnitsFromApi> findByNameAndAddressAndVicinity(String name, String address, String vicinity){
		if (name==null || name.isEmpty()) return null;
		
		if (vicinity.isEmpty() && address.isEmpty() ) return null;
			
		String hql = "from BusinessUnitsFromApi where name=:name";
		
		if (vicinity != null)
			hql += " and vicinity=:vicinity";
		else
			hql += " and vicinity is null";
	
		if (address != null)
			hql += " and address=:address";
		else
			hql += " and address is null";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("name", name);
		
		if (vicinity != null)
			query.setParameter("vicinity", vicinity);

		if (address != null)
			query.setParameter("address", address);
		
		List<BusinessUnitsFromApi> listGeneralLicensees = query.list();
		if (listGeneralLicensees !=null && listGeneralLicensees.size() >0 ) return listGeneralLicensees;
		
		return null;
	}
	
	public void updateBusinessUnitsFromApi(BusinessUnitsFromApi businessUnitsFromApi){
		getSession().update(businessUnitsFromApi);
	}
	
}
