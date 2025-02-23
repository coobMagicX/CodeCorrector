JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  boolean hasAddedSubtype = false; 

  
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
      hasAddedSubtype = true;
    }
  }

  
  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
        hasAddedSubtype = true;
      }
    }
  } else {
    
    if (that.isSubtype(this)) {
      builder.addAlternate(that);
      hasAddedSubtype = true;
    }
  }

  