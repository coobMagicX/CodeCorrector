public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Ensure that the length of the array is equal to the requested sample size
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    // Iterate over the desired sample size and fill the array with sampled values
    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }

    return out;
}