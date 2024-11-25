Object returnValueFor(Class<?> type) {
    if (Primitives.isPrimitiveOrWrapper(type)) {
        return Primitives.defaultValueForPrimitiveOrWrapper(type);
    } else if (type == Collection.class) {
        return Collections.emptyList();
    } else if (type == Set.class) {
        return new HashSet<>();
    } else if (type == List.class) {
        return Collections.emptyList();
    } else if (type == Map.class) {
        return Collections.emptyMap();
    } else {
        try {
            return type.newInstance();
        } catch (Exception e) {
            // Handle unsupported collection types
            return null;
        }
    }
}