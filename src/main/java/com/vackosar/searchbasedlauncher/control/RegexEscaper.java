package com.vackosar.searchbasedlauncher.control;

import java.util.regex.Pattern;

import roboguice.inject.ContextSingleton;

@ContextSingleton
public class RegexEscaper {
    private static final String[] SPECIAL_CHARACTERS = {".", "+", "-", "&", "|", "(", ")", "{", "}", "[", "]", "^", "\"", "~", "*", "?", ":", "\\"};
    private static final String PREFIX = "([";
    private static final String SUFFIX = "])";
    private static final String BACKSLASH = "\\";
    private static final String REPLACEMENT = "\\\\$1";
    private static final Pattern PATTERN = createPattern();

    public String act(String input) {
        return PATTERN.matcher(input).replaceAll(REPLACEMENT);
    }

    private static Pattern createPattern() {
        final StringBuilder builder = new StringBuilder();
        builder.append(PREFIX);
        for (String character: SPECIAL_CHARACTERS) {
            builder.append(BACKSLASH).append(character);
        }
        builder.append(SUFFIX);
        return Pattern.compile(builder.toString());
    }
}
