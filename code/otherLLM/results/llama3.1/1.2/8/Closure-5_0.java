private boolean isVarOrAssignExprLhs(Node name) {
  Node parent = name.getParent();
  if (parent.isVar() || parent.isAssign()) {
    // Check that there are no more ancestors of the Var/Assign node.
    while (true) {
      parent = parent.getParent();
      if (!NodeUtil.isStatementBlock(parent)) break;
    }
    return true;
  }
  return false;
}

private boolean isGetterDef(Node child) {
  return child.isCall() && NodeUtil.isGetter(child);
}

private boolean isSetterDef(Node child) {
  return child.isCall() && NodeUtil.isSetter(child);
}