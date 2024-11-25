private void removeUnreferencedFunctionArgs(Scope fnScope) {
  Node function = fnScope.getRootNode().getFirstChild();
  
  if (function != null && function.isParamList()) {
    // If the param list has children, we need to iterate over them and mark
    // each as referenced.
    for (Node a = function.getFirstChild(); a != null; a = a.getNext()) {
      markReferencedVar(fnScope.getVar(a.getString()));
    }
  }

  Node paramList = function.getParent().getNext();
  
  if (paramList != null && paramList.isParamList()) {
    // If the param list has children, we need to iterate over them and
    // remove any that are unreferenced.
    for (Iterator<Var> it = maybeUnreferenced.iterator(); it.hasNext(); ) {
      Var var = it.next();

      if (paramList.getChildCount() > 0) {
        Node paramNode = null;
        for (Node p = paramList.getFirstChild(); p != null; p = p.getNext()) {
          if (p.getString().equals(var.name)) {
            paramNode = p;
            break;
          }
        }

        if (paramNode != null) {
          // Remove the unreferenced parameter
          NodeUtil.removeChild(paramList, paramNode);
          compiler.reportCodeChange();
        }
      }
    }
  }
}