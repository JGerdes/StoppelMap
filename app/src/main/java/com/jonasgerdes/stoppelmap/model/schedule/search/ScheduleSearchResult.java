package com.jonasgerdes.stoppelmap.model.schedule.search;

import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;
import com.jonasgerdes.stoppelmap.model.schedule.Event;

/**
 * Created by Jonas on 06.08.2016.
 */
public class ScheduleSearchResult {

    public static final int REASON_NAME = 0;
    public static final int REASON_ARTIST = 1;
    public static final int REASON_TAG = 2;
    public static final int REASON_LOCATION = 3;

    private Event mEvent;
    private int mReason;
    private String mReasonString;

    public ScheduleSearchResult(Event event, int reason, String reasonString) {
        mEvent = event;
        mReason = reason;
        mReasonString = reasonString;
    }

    public Event getEvent() {
        return mEvent;
    }

    public int getReason() {
        return mReason;
    }

    public String getReasonString() {
        return mReasonString;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SearchResult) {
            return ((SearchResult) o).getIdentifier().equals(this.getIdentifier());
        }
        return false;
    }

    public String getIdentifier() {
        return mEvent.getUuid() + mReason + mReasonString;
    }
}
