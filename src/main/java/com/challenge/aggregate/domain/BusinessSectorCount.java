package com.challenge.aggregate.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({})
@ToString
public class BusinessSectorCount {
	@JsonIgnore
	public BusinessSectorCount(String name, Double value) {
		this.setProperty(name, value);
	}

	@JsonIgnore
	private Map<String, Double> properties = new ConcurrentHashMap<>();

	@JsonAnyGetter
	public Map<String, Double> getProperties() {
		return this.properties;
	}

	@JsonAnySetter
	public void setProperty(String name, Double value) {
		this.properties.put(name, value);
	}
}
