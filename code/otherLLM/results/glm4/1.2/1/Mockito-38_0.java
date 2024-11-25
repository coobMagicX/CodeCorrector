private int[] getSuspiciouslyNotMatchingArgsIndexes(ArgumentMatcher<?>[] matchers, StringDescription... descriptions) {
    if (matchers == null || descriptions == null) {
        throw new IllegalArgumentException("Matchers or descriptions cannot be null");
    }

    List<Integer> nonMatchingIndices = new ArrayList<>();
    
    for (int i = 0; i < Math.min(matchers.length, descriptions.length); i++) {
        Matcher m = matchers[i];
        StringDescription desc = descriptions[i];

        if (m == null || desc == null) {
            // If either matcher or description is null at any index, consider it as a non-matching condition
            nonMatchingIndices.add(i);
        } else {
            // Check for non-matching conditions using the provided methods
            if (!safelyMatches(m, desc.getDescription()) || !toStringEquals(m, desc)) {
                nonMatchingIndices.add(i);
            }
        }
    }

    // Convert the list of non-matching indices to an array
    int[] result = new int[nonMatchingIndices.size()];
    for (int i = 0; i < nonMatchingIndices.size(); i++) {
        result[i] = nonMatchingIndices.get(i);
    }
    
    return result;
}

// Helper methods

private boolean toStringEquals(Matcher m, StringDescription desc) {
    // Ensure that the description is not null
    if (desc == null) {
        throw new IllegalArgumentException("Description cannot be null");
    }

    // Use the existing toStringEquals method with StringDescription instead of Object
    return toStringEquals(m, desc.getDescription());
}

private boolean safelyMatches(Matcher m, StringDescription desc) {
    // Ensure that both matcher and description are not null
    if (m == null || desc == null) {
        throw new IllegalArgumentException("Matcher or description cannot be null");
    }

    try {
        return m.matches(desc.getDescription());
    } catch (Throwable t) {
        return false;
    }
}