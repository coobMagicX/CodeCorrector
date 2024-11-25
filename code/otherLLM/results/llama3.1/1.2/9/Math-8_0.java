public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    if (sampleSize == 1) {
        // handle the edge case where sampleSize is 1
        return new T[] { sample() };
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }

    return out;
}