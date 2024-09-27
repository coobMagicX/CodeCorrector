    public void should_instantiate_type_with_vararg_constructor() throws Exception {
        Observer[] vararg = new Observer[] {  };
        given(resolver.resolveTypeInstances(Matchers.<Class<?>[]>anyVararg())).willReturn(new Object[]{ "", vararg});

        new ParameterizedConstructorInstantiator(this, field("withVarargConstructor"), resolver).instantiate();

        assertNotNull(withVarargConstructor);
    }