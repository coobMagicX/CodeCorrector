// ----------------- Fix for LANG-59 ---------------------- START ---------------
// Manually truncate milliseconds, seconds, and minutes, rather than using
// Calendar methods.

// Truncate milliseconds
val.set(Calendar.MILLISECOND, 0);

// Truncate seconds
if (field == Calendar.SECOND) {
    val.set(Calendar.SECOND, 0);    // Make sure to truncate each field according to expectations
}

// If handling minutes needs direct truncation, add similar handling for minutes here

// ----------------- Fix for LANG-59 ----------------------- END ----------------

// Continue with existing logic...
