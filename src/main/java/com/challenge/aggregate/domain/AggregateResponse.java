package com.challenge.aggregate.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({})
@NoArgsConstructor
@ToString
public class AggregateResponse {

	public AggregateResponse(String name, BusinessSectorCount value) {
		this.setProperty(name, value);
	}

	@JsonIgnore
	private final Map<String, BusinessSectorCount> properties = new ConcurrentHashMap<>();

	@JsonAnyGetter
	public Map<String, BusinessSectorCount> getProperties() {
		return this.properties;
	}

	@JsonAnySetter
	public void setProperty(String name, BusinessSectorCount value) {
		this.properties.put(name, value);
	}
}
