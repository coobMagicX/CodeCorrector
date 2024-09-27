public Number read(JsonReader in) throws IOException {
  JsonToken jsonToken = in.peek();
  switch (jsonToken) {
  case NULL:
    in.nextNull();
    return null;
  case NUMBER:
  case STRING: // Handle numeric values provided as strings
    return new LazilyParsedNumber(in.nextString());
  default:
    throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
  }
}