JSType meet(JSTYPE that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  // Check if the 'that' type is a union type
  if (that instanceof UnionType) {
    // Iterate over each subtype in the union type
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      // If this subtype is a subtype of 'this', add it to the builder
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (that.isSubtype(this)) {
    // If 'that' is a subtype of 'this', add it to the builder
    builder.addAlternate(that);
  }

  // Build the resulting union type
  JSType result = builder.build();
  
  // Check for object types and return NO_OBJECT_TYPE if both are objects
  if (result != null) {
    return result;
  } else if (this.isObject() && that.isObject()) {
    return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
  } else {
    return getNativeType(JSTypeNative.NO_TYPE);
  }
}