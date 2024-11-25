private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (field.getAnnotation(InjectMock.class).injectIfNull()) {
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        }
    }
}