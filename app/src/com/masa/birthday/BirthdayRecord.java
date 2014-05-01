package com.masa.birthday;

import java.util.Date;

import android.content.res.Resources;

import com.masa.notifier.Email;

public class BirthdayRecord {
	private static final int DAYS_BEFORE_REMINDER = 30;

	private String name;
	private Date birthday;
	private String photo;
	private String email;

	public BirthdayRecord(String name, Date birthday, String photo, String email) {
		this.name = name;
		this.birthday = birthday;
		this.photo = photo;
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
	
	public Email getEmail(Resources resources) {
		String subject = resources.getString(
				R.string.happy_birthday_email_title, getName());
		String text =  resources.getString(R.string.happy_birthday_email_text);
		return new Email(getEmail(), subject, text, getName());
	}

	public boolean shouldFireNotification() {
		return getDaysTillBirthday() < DAYS_BEFORE_REMINDER;
	}
	
	@SuppressWarnings("deprecation")
	public int getDaysTillBirthday() {
	  int daysFromYearStart = birthday.getMonth() * 30 + birthday.getDate();
	  int currentDaysFromYearStart = new Date().getMonth() * 30 + new Date().getDate();
	  int daysTillBirthday = daysFromYearStart > currentDaysFromYearStart ?
		  daysFromYearStart - currentDaysFromYearStart :
		  365 + daysFromYearStart - currentDaysFromYearStart;
      return daysTillBirthday;
	}
}
