package com.masa.notifier;

import android.content.Intent;

public class Email {
	
	private String sendTo;
	private String subject;
	private String text;
	private String firstName;

	public Email(String sendTo, String subject, String text, String firstName) {
		this.sendTo = sendTo;
		this.subject = subject;
		this.text = text;
		this.firstName = firstName;
	}

	public Intent createIntent() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[] {sendTo});
		i.putExtra(Intent.EXTRA_SUBJECT, subject);
		i.putExtra(Intent.EXTRA_TEXT   , text);
		return i;
	}

	public String getFirstName() {
		return firstName;
	}

}
