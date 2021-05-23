package com.challenge.aggregate.service.data.prefix;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenge.aggregate.config.ServerConfig;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test TestPrefixDataServiceImpl")
class TestPrefixDataServiceImpl {
	private PrefixDataServiceImpl prefixDataService;

	@BeforeEach
	void setup() {
		final ServerConfig serverConfig = new ServerConfig();
		serverConfig.setTextDatabase("file.txt");
		serverConfig.setUrl("https://challenge-business-sector-api.meza.com/sector/%s");

		this.prefixDataService = spy(new PrefixDataServiceImpl(serverConfig));
	}

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

