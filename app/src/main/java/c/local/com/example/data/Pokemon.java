package c.local.com.example.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Pokemon {

	private int id;
	private String name;
	private String url;
	private String  date;

	public Pokemon(int id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh時mm分ss秒SSSミリ秒 ");
		this.date = sdf.format(calendar.getTime());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
