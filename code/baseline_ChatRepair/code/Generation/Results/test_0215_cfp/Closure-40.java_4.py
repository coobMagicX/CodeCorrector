public void visit(NodeTraversal t, Node n, Node parent) {

  // Ensure the traversal and nodes are not null
  if (t == null || n == null) {
    throw new IllegalStateException("NodeTraversal or Node must not be null");
  }

  // Record global variable and function declarations
  if (t.inGlobalScope()) {
    if (NodeUtil.isVarDeclaration(n)) {
      NameInformation ns = createNameInformation(t, n, parent);
      if (ns != null && ns.name != null) {
        recordSet(ns.name, n);
      } else {
        throw new IllegalStateException("Failed to retrieve NameInformation or name is null for var declaration.");
      }
    } else if (NodeUtil.isFunctionDeclaration(n)) {
      NameInformation ns = checkAndCreateNameInfoForFunction(t, n);
      if (ns != null && ns.name != null) {
        recordSet(ns.name, ns.nameNode);
      } else {
        throw new IllegalStateException("Failed to retrieve NameInformation or name is null for function declaration.");
      }
    } else if (NodeUtil.isObjectLitKey(n, parent)) {
      NameInformation ns = createNameInformation(t, n, parent);
      if (ns != null && ns.name != null) {
        recordSet(ns.name, n);
      } else {
        throw new IllegalStateException("Failed to retrieve NameInformation or name is null for object literal.");
      }
    }
  }

  // Record assignments and call sites
  if (n.isAssign()) {
    processAssignment(t, n);
  } else if (n.isCall()) {
    processFunctionCall(t, n, parent);
  }
}

private NameInformation checkAndCreateNameInfoForFunction(NodeTraversal t, Node functionNode) {
  Node nameNode = functionNode.getFirstChild();
  if (nameNode != null && nameNode.isName()) {
    return createNameInformation(t, nameNode, functionNode);
  }
  return null;
}

private void processAssignment(NodeTraversal t, Node n) {
  Node nameNode = n.getFirstChild();
  if (nameNode != null) {
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null && ns.name != null) {
      if (ns.isPrototype) {
        recordPrototypeSet(ns.prototypeClass, ns.prototypeProperty, n);
      } else {
        recordSet(ns.name, nameNode);
      }
    } else {
      throw new IllegalStateException("Failed to retrieve NameInformation or name is null in assignment.");
    }
  } else {
    throw new IllegalStateException("Assignment node has no children.");
  }
}

private void processFunctionCall(NodeTraversal t, Node n, Node parent) {
  Node nameNode = n.getFirstChild();
  if (nameNode != null && nameNode.isName()) {
    NameInformation ns = createNameInformation(t, nameNode, n);
    if (ns != null && ns.onlyAffectsClassDef && ns.name != null) {
      JsName name = getName(ns.name, false);
      if (name != null) {
        refNodes.add(new ClassDefiningFunctionNode(name, n, parent, parent.getParent()));
      } else {
        throw new IllegalStateException("JsName is null for defining class function node.");
      }
    }
  } else {
    throw new IllegalStateException("Function call node's first child is not a name type.");
  }
}
