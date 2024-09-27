    public void exception_message_when_constructor_not_found() {
        try {
            //when
            spy(HasConstructor.class);
            //then
            fail();
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'HasConstructor'", e.getMessage());
            assertContains("0-arg constructor", e.getCause().getMessage());
        }
    }