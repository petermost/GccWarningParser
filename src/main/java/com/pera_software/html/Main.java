package com.pera_software.html;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.pera_software.aidkit.lang.Out;

//https://gcc.gnu.org/onlinedocs/gcc-7.3.0/gcc/Warning-Options.html
//https://gcc.gnu.org/onlinedocs/gcc-8.1.0/gcc/Warning-Options.html
//https://gcc.gnu.org/onlinedocs/gcc/Warning-Options.html

// TODO: Filter out options not applicable for C++ (C and Objective-C only)
// TODO: Mark the occurrence of the 'enabled' text.
// TODO: Check whether https://gcc.gnu.org/onlinedocs/gcc-7.3.0/gcc/C_002b_002b-Dialect-Options.html#C_002b_002b-Dialect-Options
//       should be parsed too.

public class Main {
	private static final Duration TIMEOUT = Duration.ofMinutes(1);

	private static final List<String> IGNORED_OPTIONS = List.of(
		// GCC options:
			
		"-fsyntax-only",
		"-fmax-errors=n",
		"-w",
		"-Werror",
		"-Werror=",
		"-Wfatal-errors",
		"-Wpedantic",
		"-pedantic",
		"-pedantic-errors",
		"-Wall",
		"-Wextra",
		
		// C++ options:
		
		"-nostdinc++"
		
	);
	
	private static final List<String> ENABLED_TEXTS = List.of(
		"by default", 
		"by -Wall", 
		"in -Wall", 
		"by -Wextra", 
		"by either -Wall", 
		"by either -Wextra", 
		"by either -Wpedantic", 
		"with -Wextra",
		"by -Wpedantic", 
		"by -Wextra",
		"-Wall implies -W"
	);
	
	public static void main(String[] args) throws Exception {
		List<Option> options = parseOptions(
			"https://gcc.gnu.org/onlinedocs/gcc-7.3.0/gcc/Warning-Options.html",
			"https://gcc.gnu.org/onlinedocs/gcc-7.3.0/gcc/C_002b_002b-Dialect-Options.html#C_002b_002b-Dialect-Options"
		);
		
		// Remove ignored options:
		
		for (var iterator = options.iterator(); iterator.hasNext(); ) {
			Option option = iterator.next();
			if (isIgnoredOption(option)) {
				System.out.format("Ignored option: %s:\n\t%s\n\n", option.names(), option.description());
				iterator.remove();
			}
		}
		
		// Print redundant options:
		
		printSeperator();
		for (var iterator = options.iterator(); iterator.hasNext(); ) {
			Option option = iterator.next();
			Out<String> surroundingText = new Out<>();
			if (isEnabledByCompositeSwitch(option, surroundingText)) {
				System.out.format("Redundant option: %s\n\t%s\n%s\n\n", option.names(), surroundingText.get(), option.description());
				iterator.remove();
			}
		}
		
		// Print remaining options:
		
		printSeperator();
		for (Option option : options) {
			System.out.format("Remaining option: %s:\n\t%s\n\n", option.names(), option.description());
		}
	}

	private static List<Option> parseOptions(String ...urls) throws Exception {
		OptionParser parser = new OptionParser();
		List<Option> options = new ArrayList<>();
		
		for (String url : urls) {
			options.addAll(parser.parse(url, TIMEOUT));
		}
		return options;
	}
	
	private static boolean isIgnoredOption(Option option) {
		for (String ignoredOption : IGNORED_OPTIONS) {
			for (String name : option.names()) {
				if (name.equals(ignoredOption) || name.startsWith("-Wno-") || name.startsWith("-f") || isCOrObjectiveCOption(name)) {
					return true;
				}
			}
		}
		return false;
	}
		
	private static boolean isCOrObjectiveCOption(String text) {
		return text.contains("(C and Objective-C only)") || text.contains("(C, Objective-C only)");
	}
	
	private static boolean isEnabledByCompositeSwitch(Option option, Out<String> surroundingText) {
		for (String enabledText : ENABLED_TEXTS) {
			if (findString(option.description(), enabledText, surroundingText)) {
				return true;
			}
		}
		return false;
	}

	private static boolean findString(String s, String text, Out<String> surroundingText) {
		var index = s.indexOf(text);
		if (index >= 0) {
			surroundingText.set(subString(s, index - 20, index + 20));
			return true;
		}
		return false;
	}
	
	private static String subString(String s, int beginIndex, int endIndex) {
		if (beginIndex < 0) {
			beginIndex = 0;
		}
		if (endIndex > s.length()) {
			endIndex = s.length();
		}
		return s.substring(beginIndex, endIndex);
	}
	
	private static void printSeperator() {
		System.out.println("=====================================================================================================================");
	}
}
