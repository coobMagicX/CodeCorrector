public void visit(NodeTraversal t, Node n, Node parent) {

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
      Node nameNode = n.getFirstChild();
      if (nameNode != null && nameNode.isName()) {
        NameInformation ns = createNameInformation(t, nameNode, n);
        if (ns != null && ns.name != null) {
          recordSet(ns.name, nameNode);
        } else {
          throw new IllegalStateException("Failed to retrieve NameInformation or name is null for function declaration.");
        }
      } else {
        throw new IllegalStateException("Function declaration node's first child is not a name.");
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
  } else if (n.isCall()) {
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
      } else {
        throw new IllegalStateException("NameInformation is null or does not affect class definition in function call.");
      }
    } else {
      throw new IllegalStateException("Function call node's first child is not a name type.");
    }
  }
}
