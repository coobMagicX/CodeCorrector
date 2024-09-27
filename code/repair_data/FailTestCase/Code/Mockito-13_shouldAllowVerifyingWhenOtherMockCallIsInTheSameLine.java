    public void shouldAllowVerifyingWhenOtherMockCallIsInTheSameLine() {
        //given
        when(mock.otherMethod()).thenReturn("foo");
        
        //when
        mockTwo.simpleMethod("foo");
        
        //then
        verify(mockTwo).simpleMethod(mock.otherMethod());
        try {
            verify(mockTwo, never()).simpleMethod(mock.otherMethod());
            fail();
        } catch (NeverWantedButInvoked e) {}
    }