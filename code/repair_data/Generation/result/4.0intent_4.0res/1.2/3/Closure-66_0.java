public void visit(NodeTraversal t, Node n, Node parent) {
  JSType childType;
  JSType leftType, rightType;
  Node left, right;
  // To be explicitly set to false if the node is not typeable.
  boolean typeable = true;

  switch (n.getType()) {
    case Token.NAME:
      typeable = visitName(t, n, parent);
      break;

    case Token.LP:
      if (parent.getType() != Token.FUNCTION) {
        ensureTyped(t, n, getJSType(n.getFirstChild()));
      } else {
        typeable = false;
      }
      break;

    case Token.COMMA:
      ensureTyped(t, n, getJSType(n.getLastChild()));
      break;

    case Token.TRUE:
    case Token.FALSE:
      ensureTyped(t, n, BOOLEAN_TYPE);
      break;

    case Token.THIS:
      ensureTyped(t, n, t.getScope().getTypeOfThis());
      break;

    case Token.REF_SPECIAL:
      ensureTyped(t, n);
      break;

    case Token.GET_REF:
      ensureTyped(t, n, getJSType(n.getFirstChild()));
      break;

    case Token.NULL:
      ensureTyped(t, n, NULL_TYPE);
      break;

    case Token.NUMBER:
      ensureTyped(t, n, NUMBER_TYPE);
      break;

    case Token.STRING:
      if (!NodeUtil.isObjectLitKey(n, n.getParent())) {
        ensureTyped(t, n, STRING_TYPE);
      }
      break;

    case Token.GET:
    case Token.SET:
      break;

    case Token.ARRAYLIT:
      ensureTyped(t, n, ARRAY_TYPE);
      break;

    case Token.REGEXP:
      ensureTyped(t, n, REGEXP_TYPE);
      break;

    case Token.GETPROP:
      visitGetProp(t, n, parent);
      typeable = !(parent.getType() == Token.ASSIGN && parent.getFirstChild() == n);
      break;

    case Token.GETELEM:
      visitGetElem(t, n);
      typeable = false;
      break;

    case Token.VAR:
      visitVar(t, n);
      typeable = false;
      break;

    case Token.NEW:
      visitNew(t, n);
      typeable = true;
      break;

    case Token.CALL:
      visitCall(t, n);
      typeable = !NodeUtil.isExpressionNode(parent);
      break;

    case Token.RETURN:
      visitReturn(t, n);
      typeable = false;
      break;

    case Token.DEC:
    case Token.INC:
      left = n.getFirstChild();
      validator.expectNumber(t, left, getJSType(left), "increment/decrement");
      ensureTyped(t, n, NUMBER_TYPE);
      break;

    case Token.NOT:
      ensureTyped(t, n, BOOLEAN_TYPE);
      break;

    case Token.VOID:
      ensureTyped(t, n, VOID_TYPE);
      break;

    case Token.TYPEOF:
      ensureTyped(t, n, STRING_TYPE);
      break;

    case Token.BITNOT:
      childType = getJSType(n.getFirstChild());
      if (!childType.matchesInt32Context()) {
        report(t, n, BIT_OPERATION, NodeUtil.opToStr(n.getType()), childType.toString());
      }
      ensureTyped(t, n, NUMBER_TYPE);
      break;

    case Token.POS:
    case Token.NEG:
      left = n.getFirstChild();
      validator.expectNumber(t, left, getJSType(left), "sign operator");
      ensureTyped(t, n, NUMBER_TYPE);
      break;

    case Token.OBJECTLIT:
      ensureTyped(t, n);
      for (Node key : n.children()) {
        if (key.getType() == Token.STRING_KEY) {
          ensureTyped(t, key, STRING_TYPE);
        } else if (key.getType() == Token.NUMBER) {
          ensureTyped(t, key, NUMBER_TYPE);
        } else {
          visit(t, key, n);
        }
      }
      break;

    default:
      report(t, n, UNEXPECTED_TOKEN, Token.name(n.getType()));
      ensureTyped(t, n);
      break;
  }

  typeable = typeable && !inExterns;

  if (typeable) {
    doPercentTypedAccounting(t, n);
  }

  checkNoTypeCheckSection(n, false);
}