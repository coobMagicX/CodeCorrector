    public void bigEndianWithOverflow() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[] {
                87, // 01010111
                45, // 00101101
                66, // 01000010
                15, // 00001111
                90, // 01011010
                29, // 00011101
                88, // 01011000
                61, // 00111101
                33, // 00100001
                74  // 01001010
            });
        BitInputStream bin = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        assertEquals(10, // 01010
                     bin.readBits(5));
        assertEquals(8274274654740644818l, //111-00101101-01000010-00001111-01011010-00011101-01011000-00111101-0010
                     bin.readBits(63));
        assertEquals(330, // 0001-01001010
                     bin.readBits(12));
        assertEquals(-1 , bin.readBits(1));
    }