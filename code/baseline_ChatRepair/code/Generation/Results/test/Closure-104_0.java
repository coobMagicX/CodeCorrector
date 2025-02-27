JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  boolean thisIsSubtypeOfThat = this.isSubtype(that);
  boolean thatIsSubtypeOfThis = that.isSubtype(this);

  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
      thisIsSubtypeOfThat = true;
    }
  }

  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
        thatIsSubtypeOfThis = true;
      }
    }
  }

  
  if (thisIsSubtypeOfThat && thatIsSubtypeOfThis) {
    builder.addAlternate(that);
  }

  
  JSType result = builder.build();
  if (result != null) {
    return result;
  } else if (this.isObject() && that.isObject()) {
    
    return getNativeType(JSTypeNative.NO_OBJECT_TYPE