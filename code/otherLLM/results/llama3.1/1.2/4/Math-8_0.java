public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final T[]out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }

    // Validate that the returned array has a correct length
    if (out.length != sampleSize) {
        throw new AssertionError("Sample size mismatch: expected " + sampleSize + ", but got " + out.length);
    }

    return out;
}