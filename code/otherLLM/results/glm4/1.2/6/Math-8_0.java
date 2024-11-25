public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Ensure that 'singletons' is not null and has at least one singleton
    if (singletons == null || singletons.isEmpty()) {
        throw new IllegalArgumentException("No singletons available for sampling.");
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    // Initialize the array elements to ensure they are distinct objects, if necessary
    if (!singletons.get(0).getClass().isArray()) {
        for (int i = 0; i < sampleSize; i++) {
            out[i] = sample();
        }
    } else {
        // If singletons are arrays, ensure the array is properly populated
        Object[] singletonArray = singletons.get(0);
        System.arraycopy(singletonArray, 0, out, 0, sampleSize);
    }

    return out;
}