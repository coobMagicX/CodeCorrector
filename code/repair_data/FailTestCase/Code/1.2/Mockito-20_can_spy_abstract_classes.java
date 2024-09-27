    public void can_spy_abstract_classes() {
        AbstractMessage mock = spy(AbstractMessage.class);
        assertEquals("hey!", mock.getMessage());
    }