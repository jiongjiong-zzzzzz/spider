package net.kernal.spiderman.kit;

import java.io.Serializable;

public class Seed implements Serializable {

	private static final long serialVersionUID = 7069409527143878213L;
	
	private String name;
	private String url;
	public Seed(String name, String url) {
		this.name = name;
		this.url = url;
	}
	
	public Seed(String url) {
		this(url, url);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getUrl() {
		return this.url;
	}

	public String toString() {
		return "Seed [name=" + name + ", url=" + url + "]";
	}
	
}
