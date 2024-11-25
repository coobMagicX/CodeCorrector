private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (isLegalInjection(field, fieldInstance, mocks)) {
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        }
    }
}

// Helper method to determine if the injection is legal
private boolean isLegalInjection(Field field, Object fieldInstance, Set<Object> mocks) {
    // Check if the field type matches any of the mock types in the mocks set
    for (Object mock : mocks) {
        if (mock != null && field.getType().isAssignableFrom(mock.getClass())) {
            return true; // Legal injection if there is a match
        }
    }
    return false; // Illegal injection if no matching type found
}

// Usage of the helper method in the existing context
public void injectMocksOnFields(Set<Field> injectMocksFields, Set<Object> mocks, Object testClassInstance) {
    for (Field field : injectMocksFields) {
        Set<Object> mocksToBeInjected = new HashSet<>(mocks);
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
            fieldClass = fieldClass.getSuperclass();
        }
    }
}