import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public static boolean verifyCheckSum(byte[] header) {
    long storedSum = 0;
    long unsignedSum = 0;
    long signedSum = 0;

    int digits = 0;
    for (int i = 0; i < header.length; i++) {
        byte b = header[i];
        if (CHKSUM_OFFSET  <= i && i < CHKSUM_OFFSET + CHKSUMLEN) {
            if ('0' <= b && b <= '7' && digits++ < 6) {
                storedSum = storedSum * 8 + b - '0';
            } else if (digits > 0) {
                digits = 6;
            }
            b = ' ';
        }
        unsignedSum += 0xff & b;
        signedSum += b;
    }
    return storedSum == unsignedSum || storedSum == signedSum;
}

public static TarArchiveInputStream getStreamFor(byte[] header) {
    // Implementation to create and return a valid instance of TarArchiveInputStream
    // using the provided header data
    TarArchiveInputStream inputStream = new TarArchiveInputStream(new ByteArrayInputStream(header));
    return inputStream;
}