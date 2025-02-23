private boolean isInlinableObject(List<Reference> refs) {
  Set<String> validProperties = new HashSet<>();
  boolean isCandidateForInlining = true;

  for (Reference ref : refs) {
    Node nameNode = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Handle property accesses
    if (parent.isGetProp()) {
      String propName = parent.getLastChild().getString();
      Preconditions.checkState(parent.getFirstChild() == nameNode);

      // Direct use as a function call (e.g. goog.isFunction(obj.prop))
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        isCandidateForInlining = false;
        break;
      }

      // If the property is deleted anywhere, it should not be inlined
      if (gramps.isDelProp() && gramps.getFirstChild() == parent) {
        isCandidateForInlining = false;
        break;
      }

      // Valid property update scenario
      if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
        validProperties.add(propName);
      }
    }

    // Handle variable declarations and assignments
    if (isVarOrAssignExprLhs(nameNode)) {
      Node value = ref.getAssignedValue();
      if (value != null && value.isObjectLit()) {
        for (Node child = value.getFirstChild(); child != null; child = child.getNext()) {
          if (!child.isGetterDef() && !child.isSetterDef()) {
            validProperties.add(child.getString());
          } else {
            // ES5 getters/setters are not supported
            isCandidateForInlining = false;
            break;
          }
          
          // Check for self-references within the same object literal
          Node childValue = child.getFirstChild();
          for (Reference innerRef : refs) {
            Node innerParent = innerRef.getParent();
            while (!NodeUtil.isStatementBlock(innerParent)) {
              if (innerParent == childValue) {
                isCandidateForInlining = false;
                break;
              }
              innerParent = innerParent.getParent();
            }
            if (!isCandidateForInlining) {
              break;
            }
          }

          if (!isCandidateForInlining) {
            break;
          }
        }
      } else {
        // Non-object literals cannot be inlined
        isCandidateForInlining = false;
        break;
      }

      if (!isCandidateForInlining) {
        break;
      }
    } else {
      isCandidateForInlining = false;
      break;
    }
  }

  return isCandidateForInlining;
}
