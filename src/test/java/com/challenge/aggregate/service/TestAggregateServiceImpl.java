package com.challenge.aggregate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenge.aggregate.service.data.prefix.PrefixDataService;
import com.challenge.aggregate.domain.AggregateResponse;
import com.challenge.aggregate.domain.BusinessSectorCount;
import com.challenge.aggregate.service.data.bs.BusinessSectorDataService;
import com.challenge.aggregate.service.domain.BusinessSector;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test AggregateServiceImpl")
class TestAggregateServiceImpl {
	@Mock
	private BusinessSectorDataService businessSectorDataService;
	@Mock
	private PrefixDataService prefixDataService;

	@InjectMocks
	private AggregateServiceImpl aggregateService;

	@DisplayName("testNormalizePhoneNumber")
	@ParameterizedTest(name = "#{index} - phone: {0} | expected: {1}")
	@MethodSource("testNormalizePhoneNumberProvider")
	void testNormalizePhoneNumber(String phone, String expected) {
		//When
		String actual = aggregateService.normalizePhoneNumber(phone);
		//Then
		assertEquals(expected, actual);
	}

	@DisplayName("testIsValidPhoneNumber")
	@ParameterizedTest(name = "#{index} - phone: {0} | expected: {1}")
	@MethodSource("testIsValidPhoneNumberProvider")
	void testIsValidPhoneNumber(String phone, boolean expected) {
		//When
		boolean actual = aggregateService.isValidPhoneNumber(phone);
		//Then
		assertEquals(expected, actual);
	}

	@DisplayName("testAggregate")
	@ParameterizedTest(name = "#{index} - phone: {0} | prefixReturn: {1} | sectorReturn: {2} | expected: {3}")
	@MethodSource("testAggregateProvider")
	void testAggregate(String phoneNumber, Optional<String> prefixReturn, Optional<BusinessSector> sectorReturn, AggregateResponse expected) {
		//Given
		lenient().when(prefixDataService.prefixByPhoneNumber(anyString())).thenReturn(prefixReturn);
		lenient().when(businessSectorDataService.businessSectorByPhoneNumber(anyString())).thenReturn(sectorReturn);

		//When
		//For test simplicity purpose give one phone number by turn
		AggregateResponse actual = aggregateService.aggregate(Arrays.asList(phoneNumber));

		//Then
		assertEquals(expected.toString(), actual.toString());
	}


	/* DATA PROVIDERS */

	private static Stream<Arguments> testAggregateProvider() {
		return Stream.of(
				Arguments.of("+123456789",
						Optional.ofNullable("1"),
						Optional.of(new BusinessSector("+123456789", "Banking")),
						new AggregateResponse("1", new BusinessSectorCount("Banking", 1.0))
				),
				Arguments.of("0044123456789",
						Optional.ofNullable("44"),
						Optional.of(new BusinessSector("0044123456789", "Marketing")),
						new AggregateResponse("44", new BusinessSectorCount("Marketing", 1.0))
				)
		);
	}

	private static Stream<Arguments> testNormalizePhoneNumberProvider() {
		return Stream.of(
				Arguments.of("1 2 3 4 5 6 7 8 9", "123456789"),
				Arguments.of("+123 4 5 6 7 8 9", "123456789"),
				Arguments.of("00123 4 5 6 7 8 9", "123456789")
		);
	}

	private static Stream<Arguments> testIsValidPhoneNumberProvider() {
		return Stream.of(
				/* Valid Examples*/
				Arguments.of("+123", true),
				Arguments.of("+1 2 3", true),
				Arguments.of("+1234567", true),
				Arguments.of("+1 2 3 4 5 6 7", true),
				Arguments.of("+12345678", true),
				Arguments.of("+1 2 3 4 5 6 7 8", true),
				Arguments.of("+123456789", true),
				Arguments.of("+1 2 3 4 5 6 7 8 9", true),
				Arguments.of("+1234567890", true),
				Arguments.of("+1 2 3 4 5 6 7 8 9 0", true),
				Arguments.of("+12345678901", true),
				Arguments.of("+1 2 3 4 5 6 7 8 9 0 1", true),
				Arguments.of("+123456789012", true),
				Arguments.of("+1 2 3 4 5 6 7 8 9 0 1 2", true),
				Arguments.of("00123", true),
				Arguments.of("001 2 3", true),
				Arguments.of("001234567", true),
				Arguments.of("001 2 3 4 5 6 7", true),
				Arguments.of("0012345678", true),
				Arguments.of("001 2 3 4 5 6 7 8", true),
				Arguments.of("00123456789", true),
				Arguments.of("001 2 3 4 5 6 7 8 9", true),
				Arguments.of("001234567890", true),
				Arguments.of("001 2 3 4 5 6 7 8 9 0", true),
				Arguments.of("0012345678901", true),
				Arguments.of("001 2 3 4 5 6 7 8 9 0 1", true),
				Arguments.of("00123456789012", true),

				Arguments.of("123", true),
				Arguments.of("1234567", true),
				Arguments.of("12345678", true),
				Arguments.of("123456789", true),
				Arguments.of("1234567890", true),
				Arguments.of("12345678901", true),
				Arguments.of("123456789012", true),


				/* Invalid Examples*/
				Arguments.of("1234567890123", false),
				Arguments.of("123456", false),
				Arguments.of("12345", false),
				Arguments.of("1234", false),

				Arguments.of("+1", false),
				Arguments.of("+ 1", false),

				Arguments.of("00 1", false),
				Arguments.of("+12", false),
				Arguments.of("+1 2", false),

				Arguments.of("+1234", false),
				Arguments.of("+1 2 3 4", false),
				Arguments.of("+12345", false),
				Arguments.of("+1 2 3 4 5", false),
				Arguments.of("+123456", false),
				Arguments.of("+1 2 3 4 5 6", false),

				Arguments.of("0012", false),
				Arguments.of("001 2", false),
				Arguments.of("001234", false),
				Arguments.of("001 2 3 4", false),
				Arguments.of("0012345", false),/////////////
				Arguments.of("001 2 3 4 5", false),///////////////
				Arguments.of("00123456", false),////////////////
				Arguments.of("001 2 3 4 5 6", false)////////////
		);
	}

}
