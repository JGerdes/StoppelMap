package com.jonasgerdes.stoppelmap.util;

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
}
