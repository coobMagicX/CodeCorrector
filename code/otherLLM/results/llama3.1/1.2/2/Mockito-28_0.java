private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)) { // Filter candidate before injection
            thenInject(field); // Inject mock into specific field
        }
    }
}

private void thenInject(Field field) {
    // Assuming this method is already implemented and available for use
}