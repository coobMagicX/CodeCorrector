public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, sampleSize);
    }

    
    if (singletons.isEmpty()) {
        throw new IllegalStateException("Cannot create array of type T as no template object (singleton) is available.");
    }

    