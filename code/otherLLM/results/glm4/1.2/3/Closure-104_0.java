JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  if (that instanceof UnionType) {
    UnionType unionThat = (UnionType) that;
    for (JSType otherAlternate : unionThat.alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (this.isSubtype(that)) {
    builder.addAlternate(that);
  }

  JSType result = builder.build();
  if (result != null) {
    return result;
  } else {
    // Check for the case where both types are objects and there is no common subtype
    if (this.isObject() && that.isObject()) {
      // If neither type has a common supertype, return NO_OBJECT_TYPE
      return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    }
    // Otherwise, return NO_TYPE
    return getNativeType(JSTypeNative.NO_TYPE);
  }
}