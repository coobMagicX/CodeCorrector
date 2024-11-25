public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(
            singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        out[i] = generateFromDist(sample());
    }

    return out;
}

private T generateFromDist(T value) {
    // Assuming the input list represents a discrete distribution where each pair is in the format of (value, probability)
    int index = (int) (Math.random() * singletons.size()); // Select an element from the list based on its probability
    return singletons.get(index);
}