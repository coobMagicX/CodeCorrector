public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    
    if (singletons.isEmpty()) {
        throw new IllegalArgumentException("The list of singletons cannot be empty.");
    }

    
    Class<?> componentType = single