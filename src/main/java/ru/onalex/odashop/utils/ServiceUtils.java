package ru.onalex.odashop.utils;

public class ServiceUtils {

    public static String replaceQuotes(String input) {
//        if (input == null || input.isEmpty()) {
//            return input;
//        }
//
//        // Разбиваем строку на токены, сохраняя разделители
//        String[] tokens = input.split("((?<=\")|(?=\"))");
//
//        StringBuilder result = new StringBuilder();
//        boolean openQuote = false;
//
//        for (String token : tokens) {
//            if (token.equals("\"")) {
//                if (openQuote) {
//                    result.append("»");
//                    openQuote = false;
//                } else {
//                    result.append("«");
//                    openQuote = true;
//                }
//            } else {
//                result.append(token);
//            }
//        }
//
//        return result.toString().replace("\\","");
        if (input == null || input.isEmpty()) {
            return input;
        }

        // 1. Заменяем все последовательности \" на временные маркеры
        String temp = input.replace("\\\"", "QUOTE_MARKER");

        // 2. Расставляем «ёлочки» вместо маркеров
        String withQuotes = temp.replaceAll("(^|\\s)QUOTE_MARKER", "$1«")
                .replaceAll("QUOTE_MARKER($|\\s*|\\p{Punct})", "»$1");

        // 3. Важное исправление: убираем оставшиеся обратные слеши
        return withQuotes.replace("\\", "");
    }
}
