private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Optional<Object> bestMatchMock = findBestMatchMock(mocks, field);
        if (bestMatchMock.isPresent()) {
            try {
                field.setAccessible(true);
                field.set(fieldInstance, bestMatchMock.get());
            } catch (IllegalAccessException e