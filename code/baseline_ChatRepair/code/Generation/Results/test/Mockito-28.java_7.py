private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        // Find the most suitable mock for the field, prioritizing type compatibility
        Optional<Object> bestMatchMock = mocks.stream()
            .filter(mock -> field.getType().isAssignableFrom(mock.getClass()))
            .findFirst();

        bestMatchMock.ifPresent(mock -> mockCandidateFilter.filterCandidate(mocks, field, fieldInstance, mock).thenInject());
    }
}
