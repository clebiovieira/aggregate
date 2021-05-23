package com.challenge.aggregate.service.data.bs;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.challenge.aggregate.service.domain.BusinessSector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BusinessSectorDataServiceImpl implements BusinessSectorDataService {
	private static final String BUSINESS_SECTOR_URL = "https://challenge-business-sector-api.meza.com/sector/%s";

	private final RestTemplate restTemplate;

	/**
	 * <p>Retrieve the Business Sector using Aggregate API</p>
	 *
	 * @return Optional<BusinessSector>
	 */
	public Optional<BusinessSector> businessSectorByPhoneNumber(String phoneNumber) {
		final Instant start = Instant.now();
		log.info("Searching Business Sector for {} ...: {}", phoneNumber, LocalDateTime.now());

		ResponseEntity<BusinessSector> businessSectorResponse =
				restTemplate.getForEntity(String.format(BUSINESS_SECTOR_URL, phoneNumber), BusinessSector.class);

		if (businessSectorResponse.getStatusCode().is2xxSuccessful()) {
			log.info("Business Sector found: {}  - Time Elapsed {}",
					businessSectorResponse.getBody().getSector(), Duration.between(start, Instant.now()).toMillis());
			return Optional.ofNullable(businessSectorResponse.getBody());
		}

		log.info("Business Sector found: - Time Elapsed {}", Duration.between(start, Instant.now()).toMillis());
		return Optional.empty();
	}

}
