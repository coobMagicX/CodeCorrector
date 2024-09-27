private static ZipLong unixTimeToZipLong(long l) {
    final long MAX_32_SIGNED_INT = 0x7FFFFFFFL;
    if (l < 0 || l > MAX_32_SIGNED_INT) {
        throw new IllegalArgumentException("X5455 timestamps must fit in a signed 32 bit integer: " + l);
    }
    return new ZipLong(l);
}