public class Base64 {
    private static final int CHUNK_SIZE = 76;
    private static final byte[] CHUNK_SEPARATOR = {'\r', '\n'};
    private final boolean urlSafe;
    private final byte[] alphabet;
    private static final byte[] STANDARD_ENCODE_TABLE = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };
    private static final byte[] URL_SAFE_ENCODE_TABLE = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'
    };

    public Base64(boolean urlSafe) {
        this.urlSafe = urlSafe;
        this.alphabet = urlSafe ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
    }

    public byte[] encode(byte[] binaryData) {
        if (binaryData == null || binaryData.length == 0) {
            return binaryData;
        }
        return encodeBase64(binaryData, false, this.urlSafe, Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
        if (binaryData == null || binaryData.length == 0) {
            return binaryData;
        }

        long len = getEncodeLength(binaryData, CHUNK_SIZE, CHUNK_SEPARATOR);
        if (len > maxResultSize) {
            throw new IllegalArgumentException("Input array too big, the output array would be bigger (" +
                len +
                ") than the specified maximum size of " +
                maxResultSize);
        }

        Base64 b64 = isChunked ? new Base64(true) : new Base64(urlSafe);
        return b64.encode(binaryData);
    }

    private static long getEncodeLength(byte[] binaryData, int chunkSize, byte[] chunkSeparator) {
        chunkSize = (chunkSize / 4) * 4;
        long len = (binaryData.length * 4) / 3;
        long mod = len % 4;
        if (mod != 0) {
            len += 4 - mod;
        }
        len += (len / chunkSize) * chunkSeparator.length;
        return len;
    }
}