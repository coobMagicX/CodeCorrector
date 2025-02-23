private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Object bestMatchMock = null;
        // Iterate over mocks to find the best match for the field type
        for (Object mock : mocks) {
            if (field.getType().isAssignableFrom(mock.getClass())) {
                if (bestMatchMock == null || mock.getClass().isAssignableFrom(bestMatchMock.getClass())) {
                    bestMatchMock = mock;
                }
            }
        }
        if (bestMatchMock != null) {
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance, bestMatchMock).thenInject();
        }
   