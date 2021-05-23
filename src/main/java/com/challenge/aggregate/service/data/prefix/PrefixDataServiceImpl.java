package com.challenge.aggregate.service.data.prefix;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrefixDataServiceImpl implements PrefixDataService {
	private static final String TEXT_FILE_PATH = "prefixes.txt";

	/**
	 * <p>Verify and retrieve a prefix phone number, it should belong a deterministic text file, otherwise Optional Empty</p>
	 *
	 * @param phoneNumber Valid Phone Number
	 * @return Valid Prefix
	 */
	public Optional<String> prefixByPhoneNumber(String phoneNumber) {
		//The number always has a prefix at least with one character
		//Regardless prefix, at least one suffix number
		//In case of prefix shadowing by substring, use the highest (Eg. +44 and +443(Preference))
		final Instant start = Instant.now();

		for (int i = 1; i < phoneNumber.length(); i++) {
			final String prefix = phoneNumber.substring(0, (phoneNumber.length() - i));

			if (findPrefixInTextFile(TEXT_FILE_PATH, prefix)) {

				log.info("Prefix found: {} - Time Elapsed {}", prefix, Duration.between(start, Instant.now()).toMillis());
				return Optional.of(prefix);
			}
		}
		log.info("Prefix not found: {} - Time Elapsed {}", phoneNumber, Duration.between(start, Instant.now()).toMillis());
		return Optional.empty();
	}

	/**
	 * <p>Find under text file, using NIO API leverage of parallel access and filter</p>
	 *
	 * @param path   relative Path to text file
	 * @param prefix Prefix
	 * @return Valid Prefix
	 */
	boolean findPrefixInTextFile(String path, String prefix) {
		log.info("Prefix file in:  {}", path);

		try (Stream<String> lines = Files.lines(Paths.get(path))) {
			return lines.parallel().anyMatch(s -> s.equalsIgnoreCase(prefix));
		}
		catch (IOException error) {
			log.error("Error trying to findPrefixInTextFile in: {}", path);
			return false;
		}
	}

}