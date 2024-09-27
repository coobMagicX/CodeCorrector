    public void testWinzipBackSlashWorkaround() throws Exception {
        URL zip = getClass().getResource("/test-winzip.zip");
        File archive = new File(new URI(zip.toString()));
        zf = new ZipFile(archive);
        assertNull(zf.getEntry("\u00e4\\\u00fc.txt"));
        assertNotNull(zf.getEntry("\u00e4/\u00fc.txt"));
    }