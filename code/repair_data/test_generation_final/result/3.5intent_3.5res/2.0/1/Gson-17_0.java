public Date read(JsonReader in) throws IOException {
  if (in.peek() != JsonToken.STRING) {
    throw new JsonParseException("The date should be a string value");
  }
  Date date = deserializeToDate(in.nextString());
  if (dateType == Date.class) {
    return date;
  } else if (dateType == Timestamp.class) {
    return new Timestamp(date.getTime());
  } else if (dateType == java.sql.Date.class) {
    return new java.sql.Date(date.getTime());
  } else {
    // This must never happen: dateType is guarded in the primary constructor
    throw new AssertionError();
  }
  
  // Add the following code to throw IllegalStateException for unexpected tokens
  if (in.peek() == JsonToken.NAME || in.peek() == JsonToken.BEGIN_OBJECT || in.peek() == JsonToken.BEGIN_ARRAY) {
    throw new IllegalStateException("Unexpected token encountered");
  }
}