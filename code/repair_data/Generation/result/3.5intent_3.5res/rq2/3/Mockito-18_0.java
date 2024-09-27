@Test
public void testReturnValueFor_MapClass_ReturnsNewHashMap() {
    Object returnValue = returnValueFor(Map.class);
    assertTrue(returnValue instanceof HashMap);
}