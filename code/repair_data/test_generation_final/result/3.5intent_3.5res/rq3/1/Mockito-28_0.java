private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
    for (Field field : orderedInstanceFields) {
        mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
    }
}