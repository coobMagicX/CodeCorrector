public void writeEmbeddedObject(Object object) throws IOException {
    if (object instanceof byte[]) {
        byte[] bytes = (byte[]) object;
        generator.writeBinary(bytes);
    } else {
        throw new JsonGenerationException("No native support for writing embedded objects", this);
    }
}