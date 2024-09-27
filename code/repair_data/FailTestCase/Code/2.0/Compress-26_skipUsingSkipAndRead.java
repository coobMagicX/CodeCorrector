    public void skipUsingSkipAndRead() throws Exception {
        skip(new StreamWrapper() {
                public InputStream wrap(final InputStream toWrap) {
                    return new FilterInputStream(toWrap) {
                        boolean skipped;
                        public long skip(long s) throws IOException {
                            if (!skipped) {
                                toWrap.skip(5);
                                skipped = true;
                                return 5;
                            }
                            return 0;
                        }
                    };
                }
            });
    }