JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (!alternate.isSubtype(that)) { // Add a check to ensure that 'this' is not a subtype of 'that'
      continue;
    }
    if (!that.isSubtype(alternate)) { // And also that 'that' is not a subtype of 'this'
      builder.addAlternate(alternate);
    }
  }

  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (!otherAlternate.isSubtype(this)) { // Add the same check as above
        continue;
      }
      if (!this.isSubtype(otherAlternate)) { // And also that 'this' is not a subtype of 'that'
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (that.isSubtype(this) && !this.isSubtype(that)) {
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