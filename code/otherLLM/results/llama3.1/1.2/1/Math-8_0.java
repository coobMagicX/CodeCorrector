public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    if (singletons.size() != singletons.get(0).getClass().getFields().length) {
        throw new IllegalArgumentException("All singleton distributions must have the same number of fields");
    }

    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }

    return out;
}