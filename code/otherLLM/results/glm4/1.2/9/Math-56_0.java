public int[] getCounts(int index) {
    if (index < 0 ||
        index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];

    int count = 0;
    for (int i = 0; i < last; i++) {
        int idx = 0;
        final int offset = uniCounterOffset[i];
        while (count <= index) {
            count += offset;
            ++idx;
        }
        // The previous calculation of count already overshoots the target, so no need to subtract offset
        indices[i] = idx - 1; // Directly assign after adjusting for overshooting
    }

    int idx = 0;
    while (count < index) {
        count += idx;
        ++idx;
    }
    // Adjust idx if it has been incremented one time too many
    if (count > index) {
        --idx;
    }
    indices[last] = idx;

    return indices;
}