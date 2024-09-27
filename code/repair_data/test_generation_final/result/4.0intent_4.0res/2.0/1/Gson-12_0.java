@Override public void skipValue() throws IOException {
    if (stackSize == 1 && peek() == JsonToken.BEGIN_OBJECT) {
        popStack();  // Remove BEGIN_OBJECT from the stack
        if (peek() == JsonToken.END_OBJECT) {
            popStack(); // Remove END_OBJECT from the stack as it is also empty
        }
        push(JsonToken.END_DOCUMENT);  // Push END_DOCUMENT to indicate the end of JSON stream
    } else {
        if (peek() == JsonToken.NAME) {
            nextName();  // Skip the name token
            pathNames[stackSize - 2] = "null";
        } else {
            popStack();  // Remove the current token from stack
            pathNames[stackSize - 1] = "null";
        }
        pathIndices[stackSize - 1]++;  // Increment the path index for the current level
    }
}