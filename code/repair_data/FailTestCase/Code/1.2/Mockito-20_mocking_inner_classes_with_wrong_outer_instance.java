    public void mocking_inner_classes_with_wrong_outer_instance() {
        try {
            //when
            mock(InnerClass.class, withSettings().useConstructor().outerInstance("foo").defaultAnswer(CALLS_REAL_METHODS));
            //then
            fail();
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'InnerClass'", e.getMessage());
            assertContains("Please ensure that the outer instance has correct type and that the target class has 0-arg constructor.", e.getCause().getMessage());
        }
    }