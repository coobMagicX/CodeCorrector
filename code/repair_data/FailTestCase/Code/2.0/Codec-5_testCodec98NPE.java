    public void testCodec98NPE() throws Exception {
        byte[] codec98 = StringUtils.getBytesUtf8(Base64TestData.CODEC_98_NPE);
        byte[] codec98_1024 = new byte[1024];
        System.arraycopy(codec98, 0, codec98_1024, 0, codec98.length);
        ByteArrayOutputStream data = new ByteArrayOutputStream(1024);
        Base64OutputStream stream = new Base64OutputStream(data, false);
        stream.write(codec98_1024, 0, 1024);
        stream.close();

        byte[] decodedBytes = data.toByteArray();
        String decoded = StringUtils.newStringUtf8(decodedBytes);
        assertEquals(
            "codec-98 NPE Base64OutputStream", Base64TestData.CODEC_98_NPE_DECODED, decoded
        );
    }