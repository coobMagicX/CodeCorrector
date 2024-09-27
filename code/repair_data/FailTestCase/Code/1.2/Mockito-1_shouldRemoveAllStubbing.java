    public void shouldRemoveAllStubbing() throws Exception {
        when(mock.objectReturningMethod(isA(Integer.class))).thenReturn(100);
        when(mock.objectReturningMethod(200)).thenReturn(200);
        reset(mock);
        assertNull(mock.objectReturningMethod(200));
        assertEquals("default behavior should return null", null, mock.objectReturningMethod("blah"));
    }