public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }

    
    T singleSample = sample();
    @SuppressWarnings("unchecked")
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singleSample.getClass(), sampleSize);
    out[0] = singleSample;

    for (int i = 1; i < sampleSize; i++) {
        out[i] = sample();
   