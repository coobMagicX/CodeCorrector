public Number read(JsonReader in) throws IOException {
  JsonToken jsonToken = in.peek();
  switch (jsonToken) {
    case NULL:
      in.nextNull();
      return null;
    case NUMBER:
      String input = in.nextString();
      try {
        return Integer.parseInt(input);
      } catch (NumberFormatException e) {
        try {
          return Long.parseLong(input);
        } catch (NumberFormatException ex) {
          try {
            return Float.parseFloat(input);
          } catch (NumberFormatException exc) {
            try {
              return Double.parseDouble(input);
            } catch (NumberFormatException exce) {
              return new LazilyParsedNumber(input);
            }
          }
        }
      }
    default:
      throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
  }
}