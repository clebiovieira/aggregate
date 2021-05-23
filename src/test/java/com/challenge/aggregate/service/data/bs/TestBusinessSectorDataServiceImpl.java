package com.challenge.aggregate.service.data.bs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.challenge.aggregate.config.ServerConfig;
import com.challenge.aggregate.service.domain.BusinessSector;

@ExtendWith(MockitoExtension.class)
@DisplayName("Test BusinessSectorDataService")
class TestBusinessSectorDataServiceImpl {
	@Mock
	RestTemplate restTemplate;

	BusinessSectorDataServiceImpl businessSectorDataService;

	@BeforeEach
	void setup() {
		final ServerConfig serverConfig = new ServerConfig();
		serverConfig.setTextDatabase("file.txt");
		serverConfig.setUrl("https://challenge-business-sector-api.meza.com/sector/%s");

		this.businessSectorDataService = new BusinessSectorDataServiceImpl(serverConfig, this.restTemplate);
	}

	@DisplayName("testBusinessSectorByPhoneNumber")
	@ParameterizedTest(name = "#{index} - phone: {0} | response: {1} | expected: {2}")
	@MethodSource("testBusinessSectorByPhoneNumberProvider")
	void testBusinessSectorByPhoneNumber(String phone, ResponseEntity<BusinessSector> response, Optional<BusinessSector> expected) {
		//Given
		when(restTemplate.getForEntity(String.format(BUSINESS_SECTOR_URL, phone), BusinessSector.class)).thenReturn(response);

		//When
		Optional<BusinessSector> actual = businessSectorDataService.businessSectorByPhoneNumber(phone);

		//Then
		assertEquals(expected, actual);
	}

	/* DATA AND PROVIDERS */
	private static final String BUSINESS_SECTOR_URL = "https://challenge-business-sector-api.meza.com/sector/%s";

	private static BusinessSector BS_SECTOR_1 = new BusinessSector("+123", "Marketing");

	private static Stream<Arguments> testBusinessSectorByPhoneNumberProvider() {
		return Stream.of(
				Arguments.of("+123",
						new ResponseEntity(BS_SECTOR_1, HttpStatus.OK), Optional.ofNullable(BS_SECTOR_1)),
				Arguments.of("+123",
						new ResponseEntity(BS_SECTOR_1, HttpStatus.BAD_REQUEST), Optional.empty()),
				Arguments.of("+123",
						new ResponseEntity(BS_SECTOR_1, HttpStatus.NOT_FOUND), Optional.empty())
		);
	}
}
