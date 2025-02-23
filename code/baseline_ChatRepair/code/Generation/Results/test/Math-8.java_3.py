public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Use an array of Object first and then cast it to generic array T[]
    Object[] outObject = new Object[sampleSize];
    for (int i = 0; i < sampleSize; i++) {
        outObject[i] = sample();
    }

    // Now safely cast the array to the generic type array
    @SuppressWarnings("unchecked")
    final T[] out = (T[]) outObject;

    return out;
}
