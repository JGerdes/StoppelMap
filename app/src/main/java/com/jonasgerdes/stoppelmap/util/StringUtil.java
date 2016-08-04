package com.jonasgerdes.stoppelmap.util;

import com.jonasgerdes.stoppelmap.model.shared.RealmString;

import java.util.List;

/**
 * Created by Jonas on 01.08.2016.
 */
public class StringUtil {

    public static final int EMOJI_BUS = 0x1F68C;
    public static final int EMOJI_BUS_STOP = 0x1F68F;
    public static final int EMOJI_ROLLERCOASTER = 0x1F3A2;


    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static String concat(List<RealmString> parts, String glue) {
        if (parts == null) {
            return null;
        }
        if (parts.size() == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.size() - 1; i++) {
            result.append(parts.get(i).getVal());
            result.append(glue);
        }
        result.append(parts.get(parts.size() - 1).getVal());
        return result.toString();
    }
}
