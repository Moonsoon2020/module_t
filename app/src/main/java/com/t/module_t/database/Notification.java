package com.t.module_t.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Notification{
    public String text;
    public String id_notifications;
    public Date date;

    public static String splitString(String inputString, int maxCharactersPerLine) {
        String[] words = inputString.split("\\s+"); // Разбиваем строку на слова

        StringBuilder currentLine = new StringBuilder();
        List<String> lines = new ArrayList<>();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxCharactersPerLine) {
                // Добавляем слово и пробел в текущую строку, если это не превысит лимит символов
                if (currentLine.length() != 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                // Если текущая строка переполнится, добавляем её в список строк и начинаем новую
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }

        // Добавляем оставшуюся часть текущей строки
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        // Собираем строки в одну переменную
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            result.append(line).append("\n");
        }

        return result.toString();
    }

    public Notification(String text, String id_notifications){
        date = new Date();
        this.id_notifications = id_notifications;
        this.text = splitString(text, 20);
    }
    public Notification(String text, String id_notifications, HashMap<String,Object> data) {
        int year = ((Long)data.get("year")).intValue();
        int month = ((Long)data.get("month")).intValue(); // Учитываем, что месяцы начинаются с 0
        int day = ((Long)data.get("date")).intValue();
        int hour = ((Long)data.get("hours")).intValue();
        int minute = ((Long)data.get("minutes")).intValue();
        int second = ((Long)data.get("seconds")).intValue();
        this.id_notifications = id_notifications;
        this.text = splitString(text, 20);
        this.date = new Date(year, month, day, hour, minute, second); // Год начинается с 1900
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Notification that = (Notification) obj;
        return Objects.equals(text, that.text) && Objects.equals(date, that.date);
    }
}
