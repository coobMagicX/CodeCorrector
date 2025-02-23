private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        // Get the most appropriate mock for the field
        Optional<Object> bestMock = mocks.stream()
            .filter(mock -> field.getType().isAssignableFrom(mock.getClass())) // Check type compatibility
           