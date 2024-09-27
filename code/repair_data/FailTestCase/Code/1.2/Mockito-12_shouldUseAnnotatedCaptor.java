    public void shouldUseAnnotatedCaptor() {
        //when
        createPerson("Wes", "Williams");
        
        //then
        verify(peopleRepository).save(captor.capture());
        assertEquals("Wes", captor.getValue().getName());
        assertEquals("Williams", captor.getValue().getSurname());
    }