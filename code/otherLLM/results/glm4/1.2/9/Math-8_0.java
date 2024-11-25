public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final List<Pair<T, Double>> samples = getSamples();
    final int singletonIndex = sampleSize == 1 ? 0 : -1; // Use the first singleton if sampleSize is 1

    if (sampleSize == 1) {
        T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), 1);
        out[0] = singletons.get(singletonIndex); // Assign the singleton to the array
        return out;
    } else {
        final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

        for (int i = 0; i < sampleSize; i++) {
            out[i] = singletons.get(singletonIndex); // Assign the singleton to each element of the array
        }

        return out;
    }
}