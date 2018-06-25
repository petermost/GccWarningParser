package com.pera_software.html;

import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;


public class OptionParserTest {
	private static final Duration TIMEOUT = Duration.ofMinutes(1);

	@Test
	public void test_GCC_Latest() throws Exception {
		int optionsSeen = 0;
		OptionParser parser = new OptionParser();
		List<Option> options = parser.parse("https://gcc.gnu.org/onlinedocs/gcc/Warning-Options.html", TIMEOUT);
		
		for (Option option : options) {
			for (String name : option.names()) {
				switch (name) {
					case "-fsyntax-only":
						assertThat(option.description(), startsWith("Check the code for syntax errors"));
						++optionsSeen;
						break;
					case "-Wpedantic":
					case "-pedantic":
						assertThat(option.description(), startsWith("Issue all the warnings demanded by strict ISO C and ISO C++"));
						++optionsSeen;
						break;
					case "-Wall":
						assertThat(option.description(), startsWith("This enables all the warnings about constructions that some users consider questionable"));
						++optionsSeen;
						break;
					case "-Wextra":
						assertThat(option.description(), startsWith("This enables some extra warning flags that are not enabled by -Wall"));
						++optionsSeen;
						break;
					case "-Wformat":
					case "-Wformat=n":
						assertThat(option.description(), startsWith("Check calls to printf and scanf, etc."));
						assertThat(option.description(), endsWith("warn about strftime formats that may yield only a two-digit year."));
						++optionsSeen;
						break;
					case "-Whsa":
						assertThat(option.description(), startsWith("Issue a warning when HSAIL cannot be emitted"));
						++optionsSeen;
						break;
				}
			}
			System.out.format("%s:\n\t%.100s\n", option.names(), option.description());
		}
		assertEquals(8, optionsSeen);
	}
}
