    public void testParseOctalInvalid() throws Exception{
        byte [] buffer;
        buffer=new byte[0]; // empty byte array
        try {
            TarUtils.parseOctal(buffer,0, buffer.length);
            fail("Expected IllegalArgumentException - should be at least 2 bytes long");
        } catch (IllegalArgumentException expected) {
        }
        buffer=new byte[]{0}; // 1-byte array
        try {
            TarUtils.parseOctal(buffer,0, buffer.length);
            fail("Expected IllegalArgumentException - should be at least 2 bytes long");
        } catch (IllegalArgumentException expected) {
        }
        buffer = "abcdef ".getBytes(CharsetNames.UTF_8); // Invalid input
        try {
            TarUtils.parseOctal(buffer,0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
        }
        buffer = " 0 07 ".getBytes(CharsetNames.UTF_8); // Invalid - embedded space
        try {
            TarUtils.parseOctal(buffer,0, buffer.length);
            fail("Expected IllegalArgumentException - embedded space");
        } catch (IllegalArgumentException expected) {
        }
        buffer = " 0\00007 ".getBytes(CharsetNames.UTF_8); // Invalid - embedded NUL
        try {
            TarUtils.parseOctal(buffer,0, buffer.length);
            fail("Expected IllegalArgumentException - embedded NUL");
        } catch (IllegalArgumentException expected) {
        }
    }