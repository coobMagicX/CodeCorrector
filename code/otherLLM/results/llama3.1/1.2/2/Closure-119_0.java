public void collect(JSModule module, Scope scope, Node n) {
  Node parent = n.getParent();

  String name;
  boolean isSet = false;
  Name.Type type = Name.Type.OTHER;
  boolean isPropAssign = false;

  switch (n.getType()) {
    case Token.GETTER_DEF:
    case Token.SETTER_DEF:
    case Token.STRING_KEY:
      // This may be a key in an object literal declaration.
      name = null;
      if (parent != null && parent.isObjectLit()) {
        name = getNameForObjLitKey(n);
      }
      if (name == null) {
        return;
      }
      isSet = true;
      switch (n.getType()) {
        case Token.STRING_KEY:
          type = getValueType(n.getFirstChild());
          break;
        case Token.GETTER_DEF:
          type = Name.Type.GET;
          break;
        case Token.SETTER_DEF:
          type = Name.Type.SET;
          break;
        default:
          throw new IllegalStateException("unexpected:" + n);
      }
      break;
    case Token.NAME:
      // This may be a variable get or set.
      if (parent != null) {
        switch (parent.getType()) {
          case Token.VAR:
            isSet = true;
            Node rvalue = n.getFirstChild();
            type = rvalue == null ? Name.Type.OTHER : getValueType(rvalue);
            break;
          case Token.ASSIGN:
            if (parent.getFirstChild() == n) {
              isSet = true;
              type = getValueType(n.getNext());
            }
            break;
          case Token.GETPROP:
            return;
          case Token.FUNCTION:
            Node gramps = parent.getParent();
            if (gramps == null || NodeUtil.isFunctionExpression(parent)) {
              return;
            }
            isSet = true;
            type = Name.Type.FUNCTION;
            break;
          case Token.INC:
          case Token.DEC:
            isSet = true;
            type = Name.Type.OTHER;
            break;
          default:
            if (NodeUtil.isAssignmentOp(parent) &&
                parent.getFirstChild() == n) {
              isSet = true;
              type = Name.Type.OTHER;
            }
        }
      }
      name = n.getString();
      break;
    case Token.GETPROP:
      // This may be a namespaced name get or set.
      if (parent != null) {
        switch (parent.getType()) {
          case Token.ASSIGN:
            if (parent.getFirstChild() == n) {
              isSet = true;
              type = getValueType(n.getNext());
              isPropAssign = true;
            }
            break;
          case Token.INC:
          case Token.DEC:
            isSet = true;
            type = Name.Type.OTHER;
            break;
          case Token.GETPROP:
            return;
          default:
            if (NodeUtil.isAssignmentOp(parent) &&
                parent.getFirstChild() == n) {
              isSet = true;
              type = Name.Type.OTHER;
            }
        }
      }
      name = n.getQualifiedName();
      break;
    case Token.THROW:
    case Token.YIELD:
      return; // skip throw and yield statements
    case Token.EXPR:
      if (parent.getType() == Token.FOR || parent.getType() == Token.WHILE) {
        // handle for/while loop expressions
        Node expr = n.getFirstChild();
        isSet = true;
        type = getValueType(expr);
        name = expr.getString();
      } else if (parent.getType() == Token.SWITCH) {
        // handle switch statement expressions
        Node expr = n.getFirstChild();
        isSet = true;
        type = getValueType(expr);
        name = expr.getString();
      }
      break;
    default:
      throw new IllegalStateException("unexpected:" + n);
  }

  if (isSet && parent.getType() == Token.CATCH) {
    // handle catch block expressions
    Node expr = n.getFirstChild();
    isSet = true;
    type = getValueType(expr);
    name = expr.getString();
  }

  if (isSet && type == Name.Type.THROW) {
    // handle throw statements
    return;
  }

  if (name != null) {
    Ref ref = new Ref(type, n.getPreOrderIndex());
    // add the reference to the scope or namespace
    // ...
  }
}