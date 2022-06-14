package com.integrador.tpi.lib.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Utils {
    private static final String REGEX = "[^A-Za-z\\d]";
    private static final Pattern regexPattern = Pattern.compile(REGEX);

    public static String normalizeString(String term) {
        term = Normalizer.normalize(term, Normalizer.Form.NFD);
        term = regexPattern.matcher(term).replaceAll("");
        term = term.toLowerCase();
        return term;
    }
}
