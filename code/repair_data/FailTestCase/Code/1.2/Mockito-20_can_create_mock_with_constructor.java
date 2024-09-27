    public void can_create_mock_with_constructor() {
        Message mock = mock(Message.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));
        //the message is a part of state of the mocked type that gets initialized in constructor
        assertEquals("hey!", mock.getMessage());
    }