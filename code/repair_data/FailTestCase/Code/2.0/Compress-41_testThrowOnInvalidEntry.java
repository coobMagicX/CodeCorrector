    public void testThrowOnInvalidEntry() throws Exception {
        final InputStream is = ZipArchiveInputStreamTest.class
                .getResourceAsStream("/invalid-zip.zip");
        final ZipArchiveInputStream zip = new ZipArchiveInputStream(is);

        try {
            zip.getNextZipEntry();
            fail("IOException expected");
        } catch (ZipException expected) {
            assertTrue(expected.getMessage().contains("Unexpected record signature"));
        } finally {
            zip.close();
        }
    }