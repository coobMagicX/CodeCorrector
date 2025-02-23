private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Optional<Object> bestMatchMock = findBestMatchMock(mocks, field);
        if (bestMatchMock.isPresent()) {
            try {
                field.setAccessible(true);
                field.set(fieldInstance, bestMatchMock.get());
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to inject mock into field: " + field.getName(), e);
            }
        }
    }
}

private Optional<Object> findBestMatchMock(Set<Object> mocks, Field field) {
    return mocks.stream()
                .filter(mock -> field.getType().isAssignableFrom(mock.getClass()))
                .findFirst();
}
``