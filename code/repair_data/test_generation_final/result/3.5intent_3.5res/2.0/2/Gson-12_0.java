@Override public void skipValue() throws IOException {
    if (peek() == JsonToken.NAME) {
        nextName();
        pathNames[stackSize - 2] = "null";
        pathIndices[stackSize - 1]++;
    } else if (peek() == JsonToken.STRING || peek() == JsonToken.NUMBER || peek() == JsonToken.BOOLEAN) {
        pathIndices[stackSize - 1]++;
    } else {
        popStack();
        pathNames[stackSize - 1] = "null";
        pathIndices[stackSize - 1]++;
    }
}