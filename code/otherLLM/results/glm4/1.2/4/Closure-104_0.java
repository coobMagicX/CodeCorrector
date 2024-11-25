JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  // Check if 'that' is a UnionType and add its subtypes to the result
  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else {
    // If 'that' is not a union type, check if it's a subtype of 'this'
    if (that.isSubtype(this)) {
      builder.addAlternate(that);
    }
  }

  JSType result = builder.build();
  
  // Check if the result is not a null type before returning
  if (result != null) {
    return result;
  } else {
    // If no common supertype exists and both types are objects, use NO_OBJECT_TYPE
    // Otherwise, use NO_TYPE to indicate there's no common supertype
    return this.isObject() && that.isObject() ? getNativeType(JSTypeNative.NO_OBJECT_TYPE) : getNativeType(JSTypeNative.NO_TYPE);
  }
}