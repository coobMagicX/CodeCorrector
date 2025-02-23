public long adjustOffset(long instant, boolean earlierOrLater) {
    // The method should properly handle the choice between the earlier and later offset during an overlap caused by a daylight saving adjustment.

    // Evaluate 3 hours before and after the given instant (taking an arbitrary duration that would encompass typical DST transitions)
    long instantBefore = convertUTCToLocal(instant - 3 * DateTimeConstants.MILLIS_PER_HOUR);
    long instantAfter = convertUTCToLocal(instant + 3 * DateTimeConstants.MILLIS_PER_HOUR);
    
    // If the converted times are the same, the given instant doesn't correspond to an overlap (no DST transition around, or a gap exists)
    if (instantBefore == instantAfter) {
        return instant;  // Normal case or gap, no overlap to adjust for
    }
    
    // Evaluate the local instant for the given input
    long local = convertUTCToLocal(instant);
    
    // Find the boundaries of local times that map to two different UTC times due to an overlap
    long overlapStart = convertLocalToUTC(local, true, instant);  // Translating to earlier instant during overlap
    long overlapEnd = convertLocalToUTC(local, false, instant);   // Translating to later instant during overlap
    
    // Determine which offset to use based on earlierOrLater
    if (earlierOrLater) {
        // Opt for the later offset at overlap if allowed (typically means keeping the instant on the same "local" time but at later UTC time)
        // This is particularly relevant if the local time is interpreted as after the moment when clocks go back
        return convertLocalToUTC(local, false, overlapEnd);
    } else {
        // Opt for the earlier offset at overlap by default (mapping to the time before clocks were set back)
        // This choice would generally mean observing the earlier temporal point in regions that shift time back
        return convertLocalToUTC(local, true, overlapStart);
    }
}
