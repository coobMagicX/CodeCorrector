    public void can_instantiate_class_with_parameterized_constructor() throws Exception {
        ConstructorArgumentResolver resolver = given(mock(ConstructorArgumentResolver.class).resolveTypeInstances(any(Class[].class)))
                        .willReturn(new Object[]{null}).getMock();

        new FieldInitializer(this, field("noDefaultConstructor"), resolver).initialize();

        assertNotNull(noDefaultConstructor);
    }