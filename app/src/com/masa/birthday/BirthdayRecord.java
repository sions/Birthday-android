package com.masa.birthday;

import java.util.Date;

import android.os.Bundle;

public class BirthdayRecord {
	private String name;
	private Date birthday;
	private String photo;
	private String email;

	public BirthdayRecord(String name, Date birthday, String photo, String email) {
		this.name = name;
		this.birthday = birthday;
		this.photo = photo;
		this.email = email;
	}
	
	public BirthdayRecord(Bundle bundle) {
		this.name = bundle.getString("name");
		this.birthday = new Date();//(Date)bundle.get("birthday");
		this.photo = "";//bundle.getString("photo");
		this.email = bundle.getString("email");
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}
}
