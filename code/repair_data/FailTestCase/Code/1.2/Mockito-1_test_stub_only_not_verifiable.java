    public void test_stub_only_not_verifiable() throws Exception {
        IMethods localMock = mock(IMethods.class, withSettings().stubOnly());

        when(localMock.objectReturningMethod(isA(Integer.class))).thenReturn(100);
        when(localMock.objectReturningMethod(200)).thenReturn(200);

        assertEquals(200, localMock.objectReturningMethod(200));
        assertEquals(100, localMock.objectReturningMethod(666));
        assertEquals("default behavior should return null", null, localMock.objectReturningMethod("blah"));

        try {
            verify(localMock, atLeastOnce()).objectReturningMethod(eq(200));
            fail();
        } catch (CannotVerifyStubOnlyMock e) {}
    }