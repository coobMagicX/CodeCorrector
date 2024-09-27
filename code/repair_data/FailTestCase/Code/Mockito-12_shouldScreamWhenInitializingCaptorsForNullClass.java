    public void shouldScreamWhenInitializingCaptorsForNullClass() throws Exception {
        try {
            MockitoAnnotations.initMocks(null);
            fail();
        } catch (MockitoException e) {
        }
    }