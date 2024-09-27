public int getOffsetFromLocal(long instantLocal) {
    // get the offset at instantLocal (first estimate)
    final int offsetLocal = getOffset(instantLocal);
    // adjust instantLocal using the estimate and recalc the offset
    final long instantAdjusted = instantLocal - offsetLocal;
    final int offsetAdjusted = getOffset(instantAdjusted);
    // if the offsets differ, we must be near a DST boundary
    if (offsetLocal != offsetAdjusted) {
        // we need to ensure that time is always after the DST gap
        // this happens naturally for positive offsets, but not for negative
        if ((offsetLocal - offsetAdjusted) < 0) {
            // Calculate next transition for both local and adjusted times
            long nextLocal = nextTransition(instantLocal);
            long nextAdjusted = nextTransition(instantAdjusted);
            // Check if the next transition points are different, adjust if necessary
            if (nextLocal != nextAdjusted) {
                // Return the local offset if the next transition times are not aligned,
                // ensuring the time is on or after the transition
                return offsetLocal;
            }
        } else {
            // Check if the original local time is before the next transition
            long nextLocal = nextTransition(instantLocal);
            if (instantLocal < nextLocal) {
                // If we are before the transition, return the adjusted offset
                return offsetAdjusted;
            }
        }
    }
    return offsetAdjusted;
}

// Basic methods
//--------------------------------------------------------------------

/**
 * Get the datetime zone as a {@link java.util.TimeZone}.
 */
public abstract java.util.TimeZone toTimeZone();

/**
 * Get the offset for given instant in milliseconds from 1970-01-01T00:00:00Z.
 *
 * @param instant milliseconds from 1970-01-01T00:00:00Z
 * @return offset in milliseconds
 */
public abstract int getOffset(long instant);

/**
 * Finds the next transition after the specified instant.
 *
 * @param instant milliseconds from 1970-01-01T00:00:00Z
 * @return milliseconds from 1970-01-01T00:00:00Z to next transition
 */
public abstract long nextTransition(long instant);

/**
 * Retreats the given instant to where the time zone offset or name changes.
 * If the instant returned is exactly the same as passed in, then
 * no changes occur before the given instant.
 *
 * @param instant milliseconds from 1970-01-01T00:00:00Z
 * @return milliseconds from 1970-01-01T00:00:00Z
 */
public abstract long previousTransition(long instant);