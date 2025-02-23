public int[] getCounts(int index) {
    if (index < 0 || index >= totalSize) {
        throw new OutOfRangeException(index, 0, totalSize);
    }

    final int[] indices = new int[dimension];
    
    int count = 0;
    for (int i = 0; i <= last; i++) {
        int idx = 0;
        final int offset = (i < last) ? uniCounterOffset[i] : 1;  

        
        while (count + offset <= index) {
            count += offset;
            idx++;
        }
        
        indices[i] = idx;
    }

    return indices;
}
