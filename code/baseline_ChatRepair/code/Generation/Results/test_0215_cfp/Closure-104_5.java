JSType meet(JSType that) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    boolean anyValidSubtype = false;  

    
    for (JSType alternate : alternates) {
        if (alternate.isSubtype(that)) {
            builder.addAlternate(alternate);
            anyValidSubtype = true;
        }
    }

    
    if (that instanceof UnionType) {
        for (JSType otherAlternate : ((UnionType) that).alternates) {
            if (otherAlternate.isSubtype(this)) {
                builder.addAlternate(otherAlternate);
                anyValidSubtype = true;
            }
        }
    } else {
        
        if (that.isSubtype(this)) {
            builder.addAlternate(that);
            anyValidSubtype = true;
        }
    }

    
    JST