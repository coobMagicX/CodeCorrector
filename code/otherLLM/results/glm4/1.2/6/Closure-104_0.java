public JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  
  // Adding subtypes from the 'that' union type to the builder
  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).getAlternates()) {
      if (this.isSubtype(otherAlternate)) { // Should compare with each alternate of 'that'
        builder.addAlternate(otherAlternate);
      }
    }
  }

  // Adding subtypes from the current union type to the builder
  for (JSType alternate : this.getAlternates()) {
    if (alternate.isSubtype(this)) { // Should check if it's a subtype of the whole 'this'
      builder.addAlternate(alternate);
    } else if (that instanceof UnionType && alternate.isSubtype(((UnionType) that))) {
      builder.addAlternate(alternate); // Check against each alternate of 'that' too
    }
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