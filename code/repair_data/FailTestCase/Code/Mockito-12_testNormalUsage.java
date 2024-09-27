    public void testNormalUsage() {

        MockitoAnnotations.initMocks(this);

        // check if assigned correctly
        assertNotNull(finalCaptor);
        assertNotNull(genericsCaptor);
        assertNotNull(nonGenericCaptorIsAllowed);
        assertNull(notAMock);

        // use captors in the field to be sure they are cool
        String argForFinalCaptor = "Hello";
        ArrayList<List<String>> argForGenericsCaptor = new ArrayList<List<String>>();

        mockInterface.testMe(argForFinalCaptor, argForGenericsCaptor);

        Mockito.verify(mockInterface).testMe(finalCaptor.capture(), genericsCaptor.capture());

        assertEquals(argForFinalCaptor, finalCaptor.getValue());
        assertEquals(argForGenericsCaptor, genericsCaptor.getValue());

    }