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
      // This may be a key in an object literal declaration.
      if (parent != null && parent.isObjectLit()) {
        name = getNameForObjLitKey(n);
      }
      if (name == null) {
        return;
      }
      isSet = n.getType() == Token.SETTER_DEF;
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
      }
      break;
    case Token.NAME:
      // This may be a variable get or set.
      if (parent != null) {
        switch (parent.getType()) {
          case Token.VAR:
            isSet = true;
            Node rvalue = n.getFirstChild();
            type = (rvalue == null) ? Name.Type.OTHER : getValueType(rvalue);
            break;
          case Token.ASSIGN:
            isSet = parent.getFirstChild() == n;
            type = getValueType(n.getNext());
            break;
          case Token.GETPROP:
            if (NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n) {
              isSet = true;
              type = getValueType(n.getNext());
            } else {
              return;
            }
            break;
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
        }
      }
      name = n.getString();
      break;
    case Token.GETPROP:
      // This may be a namespaced name get or set.
      if (parent != null) {
        switch (parent.getType()) {
          case Token.ASSIGN:
            isSet = parent.getFirstChild() == n;
            type = getValueType(n.getNext());
            isPropAssign = true;
            break;
          case Token.INC:
          case Token.DEC:
            isSet = true;
            type = Name.Type.OTHER;
            break;
        }
      }
      name = n.getQualifiedName();
      if (name == null) {
        return;
      }
      break;
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