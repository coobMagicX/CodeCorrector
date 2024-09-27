JSType meet(JSType that) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    boolean hasCommonSubtype = false;

    for (JSType alternate : alternates) {
        if (alternate.isSubtype(that)) {
            builder.addAlternate(alternate);
            hasCommonSubtype = true;
        }
    }

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

    if (hasCommonSubtype) {
        return builder.build();
    } else if (this.isObject() && that.isObject()) {
        return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    } else {
        return getNativeType(JSTypeNative.NO_TYPE);
    }
}