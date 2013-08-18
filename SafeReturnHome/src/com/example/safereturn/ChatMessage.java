package com.example.safereturn;

public class ChatMessage {

	private int type;
	private String name = "정윤성";
	private String msg;
	private String timeFormat;
	private String dayFormat;
	
	ChatMessage(int type, String msg, String timeFormat) {
		this.type = type;
		this.msg = msg;
		this.timeFormat = timeFormat;
	}

	ChatMessage(int type, String dayFormat) {
		this.type = type;
		this.dayFormat = dayFormat;
	}

	int getType() {
		return type;
	}

	String getName() {
		return name;
	}

	String getMessage() {
		return msg;
	}
	
	String getTimeFormat() {
		return timeFormat;
	}
	
	String getDayFormat() {
		return dayFormat;
	}
}
