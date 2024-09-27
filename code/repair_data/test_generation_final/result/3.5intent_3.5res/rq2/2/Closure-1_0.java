private void removeUnreferencedFunctionArgs(Scope fnScope) {
  // Notice that removing unreferenced function args breaks
  // Function.prototype.length. In advanced mode, we don't really care
  // about this: we consider "length" the equivalent of reflecting on
  // the function's lexical source.
  //
  // Rather than create a new option for this, we assume that if the user
  // is removing globals, then it's OK to remove unused function args.
  //
  // See http://code.google.com/p/closure-compiler/issues/detail?id=253

  Node function = fnScope.getRootNode();

  Preconditions.checkState(function.isFunction());
  if (NodeUtil.isGetOrSetKey(function.getParent())) {
    // The parameters object literal setters can not be removed.
    return;
  }

  Node argList = getFunctionArgList(function);
  boolean modifyCallers = modifyCallSites
      && callSiteOptimizer.canModifyCallers(function);
  if (!modifyCallers) {
    // Strip unreferenced args off the end of the function declaration.
    Node lastArg;
    while ((lastArg = argList.getLastChild()) != null) {
      Var var = fnScope.getVar(lastArg.getString());
      if (!referenced.contains(var)) {
        argList.removeChild(lastArg);
        compiler.reportCodeChange();
      } else {
        break;
      }
    }
  } else {
    callSiteOptimizer.optimize(fnScope, referenced);
  }
}

void traverseFunction(Node functionNode, Scope scope) {
  Node argList = functionNode.getSecondChild();
  Node lastArg;
  while ((lastArg = argList.getLastChild()) != null) {
    Var var = scope.getVar(lastArg.getString());
    if (!referenced.contains(var)) {
      argList.removeChild(lastArg);
      compiler.reportCodeChange();
    } else {
      break;
    }
  }
}

void traverseNode(Node node, Node parent, Scope scope) {
  if (node.isFunction()) {
    traverseFunction(node, scope);
  } else {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      traverseNode(child, node, scope);
    }
  }
}

void apply() {
  if (NodeUtil.isFunctionDeclaration(node)) {
    traverseFunction(node, scope);
  } else {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      traverseNode(child, node, scope);
    }
  }
}