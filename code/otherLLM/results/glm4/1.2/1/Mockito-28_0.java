private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        // Check if the mock has already been used to avoid duplicates
        boolean isMockUsed = false;
        for (Object mock : mocks) {
            if (mock instanceof FieldMock && ((FieldMock) mock).getField().equals(field)) {
                isMockUsed = true;
                break;
            }
        }

        // If not used, inject the mock candidate for this field
        if (!isMockUsed) {
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        }
    }
}