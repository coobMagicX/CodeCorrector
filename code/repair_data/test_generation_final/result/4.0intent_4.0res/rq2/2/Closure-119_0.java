public void collect(JSModule module, Scope scope, Node n) {
  Node parent = n.getParent();

  String name = null;
  boolean isSet = false;
  Name.Type type = Name.Type.OTHER;
  boolean isPropAssign = false;

  switch (n.getType()) {
    case Token.GETTER_DEF:
    case Token.SETTER_DEF:
    case Token.STRING_KEY:
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
          case Token.CATCH:
            if (!NodeUtil.isCatchBlock(parent)) {
              return; // Ignore variables declared in catch blocks
            }
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