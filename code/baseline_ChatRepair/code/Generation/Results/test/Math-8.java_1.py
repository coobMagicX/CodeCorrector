public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    if (singletons == null || singletons.isEmpty()) {
        throw new IllegalStateException("Singletons collection must not be empty");
    }
    
    @SuppressWarnings("unchecked")
    // Using the class of the first item in the list to create the array
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }

    return out;
}
