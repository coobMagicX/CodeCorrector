public void collect(JSModule module, Scope scope, Node n) {
  Node parent = n.getParent();

  String name = null; // Initialize name to null initially
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
          name = n.getString(); // Set name for STRING_KEY
          break;
        case Token.GETTER_DEF:
          type = Name.Type.GET;
          break;
        case Token.SETTER_DEF:
          type = Name.Type.SET;
          break;
      }
      break;
    case Token.NAME:
      name = n.getString();
      if (parent != null) {
        switch (parent.getType()) {
          case Token.VAR:
          case Token.LET:
          case Token.CONST: {
            isSet = true;
            Node rvalue = n.getFirstChild();
            type = rvalue == null ? Name.Type.OTHER : getValueType(rvalue);
          }
            break;
          case Token.ASSIGN:
            if (parent.getFirstChild() == n) {
              isSet = true;
              type = getValueType(n.getNext());
            }
            break;
          case Token.GETPROP:
          case Token.FUNCTION:
            Node gramps = parent.getParent();
            if (gramps == null || NodeUtil.isFunctionExpression(parent)) {
              return;
            }
            name = n.getQualifiedName();
            isSet = true;
            type = Name.Type.FUNCTION;
            break;
          case Token.INC:
          case Token.DEC:
            isSet = true;
            type = Name.Type.OTHER;
            name = n.getQualifiedName();
            break;
          case Token.CATCH:
            isSet = true;
            type = Name.Type.OTHER;
            // Ensure name is set for catch variables
            name = n.getString();
            break;
        }
      }
      break;
    case Token.GETPROP:
      name = n.getQualifiedName();
      if (name == null) {
        return;
      }
      if (parent != null) {
        if (NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n) {
          isSet = true;
          type = getValueType(n.getNext());
          isPropAssign = true;
        }
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
