package com.challenge.aggregate.service;

import java.util.List;

import com.challenge.aggregate.domain.AggregateResponse;

public interface AggregateService {
	AggregateResponse aggregate(List<String> phoneNumbers);
}
