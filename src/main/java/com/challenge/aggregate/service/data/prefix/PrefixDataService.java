package com.challenge.aggregate.service.data.prefix;

import java.util.Optional;

public interface PrefixDataService {
	/**
	 * <p>Verify and retrieve a prefix phone number, it should belong a deterministic text file, otherwise Optional Empty</p>
	 *
	 * @param phoneNumber Valid Phone Number
	 * @return Valid Prefix
	 */
	Optional<String> prefixByPhoneNumber(String phoneNumber);
}
