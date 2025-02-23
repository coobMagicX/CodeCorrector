private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        Object bestCandidate = findBestMockCandidate(mocks, field);
        if (bestCandidate != null) {
            mockCandidateFilter.filterCandidate(Collections.singleton(bestCandidate), field, fieldInstance).