private int compareNodePointers(
    NodePointer p1,
    int depth1,
    NodePointer p2,
    int depth2) 
{
    if (depth1 < depth2) {
        int r = compareNodePointers(p1, depth1, p2.parent, depth2 - 1);
        return r == 0 ? -1 : r;
    }
    if (depth1 > depth2) {
        int r = compareNodePointers(p1.parent, depth1 - 1, p2, depth2);
        return r == 0 ? 1 : r;
    }
    if (p1 == null && p2 == null) {
        return 0;
    }

    if (p1 != null && p1.equals(p2)) {
        return 0;
    }

    if (depth1 == 1) {
        throw new JXPathException(
                "Cannot compare pointers that do not belong to the same tree: '"
                        + p1 + "' and '" + p2 + "'");
    }
    int r = compareNodePointers(p1.parent, depth1 - 1, p2.parent, depth2 - 1);
    if (r != 0) {
        return r;
    }

    // Check if the pointers are equal using the equals() method
    if (p1 != null && p2 != null && p1.equals(p2)) {
        return 0;
    }

    // Compare the pointers based on their class names
    String p1ClassName = p1.getClass().getName();
    String p2ClassName = p2.getClass().getName();
    int classNameComparison = p1ClassName.compareTo(p2ClassName);
    if (classNameComparison != 0) {
        return classNameComparison;
    }

    // Compare the pointers based on their hash codes
    int p1HashCode = p1.hashCode();
    int p2HashCode = p2.hashCode();
    return Integer.compare(p1HashCode, p2HashCode);
}