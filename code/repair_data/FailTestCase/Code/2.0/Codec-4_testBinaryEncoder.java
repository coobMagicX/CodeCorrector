    public void testBinaryEncoder() throws EncoderException {
        BinaryEncoder enc = new Base64();
        for (int i = 0; i < STRINGS.length; i++) {
            if (STRINGS[i] != null) {
                byte[] base64 = utf8(STRINGS[i]);
                byte[] binary = BYTES[i];
                boolean b = Arrays.equals(base64, enc.encode(binary));
                assertTrue("BinaryEncoder test-" + i, b);
            }
        }
    }