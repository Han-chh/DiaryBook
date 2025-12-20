package com.diarybook.main;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Diary implements Serializable, Comparable<Diary>{  //每篇单独日记对象
	/**
	 * 
	 */
	private static final long serialVersionUID = 5378749505949790358L;
	private LocalDate date;
	private String weekday;
	private String theme;
	private String mood;
	private String weather;
	private String location;
	private String content;
	private final String uid = UUID.randomUUID().toString().replace("-", "");
	


	@Override
	public int compareTo(Diary other) {
		int cmp = this.date.compareTo(other.date);
		if (cmp != 0) {
			return cmp;
		}
		cmp = this.theme.compareTo(other.theme);
		return cmp;
	}

	public Diary(LocalDate date,String weekday,String theme,String mood,String weather,String location,String content) {
		System.out.println("fff");
		this.content = content;
		this.date = date;
		this.location = location;
		this.mood = mood;
		this.theme = theme;
		this.weather= weather;
		this.weekday = weekday;
	}
	
	public Diary() {
		this.content = null;
		this.date = null;
		this.location = null;
		this.mood = null;
		this.theme = null;
		this.weather= null;
		this.weekday = null;
	}
	
	
	//setter getter
	public void setAll(LocalDate date,String weekday,
			String theme,String mood,String weather,String location,String content){
		this.content = content;
		this.date = date;
		this.location = location;
		this.mood = mood;
		this.theme = theme;
		this.weather= weather;
		this.weekday = weekday;
	}
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getWeather() {
		return weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public String getUid() {
		return uid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, date, location, mood, theme, uid, weather, weekday);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Diary other = (Diary) obj;
		return Objects.equals(content, other.content) && Objects.equals(date, other.date)
				&& Objects.equals(location, other.location) && Objects.equals(mood, other.mood)
				&& Objects.equals(theme, other.theme)&& Objects.equals(weather, other.weather) && Objects.equals(weekday, other.weekday);
	}

	@Override
	public String toString() {
		return date + " 主题《" + theme +"》"+"  心情："+mood + " 天气："
				+ weather + " 地点：" + location+" "+ weekday;
	}
}
