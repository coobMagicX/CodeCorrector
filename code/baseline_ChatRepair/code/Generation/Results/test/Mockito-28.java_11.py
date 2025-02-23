private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        // Select the most suitable mock based on their compatibility with the field's type
        Object bestMatchMock = findBestMatchMock(mocks, field.getType());
        if (bestMatchMock != null) {
            try {
                field.setAccessible(true);
                field.set(fieldInstance, bestMatchMock);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to inject mock into field: " + field.getName(), e);
            }
       