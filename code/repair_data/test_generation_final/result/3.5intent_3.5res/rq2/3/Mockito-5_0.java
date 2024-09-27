@Test
public void testVerify() {
    VerificationData data = mock(VerificationData.class);
    Mockito.when(timer.isCounting()).thenReturn(true, true, true, false);
    Mockito.doThrow(new MockitoAssertionError("Assertion Error")).when(delegate).verify(data);

    try {
        verify(data);
        fail("Expected MockitoAssertionError");
    } catch (MockitoAssertionError e) {
        // Test passed
    }
}