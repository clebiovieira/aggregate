package com.challenge.aggregate.service.data.prefix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test TestPrefixDataServiceImpl")
class TestPrefixDataServiceImpl {
	@Spy
	private PrefixDataServiceImpl prefixDataService;

	@DisplayName("testBusinessSectorByPhoneNumber")
	@ParameterizedTest(name = "#{index} - phone: {0} | sucess: {1} | expected: {2}")
	@MethodSource("testPrefixByPhoneNumberProvider")
	void testPrefixByPhoneNumber(String phone, boolean success, Optional<String> expected) {
		//Given
		lenient().doReturn(success).when(prefixDataService).findPrefixInTextFile(anyString(), anyString());

		//When
		Optional<String> actual = prefixDataService.prefixByPhoneNumber(phone);

		//Then
		assertEquals(expected, actual);
	}

	/* DATA AND PROVIDERS */

	private static Stream<Arguments> testPrefixByPhoneNumberProvider() {
		return Stream.of(
				Arguments.of("123", false, Optional.empty()),
				Arguments.of("123", true, Optional.of("12"))
		);
	}

}

