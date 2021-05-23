package com.challenge.aggregate.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.aggregate.domain.AggregateResponse;
import com.challenge.aggregate.domain.BusinessSectorCount;
import com.challenge.aggregate.service.data.prefix.PrefixDataService;
import com.challenge.aggregate.service.data.bs.BusinessSectorDataService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AggregateServiceImpl implements AggregateService {
	private final BusinessSectorDataService businessSectorDataService;
	private final PrefixDataService prefixDataService;

	public AggregateResponse aggregate(List<String> phoneNumbers) {
		final Double INITIAL_COUNT = 1.0;
		AggregateResponse aggregateResponse = new AggregateResponse();

		phoneNumbers.stream()
				.map(this::normalizePhoneNumber)
				.filter(this::isValidPhoneNumber)
				.forEach(phoneNumber ->
						prefixDataService.prefixByPhoneNumber(phoneNumber)
								.ifPresent(prefix ->
										businessSectorDataService.businessSectorByPhoneNumber(phoneNumber)
												.ifPresent(businessSector -> {

													BusinessSectorCount bsc = aggregateResponse.getProperties().get(prefix);

													// When there is no prefix found, initialize the counter
													if (bsc == null) {
														BusinessSectorCount sectorCount =
																new BusinessSectorCount(businessSector.getSector(), INITIAL_COUNT);
														aggregateResponse.setProperty(prefix, sectorCount);
													} else {
														// Same sector under same prefix
														if (bsc.getProperties().get(businessSector.getSector()) != null) {
															bsc.getProperties().put(businessSector.getSector(),
																	bsc.getProperties().get(businessSector.getSector()) + 1);
														} else {
															// A new sector under same prefix
															bsc.setProperty(businessSector.getSector(), INITIAL_COUNT);
														}
													}
												})
								)
				);

		log.info(String.format("Aggregated phones %s", aggregateResponse.getProperties().toString()));
		return aggregateResponse;

	}

	/**
	 * <p>Support function to verify the eligibility of a phone number, taking account of the given rules.
	 * A number is considered valid if it contains only digits, an optional leading `+` and
	 * whitespace anywhere except immediately after the `+`. A valid number has exactly
	 * 3 digits or more than 6 and less than 13. `00` is acceptable as replacement for
	 * the leading `+`</p>
	 * <p>For the sake of rule, '+' and 00 have the exactly meaning<p/>
	 *
	 * @param phoneNumber will be cleaned and normalized
	 * @return clean phone number without whitespaces and leading characters
	 */
	boolean isValidPhoneNumber(String phoneNumber) {
		if (Pattern.compile("^(\\+|0{2})(\\d)").matcher(phoneNumber).find()) {
			return Pattern.compile("(^(\\+|0{2})\\d)(((\\s?\\d){2})$|((\\s?\\d){6,11})$)").matcher(phoneNumber).find();
		} else {
			return Pattern.compile("(^((\\s?\\d){3})$|^((\\s?\\d){7,12})$)").matcher(normalizePhoneNumber(phoneNumber)).find();
		}
	}

	/**
	 * <p>Support function to cleanup characters(Leandinf, whitespaces) from phone number.</p>
	 *
	 * @param phoneNumber will be cleaned and normalized
	 * @return clean phone number without whitespaces and leading characters
	 */
	String normalizePhoneNumber(String phoneNumber) {
		String normalizedPhone = phoneNumber.replaceAll("\\s", "");

		if (normalizedPhone.startsWith("+")) {
			normalizedPhone = normalizedPhone.replaceFirst("\\+", "");
		} else if (normalizedPhone.startsWith("00")) {
			normalizedPhone = normalizedPhone.replaceFirst("00", "");
		}
		return normalizedPhone;
	}
}
