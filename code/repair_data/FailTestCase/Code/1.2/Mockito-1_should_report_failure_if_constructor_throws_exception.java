    public void should_report_failure_if_constructor_throws_exception() throws Exception {
        given(resolver.resolveTypeInstances(Matchers.<Class<?>[]>anyVararg())).willReturn(new Object[]{ null });

        try {
            new ParameterizedConstructorInstantiator(this, field("withThrowingConstructor"), resolver).instantiate();
            fail();
        } catch (MockitoException e) {
            assertThat(e.getMessage()).contains("constructor").contains("raised an exception");
        }
    }