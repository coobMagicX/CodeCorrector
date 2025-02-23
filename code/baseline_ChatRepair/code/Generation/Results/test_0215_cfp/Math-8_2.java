public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                                               sampleSize);
    }

    
    if (singletons == null || singletons.isEmpty()) {
        throw new IllegalArgumentException("The singletons collection must not be empty.");
    }

    
    Class<?> componentType = singletons.get(0).getClass();
    @SuppressWarnings("unchecked")
