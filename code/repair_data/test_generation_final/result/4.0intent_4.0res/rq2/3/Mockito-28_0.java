private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).isPresent()) {
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).get().thenInject();
        }
    }
}