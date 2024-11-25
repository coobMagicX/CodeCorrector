private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (field.getType().equals(fieldInstance.getClass())) {
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        }
    }
}