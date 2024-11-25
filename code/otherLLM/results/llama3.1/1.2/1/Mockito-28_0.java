private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (field.getName().equals("reference")) {
            mockShouldNotGoInHere = null;
        } else {
            MockCandidate candidate = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
            if (candidate != null && candidate.getObjectType().equals(field.getType())) {
                field.setAccessible(true);
                try {
                    field.set(fieldInstance, candidate.getInstance());
                } catch (IllegalAccessException e) {
                    // Handle exception
                }
            }
        }
    }
}