package com.travelsky.utils;

import java.util.List;




public class ListItem implements AbstractItem<ListItem> {
	private String title;
	private String date;
	private int publicity;
	private int  play;
	private List<ListItem> children;

	
	public ListItem(int publicity,String title,String date,int play){
		this.publicity = publicity;
		this.title = title;
		this.date = date;
		this.play = play;
	}
	
	
	
	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getDate() {
		return date;
	}



	public void setDate(String date) {
		this.date = date;
	}



	public int getPublicity() {
		return publicity;
	}



	public void setPublicity(int publicity) {
		this.publicity = publicity;
	}



	public int getPlay() {
		return play;
	}



	public void setPlay(int play) {
		this.play = play;
	}



	@Override
	public List<ListItem> getChildren() {
		return children;
	}

	public void setChildren(List<ListItem> children) {
		this.children = children;
	}
}
