public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final int singletonCount = singletons.size(); // Assuming there is a method to get the size of the singleton collection
    if (singletonCount == 0) {
        throw new IllegalArgumentException("Singletons list cannot be empty");
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        // Ensure that the singleton collection is not empty before sampling
        if (singletonCount > 0) {
            out[i] = singletons.get(0);
        }
    }

    return out;
}