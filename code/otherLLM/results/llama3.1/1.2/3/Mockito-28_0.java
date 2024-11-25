private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (field.getName().equals("mockShouldNotGoInHere")) continue; // Check the field injection process and filter out "mockShouldNotGoInHere"
        mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
    }
}