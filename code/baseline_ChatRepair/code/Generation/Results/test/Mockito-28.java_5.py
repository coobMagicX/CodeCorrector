private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Object bestMatch = findBestMatchingMock(mocks, field);
        if (bestMatch != null) {
            mockCandidateFilter.filterCandidate(Collections.singleton(bestMatch), field, fieldInstance).thenInject();
        }
    }
}

private Object findBestMatchingMock(Set<Object> mocks, Field field) {
    Class<?> fieldType = field.getType();
    Object bestMatch = null;
    for (Object mock : mocks) {
        if (mock != null && fieldType.is