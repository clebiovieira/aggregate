package com.challenge.aggregate.service.data.bs;

import java.util.Optional;

import com.challenge.aggregate.service.domain.BusinessSector;

public interface BusinessSectorDataService {
	/**
	 * <p>Retrieve the Business Sector using Aggregate API</p>
	 *
	 * @return Optional<BusinessSector>
	 */
	Optional<BusinessSector> businessSectorByPhoneNumber(String phoneNumber);
}
