public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Check type consistency and handle null values
    final Class<?> elementClass = singletons.get(0).getClass();
    for (Object singleton : singletons) {
        if (singleton == null || !elementClass.equals(singleton.getClass())) {
            throw new NotStrictlyPositiveException(LocalizedFormats.INVALID_SINGLETONS, sampleSize);
        }
    }

    // Validate sample size
    final int elements = singletons.size();

    // Check if the sample size is consistent with the number of elements
    if (sampleSize > elements) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_ELEMENTS, elements);
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(elementClass, sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        // Verify singletons consistency
        Object singleton = singletons.get(i % elements);
        out[i] = sample(singleton);
    }

    return out;
}