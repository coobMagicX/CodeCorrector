private void injectMockCandidate(Class<?> awaitingInjectionClazz, Set<Object> mocks, Object fieldInstance) {
    // Create a map to store the best match for each type
    Map<Class<?>, Object> bestMatches = new HashMap<>();

    for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
        // Get the type of the field
        Class<?> fieldType = field.getType();

        // Filter candidates for this type and select the best one
        Object bestMatchForType = mockCandidateFilter.filterBestCandidate(mocks, fieldType);

        // If this is the first time we are encountering this type or if we found a better match,
        // then store it as the new best match.
        bestMatches.computeIfAbsent(fieldType, t -> bestMatchForType);

        // Check if the field has not been previously injected
        if (!bestMatches.containsKey(fieldType)) {
            bestMatches.put(fieldType, bestMatchForType);
            // Now inject the mock into the fieldInstance
            try {
                ReflectionUtil.setField(fieldInstance, field, bestMatchForType);
            } catch (IllegalAccessException e) {
                new Reporter().reportError("Failed to inject mock for field " + field.getName(), e);
            }
        }
    }
}

// Helper class or method that would contain the logic to actually set the field value.
private static final class ReflectionUtil {
    public static void setField(Object instance, Field field, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }
}