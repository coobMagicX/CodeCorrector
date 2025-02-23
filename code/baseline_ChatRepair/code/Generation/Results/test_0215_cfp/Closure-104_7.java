JSType meet(JSType that) {
  UnionTypeBuilder builder = new UnionTypeBuilder(registry);
  boolean addedSubtypeFromThis = false;
  boolean addedSubtypeFromThat = false;

  
  for (JSType alternate : alternates) {
    if (alternate.isSubtype(that)) {
      builder.addAlternate(alternate);
      addedSubtypeFromThis = true;
    }
  }

  
  if (that instanceof UnionType) {
    for (JSType otherAlternate : ((UnionType) that).alternates) {
      if (otherAlternate.isSubtype(this)) {
        builder.addAlternate(otherAlternate);
        addedSubtypeFromThat = true;
      }
    }
  } 

  
  if (!(that instanceof UnionType) && that.isSubtype(this)) {
    builder.addAlternate(that);
    addedSubtypeFromThat = true;
  }

  
  if (!addedSubtypeFromThis && !addedSubtypeFromThat && this.isObject() && that.isObject())