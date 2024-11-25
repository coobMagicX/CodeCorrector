JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  
  // Handle UnionType meets UnionType
  if (that instanceof UnionType) {
    for (JSType alternate : alternates) {
      if (((UnionType) that).alternates.contains(alternate)) {
        builder.addAlternate(alternate);
      }
    }
    
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
      }
    }
  } else if (that instanceof UnionType && this instanceof UnionType) {
    // Prevent adding subtypes as alternates
    Set<JSType> subtypeAlternates = new HashSet<>();
    for (JSType alternate : ((UnionType) that).alternates) {
      if (((UnionType) that).alternates.contains(alternate)) {
        subtypeAlternates.add(alternate);
      }
    }
    
    for (JSType otherAlternate : ((UnionType) this).alternates) {
      if (!((UnionType) that).alternates.contains(otherAlternate)) {
        builder.addAlternate(otherAlternate);
      } else if (!subtypeAlternates.contains(otherAlternate)) {
        builder.addAlternate(otherAlternate);
      }
    }
    
    for (JSType alternate : alternates) {
      if (((UnionType) this).alternates.contains(alternate)) {
        builder.addAlternate(alternate);
      } else if (!((UnionType) that).alternates.contains(alternate)) {
        builder.addAlternate(alternate);
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