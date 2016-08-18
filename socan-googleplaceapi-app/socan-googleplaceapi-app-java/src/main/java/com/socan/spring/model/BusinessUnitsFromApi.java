package com.socan.spring.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="business_units_from_api")
//public class BusinessUnitsFromApi implements Serializable {
public class BusinessUnitsFromApi {
	static final long serialVersionUID=189912345l;
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String name;
    private String phone;

    @Column(name = "international_phone")
    private String internationalPhone;
    private String website;
    
    @Column(name = "always_opened")
    private String alwaysPpened;
    
    @Column(name = "google_place_url")
    private String googlePlaceUrl;
    
    private String price;
    private String address;
    private String vicinity;
    private String reviews;
    private String hours;
    
    @Column(name = "socan_licensed")
    private Boolean socanLicensed=false;
    
    @Column(name = "search_date")
    private Date searchDate;
    @Column(name = "updated_date")
    private Date updatedDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getInternationalPhone() {
		return internationalPhone;
	}
	public void setInternationalPhone(String internationalPhone) {
		this.internationalPhone = internationalPhone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getAlwaysPpened() {
		return alwaysPpened;
	}
	public void setAlwaysPpened(String alwaysPpened) {
		this.alwaysPpened = alwaysPpened;
	}
	public String getGooglePlaceUrl() {
		return googlePlaceUrl;
	}
	public void setGooglePlaceUrl(String googlePlaceUrl) {
		this.googlePlaceUrl = googlePlaceUrl;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getVicinity() {
		return vicinity;
	}
	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	public String getReviews() {
		return reviews;
	}
	public void setReviews(String reviews) {
		this.reviews = reviews;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}
	public Boolean getSocanLicensed() {
		return socanLicensed;
	}
	public void setSocanLicensed(Boolean socanLicensed) {
		this.socanLicensed = socanLicensed;
	}
	public Date getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(Date searchDate) {
		this.searchDate = searchDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
   
}
