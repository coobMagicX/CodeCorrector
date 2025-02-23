import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

private void replaceAssignmentExpression(Var v, Reference ref, Map<String, String> varmap) {
    List<Node> nodes = new ArrayList<Node>();
    Node val = ref.getAssignedValue();
    blacklistVarReferencesInTree(val, v.scope);
    
    Preconditions.checkState(val.isObjectLit(), "Value must be an object literal");

    Set<String> allVarNames = new HashSet<String>(varmap.keySet());

    for (Node key = val.getFirstChild(); key != null; key = key.getNext()) {
        String var = key.getString();
        if (varmap.containsKey(var)) {
            Node value = key.getFirstChild();
            if (value != null) {
                value.detach();
            }
            nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), value));
            allVarNames.remove(var);
        }
    }

    for (String var : allVarNames) {
        nodes.add(new Node(Token.ASSIGN, Node.newString(Token.NAME, varmap.get(var)), NodeUtil.newUndefinedNode(null)));
    }

    nodes.add(new Node(Token.TRUE));

    // Building a comma expression tree from all assignments
    Node replacement = new Node(Token.COMMA);
    if (!nodes.isEmpty()) {
        replacement.addChildToFront(nodes.get(nodes.size() - 1));
        for (int i = nodes.size() - 2; i >= 0; i--) {
            replacement = new Node(Token.COMMA, nodes.get(i), replacement);
        }
    }

    replacement.copyInformationFromForTree(ref.getParent());

    // Replace in the AST
    Node replace = ref.getParent();
    if (replace.getType() == Token.VAR) {
        replace.getParent().replaceChild(replace, new Node(Token.EXPR_RESULT, replacement));
    } else {
        replace.getParent().replaceChild(replace, replacement);
    }
}
