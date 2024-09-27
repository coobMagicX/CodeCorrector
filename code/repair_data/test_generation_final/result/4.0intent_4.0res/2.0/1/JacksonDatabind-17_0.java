public boolean useForType(JavaType t) {
    switch (_appliesFor) {
    case NON_CONCRETE_AND_ARRAYS:
        while (t.isArrayType()) {
            t = t.getContentType();
        }
        // fall through
    case OBJECT_AND_NON_CONCRETE:
        return (t.getRawClass() == Object.class)
                || (!t.isConcrete()
                        && !TreeNode.class.isAssignableFrom(t.getRawClass())
                        && !InetAddress.class.isAssignableFrom(t.getRawClass())); // Exclude InetAddress from default typing

    case NON_FINAL:
        while (t.isArrayType()) {
            t = t.getContentType();
        }
        // [Issue#88] Should not apply to JSON tree models:
        return !t.isFinal() && !TreeNode.class.isAssignableFrom(t.getRawClass());

    default:
    //case JAVA_LANG_OBJECT:
        return (t.getRawClass() == Object.class);
    }
}