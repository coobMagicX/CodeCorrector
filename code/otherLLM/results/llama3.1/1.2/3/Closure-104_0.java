JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  // Check if 'that' is a union type and contains this type as an alternate
  if (that instanceof UnionType && ((UnionType) that).getAlternates().contains(this)) {
    return getNativeType(JSTypeNative.NO_OBJECT_TYPE); // Return NO_OBJECT_TYPE if 'this' is in 'that'
  }

  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (that.isSubtype(this)) {
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