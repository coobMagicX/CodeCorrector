public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    // Verify null checks and implement deep equality comparison for shapes within the ShapeList
    ShapeList other = (ShapeList) obj;
    if (this.shapes != null && other.shapes != null) {
        // Check if both lists have the same number of shapes
        if (this.shapes.size() != other.shapes.size()) {
            return false;
        }
        
        // Perform deep equality comparison for each shape in the list
        for (int i = 0; i < this.shapes.size(); i++) {
            Shape myShape = this.shapes.get(i);
            Shape theirShape = other.shapes.get(i);
            
            if (!myShape.equals(theirShape)) {
                return false;
            }
        }
    } else if ((this.shapes != null) ^ (other.shapes != null)) { // Use XOR operator to check for one or the other being null
        return false; // One ShapeList is null and the other is not, so they're not equal
    }

    return super.equals(obj);
}