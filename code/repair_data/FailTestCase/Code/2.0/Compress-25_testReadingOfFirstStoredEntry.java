    public void testReadingOfFirstStoredEntry() throws Exception {
        ZipArchiveInputStream in = new ZipArchiveInputStream(new FileInputStream(getFile("COMPRESS-264.zip")));
        
        try {
            ZipArchiveEntry ze = in.getNextZipEntry();
            assertEquals(5, ze.getSize());
            assertArrayEquals(new byte[] {'d', 'a', 't', 'a', '\n'},
                              IOUtils.toByteArray(in));
        } finally {
            in.close();
        }
    }