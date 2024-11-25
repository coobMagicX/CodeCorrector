case Token.STRING_KEY:
  Preconditions.checkState(
      childCount == 1, "Object lit key must have 1 child");
  addJsString(n);
  break;