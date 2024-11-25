public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Since the intent is to return an array of length 1 when sampleSize is 1, we modify this condition
    if (sampleSize == 1) {
        final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), 1); // Create an array with size 1
        out[0] = singletons.get(0); // Assign the first singleton to the output array
        return out;
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        out[i] = singletons.get(i % singletons.size()); // Loop through the singletons list to get samples
    }

    return out;
}