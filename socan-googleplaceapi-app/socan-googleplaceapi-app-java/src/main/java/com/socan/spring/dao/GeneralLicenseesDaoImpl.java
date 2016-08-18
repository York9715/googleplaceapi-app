package com.socan.spring.dao;

import java.util.List;

import com.socan.spring.model.*;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("generalLicenseesDao")
public class GeneralLicenseesDaoImpl extends AbstractDao implements GeneralLicenseesDao{

	public void saveGeneralLicensees(GeneralLicensees generalLicensees){
		persist(generalLicensees);
	}

	@SuppressWarnings("unchecked")
	public List<GeneralLicensees> findAllGeneralLicensees() {
		Criteria criteria = getSession().createCriteria(GeneralLicensees.class);
		return (List<GeneralLicensees>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<GeneralLicensees> findAllGeneralLicenseesByName(String name) {
		String hql = "from GeneralLicensees where Name like :name";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", "%" + name + "%");
		 
		List<GeneralLicensees> listGeneralLicensees = query.list();
		 
		return listGeneralLicensees;
	}
		
	
	public void deleteGeneralLicenseesById(long id) {
		Query query = getSession().createSQLQuery("delete from GeneralLicensees where id = :id");
		query.setLong("id", id);
		query.executeUpdate();
	}
	
	public GeneralLicensees findById(long id){
		return (GeneralLicensees)(getSession().get(GeneralLicensees.class,id));
	}
	
	public void updateGeneralLicensees(GeneralLicensees generalLicensees){
		getSession().update(generalLicensees);
	}
	
}
