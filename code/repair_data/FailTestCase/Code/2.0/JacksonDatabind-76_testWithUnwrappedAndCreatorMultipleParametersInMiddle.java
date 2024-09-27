    public void testWithUnwrappedAndCreatorMultipleParametersInMiddle() throws Exception {
        final String json = aposToQuotes("{'first_name':'John','animal_id':1234,'last_name':'Doe','living':true,'years_old':30}");

        final ObjectMapper mapper = new ObjectMapper();
        Animal animal = mapper.readValue(json, Animal.class);
        assertEquals(1234, animal.getId());
        assertNotNull(animal.getName());
        assertEquals("John", animal.getName().getFirst());
        assertEquals("Doe", animal.getName().getLast());
        assertEquals(30, animal.getAge());
        assertEquals(true, animal.isAlive());
    }