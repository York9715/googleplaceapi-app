package com.socan.spring.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.socan.spring.model.MusicSubscriber;

@Repository("musicSubscriberDao")
@Transactional
public class MusicSubscriberDaoImpl extends AbstractDao implements MusicSubscriberDao{
	public void saveMusicSubscriber(MusicSubscriber musicSubscriber){
		persist(musicSubscriber);
	}
	
	@SuppressWarnings("unchecked")
	public List<MusicSubscriber> findAllMusicSubscriber(){
		Criteria criteria = getSession().createCriteria(MusicSubscriber.class);
		return (List<MusicSubscriber>) criteria.list();

	}
		
	@SuppressWarnings("unchecked")
	public List<MusicSubscriber> findByCustomerName(String customerName){
		String hql = "from MusicSubscriber where customerName like :customerName";
		Query query = getSession().createQuery(hql);
		query.setParameter("customerName", "%" + customerName + "%");
		 
		List<MusicSubscriber> listMusicSubscribers = query.list();
		 
		return listMusicSubscribers;
	}
	
}
