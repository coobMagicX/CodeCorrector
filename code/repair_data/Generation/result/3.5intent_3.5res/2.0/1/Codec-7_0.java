@Test
public void testByteToStringVariations() {
    byte[] binaryData = "Hello World".getBytes();
    String expected = "SGVsbG8gV29ybGQ="; // update this with the correct expected value

    String actual = Base64.encodeBase64String(binaryData);

    assertEquals(expected, actual);
}