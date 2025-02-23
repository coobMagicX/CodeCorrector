public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }

    List<T> resultList = new ArrayList<T>(sampleSize);
    for (int i = 0; i < sampleSize; i++) {
        resultList.add(sample());
    }

    @SuppressWarnings("unchecked")
    T[] out = (T