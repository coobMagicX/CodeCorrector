    public void shouldAllowPassingNullArgument() {
        //given
        Fun mockFun = mock(Fun.class);
        when(mockFun.doFun((String) anyObject())).thenReturn("value");

        //when
        mockFun.doFun(null);

        //then
        try {
            verify(mockFun).doFun("hello");
        } catch(AssertionError r) {
            //it's ok, we just want to reproduce the bug
        }
    }