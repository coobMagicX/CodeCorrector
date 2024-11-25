public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Since we want to return an array with only one element regardless of the sample size,
    // we set sampleSize to be 1.
    sampleSize = 1;

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    // As we have set sampleSize to 1, there is no need for a loop. We can directly assign the value.
    out[0] = sample();

    return out;
}