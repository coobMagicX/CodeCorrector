    public void shouldCaptureGenericList() {
        //given
        List<String> list = new LinkedList<String>();
        mock.listArgMethod(list);
                
        //when
        verify(mock).listArgMethod(genericListCaptor.capture());
        
        //then
        assertSame(list, genericListCaptor.getValue());
    } 