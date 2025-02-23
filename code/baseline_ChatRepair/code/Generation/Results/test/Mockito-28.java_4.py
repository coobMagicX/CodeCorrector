private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Object bestCandidate = selectMostSpecificMock(mocks, field);
        if (bestCandidate != null) {
            mockCandidateFilter.filterCandidate(bestCandidate, field, fieldInstance).thenInject();
        }
    }
}

private Object selectMostSpecificMock(Set<Object> mocks, Field field) {
    Class<?> fieldType = field.getType();
    Object bestMatch = null;
    for (Object mock : mocks) {
        if (field