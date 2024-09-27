class Base64 {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int MASK_8BITS = 0xff;
    private static final int decodeSize = 4;
    private static final byte PAD = '=';
    private static final int[] DECODE_TABLE = new int[128]; // Simplified, actual implementation needed

    private byte[] buffer;
    private int pos;
    private int readPos;
    private int x;
    private int modulus;
    private boolean eof;

    public Base64() {
        this.eof = false;
        this.pos = 0;
        this.readPos = 0;
        this.x = 0;
        this.modulus = 0;
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        return new Base64().decode(base64Data);
    }

    public byte[] decode(byte[] in) {
        decode(in, 0, in.length);
        byte[] result = new byte[pos];
        System.arraycopy(buffer, 0, result, 0, pos);
        return result;
    }

    void decode(byte[] in, int inPos, int inAvail) {
        if (eof) {
            return;
        }
        if (inAvail < 0) {
            eof = true;
        }
        if (buffer == null || buffer.length - pos < decodeSize) {
            resizeBuffer();
        }
        for (int i = 0; i < inAvail; i++) {
            byte b = in[inPos++];
            if (b == PAD) {
                // We're done.
                eof = true;
                break;
            } else {
                if (b >= 0 && b < DECODE_TABLE.length) {
                    int result = DECODE_TABLE[b];
                    if (result >= 0) {
                        modulus = (++modulus) % 4;
                        x = (x << 6) + result;
                        if (modulus == 0) {
                            if (buffer.length - pos < 3) {
                                resizeBuffer();
                            }
                            buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                            buffer[pos++] = (byte) ((x >> 8) & MASK_8BITS);
                            buffer[pos++] = (byte) (x & MASK_8BITS);
                        }
                    }
                }
            }
        }

        if (eof && modulus != 0) {
            x = x << 6;
            switch (modulus) {
                case 2:
                    x = x << 6;
                    if (buffer.length - pos < 1) {
                        resizeBuffer();
                    }
                    buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                    break;
                case 3:
                    if (buffer.length - pos < 2) {
                        resizeBuffer();
                    }
                    buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
                    buffer[pos++] = (byte) ((x >> 8) & MASK_8BITS);
                    break;
            }
        }
    }

    private void resizeBuffer() {
        if (buffer == null) {
            buffer = new byte[DEFAULT_BUFFER_SIZE];
            pos = 0;
            readPos = 0;
        } else {
            byte[] b = new byte[buffer.length * DEFAULT_BUFFER_RESIZE_FACTOR];
            System.arraycopy(buffer, 0, b, 0, buffer.length);
            buffer = b;
        }
    }
}