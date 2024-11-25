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
        // Check the right-hand side for locality
        Node rhs = value.getLastChild();
        return locals.apply(rhs) || isImmutableValue(rhs);
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
      Node function = value.getFirstChild();
      if (callHasLocalResult(value)) {
        return true;
      }
      if (isToStringMethodCall(value) || locals.apply(function)) {
        return true;
      }
      return evaluatesToLocalValue(function, locals);
    case Token.NEW:
      // Check for locality of the new object
      Node constructor = value.getLastChild();
      if (constructor.getType() == Token.FUNCTION) {
        // If it's a constructor call, then check its children for locality
        Node functionBody = constructor.getFirstChild().getNext();
        for (Node child : NodeUtil.getDescendants(functionBody)) {
          if (!evaluatesToLocalValue(child, locals)) {
            return false;
          }
        }
      }
      return true; // A new object is non-local unless proven otherwise
    case Token.FUNCTION:
    case Token.REGEXP:
    case Token.ARRAYLIT:
    case Token.OBJECTLIT:
      // Literals objects with non-literal children are allowed.
      return true;
    case Token.IN:
      // TODO(johnlenz): should IN operator be included in #isSimpleOperator?
      return true;
    default:
      // Other op force a local value:
      //  x = '' + g (x is now an local string)
      //  x -= g (x is now an local number)
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