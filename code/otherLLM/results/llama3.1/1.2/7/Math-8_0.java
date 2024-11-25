public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Verify array initialization for correct type and size
    if (!singletons.get(0).getClass().isArray()) {
        throw new RuntimeException("Singleton is not an array");
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    // Ensure correct handling of singletons in reflection-based array creation
    for (int i = 0; i < sampleSize; i++) {
        // Investigate the `sample()` method to ensure it returns an object with the expected type and length
        final T obj = sample();
        if (!(obj instanceof T)) {
            throw new RuntimeException("Sampled object is not of type " + singletons.get(0).getClass().getName());
        }
        out[i] = obj;
    }

    return out;

}