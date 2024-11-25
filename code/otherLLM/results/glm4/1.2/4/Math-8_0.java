public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Ensure that we are only creating an array of length 1 when sampleSize is 1
    final T[] out = java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize == 1 ? 1 : sampleSize);

    for (int i = 0; i < out.length; i++) { // Use 'out.length' to ensure it matches the correct array size
        out[i] = sample();
    }

    return out;
}