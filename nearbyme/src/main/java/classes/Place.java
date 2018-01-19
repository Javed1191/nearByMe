package classes;

import java.io.Serializable;

public class Place implements Serializable {
	public String atm_id, short_atm_name;
	public String base_branch_name;
	public String address_1;
	public String address_2;
	public String address_3;
	public String area;
	public String pincode;
	public String district;
	public String city;
	public String state;
	public String lat;
	public String log;
	public String distance;

	public String getAtm_id() {
		return atm_id;
	}

	public void setAtm_id(String atm_id) {
		this.atm_id = atm_id;
	}

	public String getShort_atm_name() {
		return short_atm_name;
	}

	public void setShort_atm_name(String short_atm_name) {
		this.short_atm_name = short_atm_name;
	}

	public String getBase_branch_name() {
		return base_branch_name;
	}

	public void setBase_branch_name(String base_branch_name) {
		this.base_branch_name = base_branch_name;
	}

	public String getAddress_1() {
		return address_1;
	}

	public void setAddress_1(String address_1) {
		this.address_1 = address_1;
	}

	public String getAddress_2() {
		return address_2;
	}

	public void setAddress_2(String address_2) {
		this.address_2 = address_2;
	}

	public String getAddress_3() {
		return address_3;
	}

	public void setAddress_3(String address_3) {
		this.address_3 = address_3;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
}
