JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  boolean addedAlternate = false;  

  
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
      addedAlternate = true;  
    }
  }

  if (that instanceof UnionType) {
    
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
        addedAlternate = true;  
      }
    }
  } else if (that.isSubtype(this)) {
    builder.addAlternate(that);
    addedAlternate = true;  
  }
  
  