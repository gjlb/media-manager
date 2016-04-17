package io.gbell.utils;

import io.gbell.models.anime.AnimeShow;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextUtils {

    private static final SimpleDateFormat tvDateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseTVDate(String input) {
        try {
            return tvDateFormatter.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static String getStackTraceAsString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    public static String getTitle(AnimeShow animeShow) {
        AnimeShow.Titles titles = animeShow.getTitles();
        String[] possibleTitles = {titles.getEnglish(), titles.getCanonical(), titles.getRomaji(), titles.getJapanese()};
        for (String title : possibleTitles) {
            if (!TextUtils.isBlank(title)) {
                return title;
            }
        }
        return null;
    }

    public static String getAlternateTitle(AnimeShow animeShow) {
        final String mainTitle = getTitle(animeShow);
        AnimeShow.Titles titles = animeShow.getTitles();
        String[] possibleTitles = {titles.getEnglish(), titles.getCanonical(), titles.getRomaji(), titles.getJapanese()};
        for (String alternateTitle : possibleTitles) {
            if (!TextUtils.isBlank(alternateTitle) && !alternateTitle.equalsIgnoreCase(mainTitle)) {
                return alternateTitle;
            }
        }
        return null;
    }

}
