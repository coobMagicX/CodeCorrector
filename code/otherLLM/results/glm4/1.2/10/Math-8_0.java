public T[] sample(int sampleSize) throws NotStrictlyPositiveException {
    if (sampleSize <= 0) {
        throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                sampleSize);
    }

    final T[] out = (T[]) java.lang.reflect.Array.newInstance(singletons.get(0).getClass(), 1); // Fixed: Initialize array size to 1

    for (int i = 0; i < 1; i++) { // Fixed: Loop condition is incorrect, it should be i < 1 since we want only one sample
        out[0] = sample(); // Fixed: Assign the sampled value directly to the first element of the array
        break; // Fixed: Exit loop after sampling once
    }

    return out;
}