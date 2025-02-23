case Token.NEW:
  // Unless proven otherwise (such as immutable constructor or internally local properties), 
  // we should assume that `new` can create non-local values.
  return false;
