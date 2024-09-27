private static final long TWO_TO_32 = 0x100000000L;
private static final long ERROR_CODE = -1;

private static ZipLong unixTimeToZipLong(long l) {
    if (l >= TWO_TO_32 || l < 0) {
        return new ZipLong(ERROR_CODE);
    }
    return new ZipLong(l);
}