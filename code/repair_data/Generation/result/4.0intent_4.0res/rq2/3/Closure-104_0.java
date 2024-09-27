JSType meet(JSType that) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    boolean hasCommonSubtype = false;

    // Check each alternate in this union type against the provided type
    for (JSType alternate : alternates) {
        if (alternate.isSubtype(that)) {
            builder.addAlternate(alternate);
            hasCommonSubtype = true;
        }
    }

    // If 'that' is a union type, check each of its alternates to see if it's a subtype of this union type
    if (that instanceof UnionType) {
        for (JSType otherAlternate : ((UnionType) that).alternates) {
            if (otherAlternate.isSubtype(this)) {
                builder.addAlternate(otherAlternate);
                hasCommonSubtype = true;
            }
        }
    } else if (that.isSubtype(this)) {
        builder.addAlternate(that);
        hasCommonSubtype = true;
    }

    // Build the union of all common subtypes found, or return NO_OBJECT_TYPE if none found
    JSType result = builder.build();
    if (hasCommonSubtype) {
        return result;
    } else {
        return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    }
}