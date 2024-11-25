JSType meet(JSType that) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    for (JSType alternate : alternates) {
        if (alternate.isSubtype(that)) {
            builder.addAlternate(alternate);
        }
    }

    if (that instanceof UnionType) {
        for (JSType otherAlternate : ((UnionType) that).alternates) {
            if (!otherAlternate.isError() && otherAlternate.isSubtype(this)) { // Adjusted condition to exclude error types
                builder.addAlternate(otherAlternate);
            }
        }
    } else if (!that.isError() && that.isSubtype(this)) { // Adjusted condition to exclude error types
        builder.addAlternate(that);
    }
    JSType result = builder.build();
    if (result != null) {
        return result;
    } else if (this.isObject() && that.isObject()) {
        return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    } else if (!this.isError() && !that.isError()) { // Ensure neither type is an error type
        return getNativeType(JSTypeNative.NO_TYPE);
    }
    return null; // Return null only when both types are error types and no intersection exists
}