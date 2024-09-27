    public void shouldUseCaptorInOrdinaryWay() {
        //when
        createPerson("Wes", "Williams");
        
        //then
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(peopleRepository).save(captor.capture());
        assertEquals("Wes", captor.getValue().getName());
        assertEquals("Williams", captor.getValue().getSurname());
    }