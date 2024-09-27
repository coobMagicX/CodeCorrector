static boolean evaluatesToLocalValue(Node value, Predicate<Node> locals) {
  switch (value.getType()) {
    case Token.ASSIGN:
      // A result that is aliased by a non-local name, is the effectively the
      // same as returning a non-local name, but this doesn't matter if the
      // value is immutable.
      return NodeUtil.isImmutableValue(value.getLastChild())
          || (locals.apply(value)
              && evaluatesToLocalValue(value.getLastChild(), locals));
    case Token.COMMA:
      return evaluatesToLocalValue(value.getLastChild(), locals);
    case Token.AND:
    case Token.OR:
      return evaluatesToLocalValue(value.getFirstChild(), locals)
         && evaluatesToLocalValue(value.getLastChild(), locals);
    case Token.HOOK:
      return evaluatesToLocalValue(value.getFirstChild().getNext(), locals)
         && evaluatesToLocalValue(value.getLastChild(), locals);
    case Token.INC:
    case Token.DEC:
      if (value.getBooleanProp(Node.INCRDECR_PROP)) {
        return evaluatesToLocalValue(value.getFirstChild(), locals);
      } else {
        return true;
      }
    case Token.THIS:
      return locals.apply(value);
    case Token.NAME:
      return isImmutableValue(value) || locals.apply(value);
    case Token.GETELEM:
    case Token.GETPROP:
      // There is no information about the locality of object properties.
      return locals.apply(value);
    case Token.CALL:
      return callHasLocalResult(value)
          || isToStringMethodCall(value)
          || locals.apply(value);
    case Token.NEW:
      // The value is local if it does not alias itself from inside the constructor.
      return !aliasesItselfFromConstructor(value) && locals.apply(value);
    case Token.FUNCTION:
    case Token.REGEXP:
    case Token.ARRAYLIT:
    case Token.OBJECTLIT:
      // Literals objects with non-literal children are allowed.
      return true;
    case Token.IN:
      // IN operator check is simplified as local if the value is immutable.
      return isImmutableValue(value);
    default:
      // Other operations force a local value:
      //  x = '' + g (x is now a local string)
      //  x -= g (x is now a local number)
      if (isAssignmentOp(value)
          || isSimpleOperator(value)
          || isImmutableValue(value)) {
        return true;
      }

      throw new IllegalStateException(
          "Unexpected expression node" + value +
          "\n parent:" + value.getParent());
  }
}

// Additional assumed methods within the context
boolean aliasesItselfFromConstructor(Node value) {
  // Implementation or assumption that checks if the 'new' object is aliased in constructor.
  // This is a placeholder for the actual logic which should be determined based on application specifics.
  return false; // Simplified assumption; actual implementation may vary.
}

boolean isImmutableValue(Node value) {
  // Checks if the node represents an immutable value.
  // This is a placeholder for actual method logic.
  return false; // Simplified assumption; actual implementation may vary.
}

boolean callHasLocalResult(Node value) {
  // Determines if a function call results in a local value.
  // Placeholder for actual method logic.
  return false; // Simplified assumption; actual implementation may vary.
}

boolean isToStringMethodCall(Node value) {
  // Checks if the call is to a toString method which would likely return a new string (local).
  // Placeholder for actual method logic.
  return false; // Simplified assumption; actual implementation may vary.
}

boolean isAssignmentOp(Node value) {
  // Determines if a node represents an assignment operator.
  // Placeholder for actual method logic.
  return false; // Simplified assumption; actual implementation may vary.
}

boolean isSimpleOperator(Node value) {
  // Determines if a node represents a simple operator.
  // Placeholder for actual method logic.
  return false; // Simplified assumption; actual implementation may vary.
}