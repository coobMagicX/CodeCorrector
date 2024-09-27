private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Object mockCandidate = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        if (mockCandidate != null && field.getType().isAssignableFrom(mockCandidate.getClass())) {
            field.setAccessible(true);
            try {
                field.set(fieldInstance, mockCandidate);
            } catch (IllegalAccessException e) {
                // handle exception
            }
        }
    }
}