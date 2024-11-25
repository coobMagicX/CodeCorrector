private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    // Assume that 'mockCandidateFilter' is a Filter interface with the method 'filterCandidate' and an abstract method 'thenInject'
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        boolean shouldInject = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        if (shouldInject) {
            // If filter returns true, it means it's a candidate for injection
            thenInject.apply(field, fieldInstance); // Assuming 'thenInject' is a method reference to an apply-like method on the Filter interface
        }
    }
}