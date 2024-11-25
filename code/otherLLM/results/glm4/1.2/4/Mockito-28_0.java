private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        boolean isCorrectField = field.getType().isAssignableFrom(fieldInstance.getClass());
        if (isCorrectField) { // Check if the type of the fieldInstance matches the type of the field
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        }
    }
}

public void injectMocksOnFields(Set<Field> injectMocksFields, Set<Object> mocks, Object testClassInstance) {
    for (Field field : injectMocksFields) {
        Set<Object> mocksToBeInjected = new HashSet<Object>(mocks);
        Object injectMocksFieldInstance = null;
        try {
            injectMocksFieldInstance = new FieldInitializer(testClassInstance, field).initialize();
        } catch (MockitoException e) {
            new Reporter().cannotInitializeForInjectMocksAnnotation(field.getName(), e);
        }

        // for each field in the class hierarchy
        Class<?> fieldClass = injectMocksFieldInstance.getClass();
        while (fieldClass != Object.class) {
            injectMockCandidate(fieldClass, mocksToBeInjected, injectMocksFieldInstance);
            fieldClass = fieldClass.getSuperclass(); // Correctly iterate through the superclass chain
        }
    }
}