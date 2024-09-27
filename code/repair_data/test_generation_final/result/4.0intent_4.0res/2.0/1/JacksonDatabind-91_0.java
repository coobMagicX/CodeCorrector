private boolean _hasCustomHandlers(JavaType t) {
    if (t.isContainerType()) {
        // First: value types may have both value and type handlers
        JavaType ct = t.getContentType();
        if (ct != null) {
            if ((ct.getValueHandler() != null) || (ct.getTypeHandler() != null)) {
                return true;
            }
        }
        // Second: map(-like) types may have value handler for key (but not type; keys are untyped)
        JavaType kt = t.getKeyType();
        if (kt != null && kt.getValueHandler() != null) {
            return true;
        }
    } else {
        // Check if non-container type has custom handlers
        if (t.getRawClass() == InetAddress.class) {
            return (t.getValueHandler() != null) || (t.getTypeHandler() != null);
        }
    }
    return false;
}