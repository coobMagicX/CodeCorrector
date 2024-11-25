public JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
    }
  }

  if (that instanceof UnionType) {
    UnionType unionThat = (UnionType) that;
    if (!unionThat.isSubtype(this)) {
      for (JSType otherAlternate : unionThat.alternates) {
        if (otherAlternate.isSubtype(this)) {
          builder.addAlternate(otherAlternate);
        }
      }
    }
  } else if (that.isSubtype(this)) {
    builder.addAlternate(that);
  }

  JSType result = builder.build();

  // Correcting the condition to check for greatest subtype and error types
  if (result != null) {
    return result;
  } else if (this.isObject() && that.isObject()) {
    // Check if any of the error types are part of the union
    boolean isEvalError = false;
    boolean isUriError = false;
    
    for (JSType alternate : alternates) {
      if (alternate == JSTypeNative.EVAL_ERROR_TYPE || alternate == JSTypeNative.URI_ERROR_TYPE) {
        isEvalError = true;
        isUriError = true;
        break; // If both types are present, no need to continue
      }
    }

    if (!isEvalError && !isUriError) {
      return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    } else {
      return getNativeType(JSTypeNative.NO_TYPE); // Return NO_TYPE if any error type is found
    }
  } else {
    return getNativeType(JSTypeNative.NO_TYPE);
  }
}