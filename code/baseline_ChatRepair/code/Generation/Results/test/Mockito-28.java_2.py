private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        // Initialize the best mock variable.
        Object bestMock = null;
        
        // Find the most appropriate mock based on the type compatibility.
        for (Object mock : mocks) {
            if (field.getType().isAssignableFrom(mock.getClass())) {
                if (bestMock == null || !bestMock.getClass().isAssignableFrom(mock.getClass())) {
                    bestMock = mock;
                }
            }
        }

        // If a suitable mock is found, inject it into the field.
        if (bestMock != null) {
           