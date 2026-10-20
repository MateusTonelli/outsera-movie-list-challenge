package com.outsera.movielist.domain;

import java.util.List;
import java.util.regex.Pattern;

final class ProducerNames {

    private static final Pattern COMMA_AND = Pattern.compile(",\\s+and\\s+", Pattern.CASE_INSENSITIVE);
    private static final Pattern AND = Pattern.compile("\\s+and\\s+", Pattern.CASE_INSENSITIVE);
    private static final Pattern COMMA = Pattern.compile(",");

    private ProducerNames() {
    }

    static List<String> parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        String normalized = AND.matcher(COMMA_AND.matcher(raw).replaceAll(", ")).replaceAll(", ");
        return COMMA.splitAsStream(normalized)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .toList();
    }
}
