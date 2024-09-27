case Token.IN:
      Preconditions.checkState(childCount == 2);
      addExpr(first, NodeUtil.precedence(type), context);
      add("in");
      add(first.getNext());
      break;