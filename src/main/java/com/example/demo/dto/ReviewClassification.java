package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewClassification {

	public enum Sentiment {
		POSITIVE,
		NEUTRAL,
		NEGATIVE
	}

	private String review;
	private Sentiment classification;
}
