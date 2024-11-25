private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    Set<Field> uniqueFields = new HashSet<>();
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (!uniqueFields.contains(field)) {
            uniqueFields.add(field);
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        }
    }
}