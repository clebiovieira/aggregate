package com.challenge.aggregate.resource.rest.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.challenge.aggregate.domain.AggregateResponse;
import com.challenge.aggregate.service.AggregateService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Api(value = "Aggregate Resource", tags = {"Aggregate"})
public class AggregateResourceImpl {
	private static final String ERROR_AGGREGATE = "Error found, could not possible aggregate.";
	private final AggregateService aggregateService;

	@ApiOperation(value = "Provide a set of aggregated Prefix and Business Sector", tags = {"Aggregate"})
	@PostMapping(value = "/aggregate", consumes = "application/json")
	public AggregateResponse aggregate(@RequestBody List<String> telephones) {
		try {
			return aggregateService.aggregate(telephones);
		}
		catch (RuntimeException e) {
			log.error(ERROR_AGGREGATE);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_AGGREGATE, e);
		}
	}

}
