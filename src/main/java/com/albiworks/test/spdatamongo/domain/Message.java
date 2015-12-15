package com.albiworks.test.spdatamongo.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
/**
 * The domain object to store a message in the database
 * @author Alexandro Blanco <alex@albiworks.com>
 *
 */
public class Message {

	@Id
	private BigInteger id;
	
	private String message;
	
	private LocalDateTime postTime = LocalDateTime.now();
	
	private String author;

	public Message(){
		
	}
	
	public Message(String message) {
		super();
		this.message = message;
	}

	public BigInteger getId() {
		return id;
	}



	public void setId(BigInteger id) {
		this.id = id;
	}

	public Message(String message, String metaData) {
		super();
		this.message = message;
		this.author = metaData;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public LocalDateTime getPostTime() {
		return postTime;
	}
	
}
