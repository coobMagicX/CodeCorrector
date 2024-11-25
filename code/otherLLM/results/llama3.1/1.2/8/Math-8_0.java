public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        if (singletons.isEmpty()) { // Check if singletons is empty before getting index 0
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SINGLETONS,
                    1); // Throw exception with correct message
        }
        out[i] = sample();
    }

    return out;
}