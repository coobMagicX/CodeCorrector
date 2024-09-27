    public void shouldVerifyBooleanVarargs() {
        mock.withBooleanVarargs(1);
        mock.withBooleanVarargs(2, true, false, true);
        mock.withBooleanVarargs(3, true, true, true);

        verify(mock).withBooleanVarargs(1);
        verify(mock).withBooleanVarargs(2, true, false, true);
        try {
            verify(mock).withBooleanVarargs(3, true, true, true, true);
            fail();
        } catch (ArgumentsAreDifferent e) {}
    }