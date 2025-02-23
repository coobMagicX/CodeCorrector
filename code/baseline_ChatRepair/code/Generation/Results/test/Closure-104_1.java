JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  boolean subtypeFound = false; 

  
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
      subtypeFound = true;  
    }
  }

  if (that instanceof UnionType) {
    
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
        subtypeFound = true; 
      }
    }
  } else if (that.isSubtype(this)) {
    builder.addAlternate(that);
    subtypeFound = true; 
  }
  
  
  J