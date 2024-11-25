import com.google.common.collect.Lists;

// Assuming the existence of these constants and classes:
final class Token {
    public static final int CALL = ...; // Example constant for token type.
    public static final int NAME = ...;
    public static final int GETPROP = ...;
    public static final int REGEXP = ...;
    public static final int STRING = ...;
    public static final String name(int type) {
        // Return the string representation of the token type
        return ...; // Implementation details.
    }
}

final class Node {
    public int getType() { /* Implementation details */ }
    public boolean isNoSideEffectsCall() { /* Implementation details */ }
    public boolean hasOneChild() { /* Implementation details */ }
    public boolean isOnlyModifiesThisCall() { /* Implementation details */ }
    public Node getFirstChild() { /* Implementation details */ }
    public Node getLastChild() { /* Implementation details */ }
    public Node getNext() { /* Implementation details */ }
}

final class AbstractCompiler {
    public boolean hasRegExpGlobalReferences() { /* Implementation details */ }
}

// Assuming the existence of these lists:
List<String> BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS = Lists.newArrayList(/* entries */);
Set<String> OBJECT_METHODS_WITHOUTSidEEFFECTS = new HashSet<>(/* entries */);
Set<String> REGEXP_METHODS = new HashSet<>(/* entries */);
Map<String, Set<String>> STRING_REGEXP_METHODS = new HashMap<>(/* entries */);

static boolean functionCallHasSideEffects(
    Node callNode, @Nullable AbstractCompiler compiler) {
  if (callNode.getType() != Token.CALL) {
    throw new IllegalStateException(
        "Expected CALL node, got " + Token.name(callNode.getType()));
  }

  if (callNode.isNoSideEffectsCall()) {
    return false;
  }

  Node nameNode = callNode.getFirstChild();

  // Built-in functions with no side effects.
  if (nameNode.getType() == Token.NAME) {
    String name = nameNode.getString();
    if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(name)) {
      return false;
    }
  } else if (nameNode.getType() == Token.GETPROP) {
    if (callNode.hasOneChild()
        && OBJECT_METHODS_WITHOUTSidEEFFECTS.contains(
              nameNode.getLastChild().getString())) {
      return false;
    }

    if (callNode.isOnlyModifiesThisCall()
        && evaluatesToLocalValue(nameNode.getFirstChild())) {
      return false;
    }

    // Functions in the "Math" namespace have no side effects.
    if (nameNode.getType() == Token.NAME) {
      String methodName = nameNode.getString();
      if ("Math".equals(methodName)) { // Check if the method is from the "Math" class
        // Assuming we have a way to check for methods with no side effects in Math
        boolean hasNoSideEffectsInMath = ...; // Implementation details
        if (hasNoSideEffectsInMath) {
          return false;
        }
      }
    }
  }

  return true;
}

// Helper method (assuming its existence)
private static boolean evaluatesToLocalValue(Node node) {
  // Implementation to determine if the expression evaluates to a local value.
  return ...; // Implementation details.
}