private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Object mockToInject = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        if (mockToInject != null) {
            field.setAccessible(true);
            try {
                if (field.getType().isAssignableFrom(mockToInject.getClass())) {
                    field.set(fieldInstance, mockToInject);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}