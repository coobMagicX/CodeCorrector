JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);

  // Adding alternates from 'this' to 'builder'
  for (JSType alternate : this.alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  // Handling the case when 'that' is a UnionType
  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      // Adding alternates from 'that' to 'builder', ensuring there's no duplicate addition
      boolean isSubtype = false;
      for (JSType existingAlternate : builder.getAlternatives()) {
        if (existingAlternate.isSameTypeAs(otherAlternate)) {
          isSubtype = true;
          break;
        }
      }
      if (!isSubtype && otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (this.isSubtype(that)) {
    // Adding 'that' to 'builder' only if it's a subtype of 'this'
    builder.addAlternate(that);
  }

  JSType result = builder.build();

  // If no type is found, check for the special case when both types are objects
  if (result == null) {
    if (this.isObject() && that.isObject()) {
      return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    } else {
      return getNativeType(JSTypeNative.NO_TYPE);
    }
  }

  return result;
}