public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }

    
    
    if (singletons == null || singletons.isEmpty()) {
        throw new IllegalArgumentException("Singletons list must not be empty.");
    }

    
    T firstSample = sample();

   