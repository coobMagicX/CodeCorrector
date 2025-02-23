JSType meet(JSType that) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    boolean foundCompatibleType = false;

    
    for (JSType alternate : alternates) {
        if (alternate.isSubtype(that)) {
            builder.addAlternate(alternate);
            foundCompatibleType = true;
        }
    }

    
    if (that instanceof UnionType) {
        for (JSType otherAlternate : ((UnionType) that).alternates) {
            if (otherAlternate.isSubtype(this)) {
                builder.addAlternate(otherAlternate);
                foundCompatibleType = true;
            }
        }
    } else {
        
        if (that.isSubtype(this)) {
            builder.addAlternate(that);
            foundCompatibleType = true;
        }
    }
    
    
    if (foundCompatibleType) {
        JSType result = builder.build();
        return result != null ? result : getNativeType(JSTypeNative.NO_TYPE);
    } else if (isObject() && that.isObject()) {
        