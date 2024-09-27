  @Override public void skipValue() throws IOException {
    if (peek() == JsonToken.NAME) {
      nextName();
      pathNames[stackSize - 2] = "null";
    } else if (peek() == JsonToken.BEGIN_OBJECT) {
      beginObject();
      while (hasNext()) {
        skipValue();
      }
      endObject();
    } else {
      popStack();
      pathNames[stackSize - 1] = "null";
    }
    pathIndices[stackSize - 1]++;
  }