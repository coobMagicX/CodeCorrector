public long adjustOffset(long instant, boolean earlierOrLater) {
    // a bit messy, but will work in all non-pathological cases
    
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    if (instantBefore == instantAfter) {
        return instant;  // not an overlap (less than is a gap, equal is normal case)
    }
    
    // work out range of instants that have duplicate local times
    long local = convertUTCToLocal(instant);
    return convertLocalToUTC(local, false, earlierOrLater ? instantAfter : instantBefore);
}

public long convertUTCToLocal(long utcInstant) {
    return getMillisKeepLocal(this, utcInstant);
}

public long convertLocalToUTC(long localInstant, boolean strict, long defaultUTCInstant) {
    long millis = localInstant;
    long offset = getOffset(millis);
    long utcInstant = millis - offset;
    long offset2 = getOffset(utcInstant);
    if (offset != offset2 && (strict || offset < 0)) {
        if (strict) {
            throw new IllegalArgumentException("Illegal instant due to time zone offset transition");
        } else {
            utcInstant = localInstant - offset2;
            offset = getOffset(utcInstant);
            if (offset != offset2) {
                throw new IllegalArgumentException("Illegal instant due to time zone offset transition");
            }
        }
    }
    return utcInstant;
}

public long getOffset(long instant) {
    return getOffsetFromLocal(instant, null);
}

public long getOffsetFromLocal(long localInstant, Integer standardOffset) {
    DateTimeField field = iZone.getField(TYPE_OFFSET);
    if (field.isSupported()) {
        try {
            int offset = field.get(localInstant);
            int offset2 = field.get(localInstant - offset);
            if (offset != offset2) {
                return offset;
            }
        } catch (RuntimeException ex) {
            // continue
        }
    }
    return standardOffset != null ? standardOffset : iZone.getOffset(localInstant);
}

public long getMillisKeepLocal(DateTimeZone newZone, long oldInstant) {
    if (newZone == null) {
        newZone = DateTimeZone.getDefault();
    }
    if (newZone == this) {
        return oldInstant;
    }
    long instantLocal = convertUTCToLocal(oldInstant);
    return newZone.convertLocalToUTC(instantLocal, false, oldInstant);
}