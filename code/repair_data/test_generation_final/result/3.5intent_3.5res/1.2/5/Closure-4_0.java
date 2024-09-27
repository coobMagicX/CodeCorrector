JSType resolveInternal(ErrorReporter t, StaticScope<JSType> enclosing) {
  boolean resolved = resolveViaRegistry(t, enclosing);
  if (detectImplicitPrototypeCycle(t)) {
    handleTypeCycle(t);
  }

  if (resolved) {
    super.resolveInternal(t, enclosing);
    finishPropertyContinuations();
    return registry.isLastGeneration() ?
        getReferencedType() : this;
  }

  resolveViaProperties(t, enclosing);
  if (detectImplicitPrototypeCycle(t)) {
    handleTypeCycle(t);
  }

  super.resolveInternal(t, enclosing);
  if (isResolved()) {
    finishPropertyContinuations();
  }
  return registry.isLastGeneration() ?
      getReferencedType() : this;
}

private boolean detectImplicitPrototypeCycle(ErrorReporter t) {
  Set<JSType> visited = new HashSet<>();
  return detectImplicitPrototypeCycleHelper(this, visited, t);
}

private boolean detectImplicitPrototypeCycleHelper(JSType type, Set<JSType> visited, ErrorReporter t) {
  if (visited.contains(type)) {
    return true;
  }

  visited.add(type);
  if (type instanceof JSTypeDeclaration) {
    JSTypeDeclaration decl = (JSTypeDeclaration) type;
    if (decl.isInterface()) {
      for (JSType implementedType : decl.getImplementedTypes()) {
        if (detectImplicitPrototypeCycleHelper(implementedType, visited, t)) {
          return true;
        }
      }
    } else {
      JSType superClass = decl.getSuperClass();
      if (superClass != null && detectImplicitPrototypeCycleHelper(superClass, visited, t)) {
        return true;
      }
    }
  }
  visited.remove(type);
  return false;
}