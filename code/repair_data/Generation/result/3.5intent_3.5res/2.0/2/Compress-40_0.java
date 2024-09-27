        public long readBits(final int count) throws IOException {
            if (count < 0 || count > MAXIMUM_CACHE_SIZE) {
                throw new IllegalArgumentException("count must not be negative or greater than " + MAXIMUM_CACHE_SIZE);
            }
            while (bitsCachedSize < count) {
                final long nextByte = in.read();
                if (nextByte < 0) {
                    return nextByte;
                }
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    if (bitsCachedSize + 8 > 56) {
                        // Skip shifting if bitsCachedSize is already greater than or equal to 57
                        break;
                    }
                    bitsCached |= (nextByte << bitsCachedSize);
                } else {
                    if (bitsCachedSize + 8 > 56) {
                        // Skip shifting if bitsCachedSize is already greater than or equal to 57
                        break;
                    }
                    bitsCached <<= 8;
                    bitsCached |= nextByte;
                }
                bitsCachedSize += 8;
            }
    
            final long bitsOut;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                bitsOut = (bitsCached & MASKS[count]);
                bitsCached >>>= count;
            } else {
                bitsOut = (bitsCached >> (bitsCachedSize - count)) & MASKS[count];
            }
            bitsCachedSize -= count;
            return bitsOut;
        }