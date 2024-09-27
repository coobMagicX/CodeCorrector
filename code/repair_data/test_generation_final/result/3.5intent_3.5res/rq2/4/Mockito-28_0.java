private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        MockCandidate mockCandidate = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        if (mockCandidate != null) {
            mockCandidate.thenInject();
        }
    }
}