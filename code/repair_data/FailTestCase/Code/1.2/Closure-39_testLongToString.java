  public void testLongToString() {
    JSType record = new RecordTypeBuilder(registry)
        .addProperty("a1", NUMBER_TYPE, null)
        .addProperty("a2", NUMBER_TYPE, null)
        .addProperty("a3", NUMBER_TYPE, null)
        .addProperty("a4", NUMBER_TYPE, null)
        .addProperty("a5", NUMBER_TYPE, null)
        .addProperty("a6", NUMBER_TYPE, null)
        .build();
    assertEquals("{a1: number, a2: number, a3: number, a4: number, ...}",
        record.toString());
    assertEquals(
        "{a1: number, a2: number, a3: number, a4: number," +
        " a5: number, a6: number}",
        record.toAnnotationString());
  }