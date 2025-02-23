public long adjustOffset(long instant, boolean earlierOrLater) {
    // evaluate 3 hours before and after to work out if anything is happening
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // No changes detected, return original instant
    if (instantBefore == instantAfter) {
        return instant;  
    }
    
    // Zone has shifted between instantBefore and instantAfter
    long local = convertUTCToLocal(instant);
    long resultEarlier = convertLocalToUTC(local, false, instantBefore);
    long resultLater = convertLocalToUTC(local, false, instantAfter);

    // Check if the earlier instant is strictly before the input instant
    // This ensures that we pick the next valid offset in sequence corresponding to the flag
    if (earlierOrLater && local == convertUTCToLocal(resultEarlier)) {
        return resultLater; // the later mapping is valid, so use the later offset
    } else if (!earlierOrLater && local == convertUTCToLocal(resultLater)) {
        return resultEarlier; // the earlier mapping is valid, so use the earlier offset
    }
    
    // No appropriate mapping found, return original unchanged (should not happen ideally)
    return instant;
}
