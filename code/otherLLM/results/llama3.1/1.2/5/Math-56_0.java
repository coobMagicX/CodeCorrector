public int[] getCounts(int index) {
    if (index < 0 ||
        index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    int count = 0;
    for (int i = 0; i <= last; i++) { // Fix: iterate up to 'last' inclusive
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count < index) {
            count += offset;
            ++idx;
        }
        --idx;
        count -= offset;
        indices[i] = idx;
    }

    return indices; // Fix: remove incorrect 'while' loop
}