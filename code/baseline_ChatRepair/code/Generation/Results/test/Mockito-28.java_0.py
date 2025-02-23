private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    // Sort fields based on their order and specificity. You might already have an implemented method for orderedInstanceFieldsFrom
    for(Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        MockCandidate bestCandidate = null;
        // Iterate through each mock to find the best candidate for the current field
        for (Object mock : mocks) {
            MockCandidate candidate = mock