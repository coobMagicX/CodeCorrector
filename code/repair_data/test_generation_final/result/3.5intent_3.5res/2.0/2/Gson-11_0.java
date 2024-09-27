public Number read(JsonReader in) throws IOException {
  JsonToken jsonToken = in.peek();
  switch (jsonToken) {
  case NULL:
    in.nextNull();
    return null;
  case NUMBER:
    String numberString = in.nextString();
    try {
      if (numberString.contains(".")) {
        return Double.parseDouble(numberString);
      } else {
        return Long.parseLong(numberString);
      }
    } catch (NumberFormatException e) {
      throw new JsonSyntaxException("Invalid number format: " + numberString);
    }
  default:
    throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
  }
}