JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  if (that instanceof UnionType) {
    boolean foundMatching = false;
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
        foundMatching = true;
      }
    }
    // Add this check to prevent infinite recursion when dealing with nested union types
    if (!foundMatching && !this.isSubtype(that)) {
      return getNativeType(JSTypeNative.NO_TYPE);
    }
  } else if (that.isSubtype(this)) {
    builder.addAlternate(that);
  }

  JSType result = builder.build();
  // Correctly handle null results
  if (result == null) {
    if (this.isObject() && that.isObject()) {
      return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    } else {
      return getNativeType(JSTypeNative.NO_TYPE);
    }
  }
  return result;
}