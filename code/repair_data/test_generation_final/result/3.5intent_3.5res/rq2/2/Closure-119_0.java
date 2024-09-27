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
      if (name == null) {
        return;
      }
      break;
    default:
      return;
  }

  // We are only interested in global names.
  if (!isGlobalNameReference(name, scope)) {
    return;
  }

  if (isSet) {
    if (isGlobalScope(scope)) {
      handleSetFromGlobal(module, scope, n, parent, name, isPropAssign, type);
    } else {
      handleSetFromLocal(module, scope, n, parent, name);
    }
  } else {
    handleGet(module, scope, n, parent, name);
  }
}

Name.Type getValueType(Node n) {
  switch (n.getType()) {
    case Token.OBJECTLIT:
      return Name.Type.OBJECTLIT;
    case Token.FUNCTION:
      return Name.Type.FUNCTION;
    case Token.OR:
      // Recurse on the second value. If the first value were an object
      // literal or function, then the OR would be meaningless and the
      // second value would be dead code. Assume that if the second value
      // is an object literal or function, then the first value will also
      // evaluate to one when it doesn't evaluate to false.
      return getValueType(n.getLastChild());
    case Token.HOOK:
      // The same line of reasoning used for the OR case applies here.
      Node second = n.getFirstChild().getNext();
      Name.Type t = getValueType(second);
      if (t != Name.Type.OTHER) {
        return t;
      }
      Node third = second.getNext();
      return getValueType(third);
  }
  return Name.Type.OTHER;
}