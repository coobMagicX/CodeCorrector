    public void testBase64EmptyOutputStream() throws Exception {
        byte[] emptyEncoded = new byte[0];
        byte[] emptyDecoded = new byte[0];
        testByteByByte(emptyEncoded, emptyDecoded, 76, CRLF);
        testByChunk(emptyEncoded, emptyDecoded, 76, CRLF);
    }    