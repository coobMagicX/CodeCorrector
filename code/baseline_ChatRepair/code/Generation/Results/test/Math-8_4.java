public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }

    
    @SuppressWarnings("unchecked")
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(getComponentType(), sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        out[i] = sample();
    }

    return out;
}


private