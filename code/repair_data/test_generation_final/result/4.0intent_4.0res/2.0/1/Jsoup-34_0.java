int nextIndexOf(CharSequence seq) {
    char startChar = seq.charAt(0);
    for (int offset = pos; offset < length; offset++) {
        // scan to first instance of startchar:
        if (startChar != input[offset]) {
            while (++offset < length && startChar != input[offset]);
        }
        if (offset < length) {
            int i = offset + 1;
            int last = i + seq.length() - 1;
            if (last > length) // Check if the remaining length is less than the sequence length
                return -1;
            for (int j = 1; j < seq.length() && seq.charAt(j) == input[i]; i++, j++);
            if (i == last && seq.charAt(seq.length() - 1) == input[last - 1]) // Ensure full sequence matched
                return offset - pos;
        }
    }
    return -1;
}