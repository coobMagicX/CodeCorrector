    public void shouldVerifyCorrectlyNumberOfInvocationsUsingAnyVarargAndEqualArgument() {
        //when
        table.newRow("x", "foo", "bar", "baz");
        table.newRow("x", "def");

        //then
        verify(table, times(2)).newRow(eq("x"), (String[]) anyVararg());
    }