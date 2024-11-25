public Size2D arrange(BlockContainer container,
                      Graphics2D g2,
                      RectangleConstraint constraint) {
    // Convert the given constraint to a content constraint
    RectangleConstraint contentConstraint = container.toContentConstraint(constraint);
    
    // Retrieve the width and height constraints
    LengthConstraintType wConstraint = contentConstraint.getWidthConstraintType();
    LengthConstraintType hConstraint = contentConstraint.getHeightConstraintType();

    Size2D contentSize;
    if (wConstraint == LengthConstraintType.FIXED && hConstraint == LengthConstraintType.FIXED) {
        // If both dimensions are fixed, we can directly calculate the size
        contentSize = arrangeFF(container, g2, constraint);
    } else if (wConstraint == LengthConstraintType.RANGE && hConstraint == LengthConstraintType.FIXED) {
        // Handle the case where width is a range and height is fixed
        // This would typically use arrangeFR method for fixed height with variable width
        contentSize = arrangeFR(container, g2, constraint);
    } else if (wConstraint == LengthConstraintType.FIXED && hConstraint == LengthConstraintType.RANGE) {
        // Handle the case where height is a range and width is fixed
        // This would typically use arrangeFR method for fixed width with variable height
        contentSize = arrangeFR(container, g2, constraint);
    } else if (wConstraint == LengthConstraintType.NONE && hConstraint == LengthConstraintType.NONE) {
        // Handle the case where there are no constraints
        // This would typically use arrangeNN method to calculate the minimum size
        contentSize = arrangeNN(container, g2);
    } else {
        throw new RuntimeException("Not implemented.");
    }

    // Calculate total width and height using the methods that consider the content's size
    Size2D totalSize = new Size2D(
            container.calculateTotalWidth(contentSize.getWidth()),
            container.calculateTotalHeight(contentSize.getHeight())
    );

    return totalSize;
}