public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        // Ensure that we do not exceed the bounds of the array
        if (i >= out.length) {
            throw new ArrayIndexOutOfBoundsException("Sample size exceeds allocated array length.");
        }
        out[i] = sample();
    }

    return out;
}