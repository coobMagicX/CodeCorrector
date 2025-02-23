private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        // Determine the best mock by comparing types, selecting the most specific type match.
        Object bestMock = null;
        for (Object mock : mocks) {
            if (field.getType().isAssignableFrom(mock.getClass())) {
                if (bestMock == null || bestMock.getClass().isAssignableFrom