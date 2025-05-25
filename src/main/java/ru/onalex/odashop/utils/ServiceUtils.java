package ru.onalex.odashop.utils;

import java.io.File;

public class ServiceUtils {
    public static String replaceQuotes(String input) {

        if (input == null || input.isEmpty()) {
            return input;
        }

        // заменяем все последовательности \" на временные маркеры
        String temp = input.replace("\\\"", "QUOTE_MARKER");

        // ставим «ёлочки» вместо маркеров
        String withQuotes = temp.replaceAll("(^|\\s)QUOTE_MARKER", "$1«")
                .replaceAll("QUOTE_MARKER($|\\s*|\\p{Punct})", "»$1");

        // убираем оставшиеся обратные слеши
        return withQuotes.replace("\\", "");
    }

}
