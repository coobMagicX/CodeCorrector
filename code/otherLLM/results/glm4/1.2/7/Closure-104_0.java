JSType meet(JSType that) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    for (JSType alternate : alternates) {
        if (alternate.isSubtype(that)) {
            builder.addAlternate(alternate);
        }
    }

    // Ensure the comparison respects the greatest subtype relationship
    JSType result = null;
    boolean isThatUnion = that instanceof UnionType;

    if (isThatUnion) {
        for (JSType otherAlternate : ((UnionType) that).alternates) {
            if (otherAlternate.isSubtype(this)) {
                builder.addAlternate(otherAlternate);
            }
        }
    }

    // Check if 'this' is a subtype of 'that', and add it to the result
    if (isThatUnion || that.isSubtype(this)) {
        builder.addAlternate(that);
    } else {
        // If 'that' is not an instance of UnionType and not a subtype, return NO_TYPE
        return getNativeType(JSTypeNative.NO_TYPE);
    }

    result = builder.build();

    if (result != null) {
        return result;
    } else if (this.isObject() && that.isObject()) {
        // If both types are objects but no union was found, return NO_OBJECT_TYPE
        return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    } else {
        // In all other cases, return NO_TYPE
        return getNativeType(JSTypeNative.NO_TYPE);
    }
}