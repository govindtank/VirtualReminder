package com.example.virtualreminder;

public class Reminder {
	
	private Integer event_id;
	private String event_text;
	private String date;
	private String time;
	private String video;
	private String recorder;
	private Integer time_int;
	
	public Reminder(Integer event_id, String event_text, String date,
			String time, String video, String recorder, Integer time_int) {
		super();
		this.event_id = event_id;
		this.event_text = event_text;
		this.date = date;
		this.time = time;
		this.video = video;
		this.recorder = recorder;
		this.time_int = time_int;
	}


	public Reminder(String event_text, String date, String time, String video,
			String recorder, Integer time_int) {
		super();
		this.event_text = event_text;
		this.date = date;
		this.time = time;
		this.video = video;
		this.recorder = recorder;
		this.time_int = time_int;
	}


	@Override
	public String toString() {
		return "Reminder [event_id=" + event_id + ", event_text=" + event_text
				+ ", date=" + date + ", time=" + time + ", video=" + video
				+ ", recorder=" + recorder + ", time_int=" + time_int + "]";
	}

	public Integer getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}
	public String getEvent_text() {
		return event_text;
	}
	public void setEvent_text(String event_text) {
		this.event_text = event_text;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getRecorder() {
		return recorder;
	}
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}


	public int getTime_int() {
		return time_int;
	}


	public void setTime_int(int time_int) {
		this.time_int = time_int;
	}

	
}
