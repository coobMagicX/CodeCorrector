public Date read(JsonReader in) throws IOException {
  if (in.peek() != JsonToken.STRING) {
    throw new JsonParseException("The date should be a string value");
  }
  String dateString = in.nextString();
  try {
    return deserializeToDate(dateString);
  } catch (ParseException e) {
    throw new JsonSyntaxException("Failed to parse date: " + dateString, e);
  }
}

private Date deserializeToDate(String s) throws ParseException {
  synchronized (localFormat) {
    try {
      return localFormat.parse(s);
    } catch (ParseException ignored) {}
    try {
      return enUsFormat.parse(s);
    } catch (ParseException ignored) {}
    try {
      return ISO8601Utils.parse(s, new ParsePosition(0));
    } catch (ParseException e) {
      throw new JsonSyntaxException("Failed to parse date: " + s, e);
    }
  }
}