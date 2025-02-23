import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    String qName = nameNode.getQualifiedName();
    if (qName != null) {
      // Recognize Math functions as having no side-effects
      if(qName.startsWith("Math.") && MATH_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(qName.substring(5))) {
        return false;
      }
    }

    if (callNode.hasOneChild()
        && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(
              nameNode.getLastChild().getString())) {
      return false;
    }

    if (callNode.isOnlyModifiesThisCall()
        && evaluatesToLocalValue(nameNode.getFirstChild())) {
      return false;
    }

    if (compiler != null && !compiler.hasRegExpGlobalReferences()) {
      if (nameNode.getFirstChild().getType() == Token.REGEXP
          && REGEXP_METHODS.contains(nameNode.getLastChild().getString())) {
        return false;
      } else if (nameNode.getFirstChild().getType() == Token.STRING
          && STRING_REGEXP_METHODS.contains(
              nameNode.getLastChild().getString())) {
        Node param = nameNode.getNext();
        if (param != null &&
            (param.getType() == Token.STRING
                || param.getType() == Token.REGEXP))
        return false;
      }
    }
  }

  return true;
}

private static final Set<String> MATH_FUNCTIONS_WITHOUT_SIDEEFFECTS = new HashSet<String>(Arrays.asList(
    "abs", "acos", "asin", "atan", "atan2", "ceil", "cos", "exp", "floor", 
    "log", "max", "min", "pow", "random", "round", "sin", "sqrt", "tan"));
