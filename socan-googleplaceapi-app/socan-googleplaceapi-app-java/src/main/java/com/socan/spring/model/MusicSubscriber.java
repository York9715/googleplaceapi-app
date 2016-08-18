package com.socan.spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="music_subscriber")
public class MusicSubscriber {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "supplier")
    private int supplier;
    
    @Column(name = "customer_name")
    private String customerName;
        
    private String address1;
    private String city;
    private String provive;
    private String pc;
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getSupplier() {
		return supplier;
	}
	public void setSupplier(int supplier) {
		this.supplier = supplier;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvive() {
		return provive;
	}
	public void setProvive(String provive) {
		this.provive = provive;
	}
	public String getPc() {
		return pc;
	}
	public void setPc(String pc) {
		this.pc = pc;
	}
    
}
