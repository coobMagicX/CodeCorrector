private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        if (mocks.contains(field.getType())) { // Check if mock candidate exists
            mockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject(); // Filter and inject mock candidate
        } else {
            // Handle edge case: No matching mock candidate found for this field
            throw new RuntimeException("No mock candidate available for " + field.getName() + " of type " + field.getType());
        }
    }
}