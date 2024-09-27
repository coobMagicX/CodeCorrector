protected void addChildren(int index, Node... children) {
    Validate.noNullElements(children);
    ensureChildNodes();
    // Start inserting from the first child to maintain the order correctly
    for (int i = 0; i < children.length; i++) {
        Node in = children[i];
        reparentChild(in);
        childNodes.add(index + i, in); // Adjust the index for each child to maintain sequential order
    }
    reindexChildren(index);
}