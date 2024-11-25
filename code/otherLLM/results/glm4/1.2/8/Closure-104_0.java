JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  // Corrected the comparison to check if 'that' is a UnionType and then compare its elements
  if (this instanceof UnionType && that.isSubtype(this)) {
    for (JSType otherAlternate : ((UnionType) this).alternates) {
      if (otherAlternate.isSubtype(that)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (that instanceof UnionType && that.isSubtype(this)) {
    // Corrected the comparison to check if 'that' is a UnionType and then compare its elements
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (this.isSubtype(that)) {
    // Corrected this to handle the case where 'this' is a subtype of 'that'
    builder.addAlternate(that);
  }

  JSType result = builder.build();
  if (result != null) {
    return result;
  } else if (this.isObject() && that.isObject()) {
    return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
  } else {
    return getNativeType(JSTypeNative.NO_TYPE);
  }
}