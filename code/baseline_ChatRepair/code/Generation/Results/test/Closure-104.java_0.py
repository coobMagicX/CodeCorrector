JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  boolean foundSubtype = false;

  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
      foundSubtype = true;
    }
  }

  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
        foundSubtype = true;
      }
    }
  } else if (that.isSubtype(this)) {
    builder.addAlternate(that);
    foundSubtype = true;
  }

  if (!foundSubtype && this.isObject() && that.isObject()) {
    return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
  }

  JSType result = builder.build();
  return result != null ? result : getNativeType(JSTypeNative.NO_TYPE);
}
