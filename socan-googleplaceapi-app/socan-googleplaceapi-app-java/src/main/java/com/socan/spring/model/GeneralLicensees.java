package com.socan.spring.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="general_licensees")
public class GeneralLicensees {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(name = "masked_id", nullable = false)
    private int maskedId;
    private String name;
    private String sic;
    
    @Column(name = "full_description")
    private String fullDescription;
    private String address;
    private String address2;
    private String address3;
    private String city;
    
    @Column(name = "state_prov")
    private String stateProv;
    
    @Column(name = "post_code")
    private String postCode;
    private String country;    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public int getMaskedId() {
		return maskedId;
	}
	public void setMaskedId(int maskedId) {
		this.maskedId = maskedId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSic() {
		return sic;
	}
	public void setSic(String sic) {
		this.sic = sic;
	}
	public String getFullDescription() {
		return fullDescription;
	}
	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateProv() {
		return stateProv;
	}
	public void setStateProv(String stateProv) {
		this.stateProv = stateProv;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}    
	
}
