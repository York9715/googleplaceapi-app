package com.socan.spring.dao;

import java.util.List;

import com.socan.spring.model.*;

public interface MusicSubscriberDao {
	void saveMusicSubscriber(MusicSubscriber musicSubscriber);
	
	List<MusicSubscriber> findAllMusicSubscriber();
		
	List<MusicSubscriber> findByCustomerName(String customerName);
	
	//void updateMusicSubscriber(MusicSubscriber musicSubscriber);
}
