    public void shouldScreamWhenWrongTypeForCaptor() {
        try {
            MockitoAnnotations.initMocks(new WrongType());
            fail();
        } catch (MockitoException e) {}
    }