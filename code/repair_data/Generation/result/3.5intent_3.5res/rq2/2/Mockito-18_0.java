@Test
public void testReturnValueFor() {
    Object returnValue = returnValueFor(String.class);
    assertTrue(returnValue instanceof HashMap<?, ?>);
}