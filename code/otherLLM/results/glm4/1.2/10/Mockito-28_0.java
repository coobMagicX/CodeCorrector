private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Optional<Object> mockToInject = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        if (mockToInject.isPresent()) {
            // Inject the mock only once per field instance to avoid duplicate injections.
            Object existingMock = field.get(fieldInstance);
            if (existingMock != null && !existingMock.equals(mockToInject.get())) {
                throw new IllegalStateException("Duplicate injection attempt for field: " + field.getName());
            }
            field.set(fieldInstance, mockToInject.get());
        }
    }
}