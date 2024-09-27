    public void shouldUseGenericlessAnnotatedCaptor() {
        //when
        createPerson("Wes", "Williams");
        
        //then
        verify(peopleRepository).save((Person) genericLessCaptor.capture());
        assertEquals("Wes", ((Person) genericLessCaptor.getValue()).getName());
        assertEquals("Williams", ((Person) genericLessCaptor.getValue()).getSurname());
    }  