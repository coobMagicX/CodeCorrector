private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).isBestMatch()) {
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        } else {
            // Use the default value or a fallback object
            field.set(fieldInstance, null); // Or some other default value
        }
    }
}