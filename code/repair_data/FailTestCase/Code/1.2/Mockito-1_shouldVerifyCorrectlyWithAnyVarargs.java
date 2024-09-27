    public void shouldVerifyCorrectlyWithAnyVarargs() {
        //when
        table.newRow("qux", "foo", "bar", "baz");
        table.newRow("abc", "def");
        
        //then
        verify(table, times(2)).newRow(anyString(), (String[]) anyVararg());
    }