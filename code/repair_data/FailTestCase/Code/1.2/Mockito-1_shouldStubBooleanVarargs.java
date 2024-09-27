    public void shouldStubBooleanVarargs() {
        when(mock.withBooleanVarargs(1)).thenReturn(true);
        when(mock.withBooleanVarargs(1, true, false)).thenReturn(true);
        
        assertEquals(true, mock.withBooleanVarargs(1));
        assertEquals(false, mock.withBooleanVarargs(9999));
        
        assertEquals(true, mock.withBooleanVarargs(1, true, false));
        assertEquals(false, mock.withBooleanVarargs(1, true, false, true));
        assertEquals(false, mock.withBooleanVarargs(2, true, false));
        assertEquals(false, mock.withBooleanVarargs(1, true));
        assertEquals(false, mock.withBooleanVarargs(1, false, false));
    }