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
      case Token.CATCH:
        if (parent.getFirstChild() == n) {
          // Handle catch block variable, it is locally defined and set.
          isSet = true;
          type = Name.Type.OTHER;
          break;
        }
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
