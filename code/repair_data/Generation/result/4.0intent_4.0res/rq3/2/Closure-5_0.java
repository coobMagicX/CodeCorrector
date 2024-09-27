import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

public class InlineObjectChecker {

    private boolean isInlinableObject(List<Reference> refs) {
        boolean ret = false;
        Set<String> validProperties = Sets.newHashSet();

        for (Reference ref : refs) {
            Node name = ref.getNode();
            Node parent = ref.getParent();
            Node gramps = ref.getGrandparent();

            if (parent.isGetProp()) {
                Preconditions.checkState(parent.getFirstChild() == name);
                if (gramps.isCall() && gramps.getFirstChild() == parent) {
                    return false;
                }

                String propName = parent.getLastChild().getString();
                if (!validProperties.contains(propName)) {
                    if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
                        validProperties.add(propName);
                    } else {
                        return false;
                    }
                }
                continue;
            }

            if (!NodeUtil.isVarOrSimpleAssignLhs(name, parent)) {
                return false;
            }

            Node val = ref.getAssignedValue();
            if (val == null) {
                continue;
            }

            if (!val.isObjectLit()) {
                return false;
            }

            for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
                if (child.isGetterDef() || child.isSetterDef()) {
                    return false;
                }

                validProperties.add(child.getString());

                Node childVal = child.getFirstChild();
                for (Reference t : refs) {
                    Node refNode = t.getNode();
                    while (refNode != null && !NodeUtil.isStatementBlock(refNode)) {
                        if (refNode == childVal) {
                            return false;
                        }
                        refNode = refNode.getParent();
                    }
                }
            }

            ret = true;
        }

        return ret;
    }
}