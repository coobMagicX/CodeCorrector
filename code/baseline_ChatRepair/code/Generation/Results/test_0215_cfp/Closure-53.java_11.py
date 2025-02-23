import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Var;
import com.google.javascript.jscomp.Reference;

private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
  List<Node> nodes = new ArrayList<Node>();
  Node val = ref.getAssignedValue();
  blacklistVarReferencesInTree(val, v.scope);
  Preconditions.checkState(val.getType() == Token.OBJECTLIT);

  for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
    String var = key.getString();
    Node value = key.removeFirstChild();
    // TODO(user): Copy type information.
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)), value));
  }

  Set<String> all = new LinkedHashSet<String>(varmap.keySet());
  for (Node node : nodes) {
    all.remove(node.getFirstChild().getString());
  }

  for (String var : all) {
    nodes.add(
      new Node(Token.ASSIGN,
               Node.newString(Token.NAME, varmap.get(var)),
               NodeUtil.newUndefinedNode(null)));
  }

  Node replacement = NodeUtil.newExpr(nodes.get(0));
  for (int i = 1; i < nodes.size(); i++) {
    replacement = new Node(Token.COMMA, replacement, nodes.get(i));
  }

  Node replace = ref.getParent();
  replacement.copyInformationFromForTree(replace);

  if (replace.getType() == Token.VAR) {
    replace.getParent().replaceChild(
        replace, NodeUtil.newExpr(replacement));
  } else {
    replace.getParent().replaceChild(replace, replacement);
  }
}
