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
        return true; // Return false for increment/decrement operators
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
      // Refine evaluation for primitive values: Check if the property is local
      if (value.getType() == Token.GETPROP && value.getLastChild().getType() == Token.NAME) {
        MatchNameNode match = new MatchNameNode((String) value.getLastChild());
        return locals.apply(match);
      }
      return false; // Return false for object properties
    case Token.CALL:
      return callHasLocalResult(value)
          || isToStringMethodCall(value)
          || locals.apply(value);
    case Token.NEW:
      // Improve handling of `new` operator: Check if the new expression is local
      Node constructor = value.getFirstChild();
      return constructor.getType() == Token.FUNCTION && locals.apply(constructor);
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