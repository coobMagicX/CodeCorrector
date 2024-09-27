private static ZipLong unixTimeToZipLong(long l) {
    final long TWO_TO_32 = 0x100000000L;
    if (l >= TWO_TO_32) {
        // Handle timestamps that are too big
        l = l % TWO_TO_32; // Convert timestamp to a valid value within the range of a signed 32-bit integer
        if (l < 0) {
            l += TWO_TO_32; // Ensure the converted timestamp is positive
        }
    }
    return new ZipLong(l);
}