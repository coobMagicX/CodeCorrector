public <T> T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    // Retrieve the type class of T to use for array creation and element assignment
    final Class<T> clazz = (Class<T>) singletons.get(0).getClass();

    @SuppressWarnings("unchecked")
    final T[] out = (T[]) java.lang.reflect.Array.newInstance(clazz, sampleSize);

    for (int i = 0; i < sampleSize; i++) {
        // Assuming 'sample()' is a method that returns an object of type T or its subclass
        T element = sample();
        
        // Check if the returned object is compatible with type T before assigning it
        if (clazz.isAssignableFrom(element.getClass())) {
            out[i] = element;
        } else {
            throw new IllegalArgumentException("The element returned from 'sample()' is not of expected type " + clazz.getName());
        }
    }

    return out;
}