    public void can_mock_inner_classes() {
        InnerClass mock = mock(InnerClass.class, withSettings().useConstructor().outerInstance(this).defaultAnswer(CALLS_REAL_METHODS));
        assertEquals("hey!", mock.getMessage());
    }