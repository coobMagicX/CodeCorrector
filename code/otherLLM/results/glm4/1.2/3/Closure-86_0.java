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
      Node constructor = value.getLastChild();
      if (constructor != null && constructor.getType() == Token.FUNCTION) {
        // If the new expression has a function as its last child, it's likely
        // an object literal or a function invocation. We need to check the
        // locality of the object properties inside this function.
        Node obj = constructor.getLastChild();
        if (obj != null && obj.getType() == Token.OBJECTLIT) {
          for (Node property : NodeUtil.getPropertyNodes(obj)) {
            return evaluatesToLocalValue(property, locals);
          }
        } else {
          // For now, we assume that non-literal objects are not local.
          return false;
        }
      } else {
        // We treat object creation expressions as potentially non-local unless
        // proven otherwise by the constructor logic. This is a simplification
        // and may need to be refined for more complex cases.
        return locals.apply(value);
      }
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