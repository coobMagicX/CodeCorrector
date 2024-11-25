JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  
  // Check for subtype relationship in both directions
  boolean isSubtypeThisToThat = this.isSubtype(that);
  boolean isSubtypeThatToThis = that.isSubtype(this);

  if (isSubtypeThisToThat) {
    // If 'this' is a subtype of 'that', add 'this' to the builder
    builder.addAlternate(this);
    
    // Also add subtypes of 'that' that are also subtypes of 'this'
    if (that instanceof UnionType) {
      for (JSType otherAlternate : ((UnionType) that).alternates) {
        if (otherAlternate.isSubtype(this)) {
          builder.addAlternate(otherAlternate);
        }
      }
    }
  }

  if (isSubtypeThatToThis) {
    // If 'that' is a subtype of 'this', add 'that' to the builder
    if (that instanceof UnionType) {
      for (JSType otherAlternate : ((UnionType) that).alternates) {
        if (otherAlternate.isSubtype(this)) {
          builder.addAlternate(otherAlternate);
        }
      }
    } else {
      // If 'that' is not a union type, it must be an object or the same type as 'this'
      if (isSubtypeThisToThat || this.equals(that)) {
        builder.addAlternate(that);
      }
    }
  }

  JSType result = builder.build();
  
  // Return NO_OBJECT_TYPE when comparing a union type with a STRING_OBJECT_TYPE
  if ((this instanceof UnionType && that.isString() && !((UnionType) this).isString())
      || (that instanceof UnionType && this.isString() && !((UnionType) that).isString())) {
    return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
  }

  // Fallback logic for objects when no common subtype is found
  if (this.isObject() && that.isObject()) {
    return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
  }
  
  // Default case
  return result != null ? result : getNativeType(JSTypeNative.NO_TYPE);
}