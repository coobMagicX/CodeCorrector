public TypePair getTypesUnderInequality(JSType that) {
    // unions types
    if (that instanceof UnionType) {
        TypePair p = that.getTypesUnderInequality(this);
        return new TypePair(p.typeB, p.typeA);
    }

    // Handling VOID_TYPE cases explicitly
    if (this.isVoidType() && that.isVoidType()) {
        return new TypePair(JSType.NO_TYPE, JSType.NO_TYPE);
    }

    // other types
    switch (this.testForEquality(that)) {
        case TRUE:
            return new TypePair(null, null);
        case FALSE:
        case UNKNOWN:
            return new TypePair(this, that);
    }

    // switch case is exhaustive
    throw new IllegalStateException();
}