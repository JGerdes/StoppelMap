package com.jonasgerdes.stoppelmap.model.version;

import java.util.List;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.03.2017
 */

public class Message {
    public String slug;
    public List<Long> versions;
    public boolean showAlways;
    public String title;
    public String message;
}
