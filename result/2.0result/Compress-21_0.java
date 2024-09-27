private void writeBits(final DataOutput header, final BitSet bits, final int length) throws IOException {
    if (length == 0) {
        return; // Immediately return if there are no bits to write.
    }

    int cache = 0;
    int shift = 7;
    for (int i = 0; i < length; i++) {
        cache |= ((bits.get(i) ? 1 : 0) << shift);
        --shift;
        if (shift < 0) { // When shift is -1, it's time to write the cache and reset
            header.write(cache);
            shift = 7;
            cache = 0;
        }
    }
    if (shift != 7) { // Only write the remaining bits if shift has been decremented
        header.write(cache);
    }
}