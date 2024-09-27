    public void shouldMatchEasilyEmptyVararg() throws Exception {
        //when
        when(mock.foo(anyVararg())).thenReturn(-1);

        //then
        assertEquals(-1, mock.foo());
    } 