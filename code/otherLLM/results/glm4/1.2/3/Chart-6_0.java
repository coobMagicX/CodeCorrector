import java.io.Serializable;

public class ShapeList implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<Shape> shapes;

    // Constructor, getters, setters, and other methods are assumed to be here.

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof ShapeList)) {
            return false;
        }

        ShapeList other = (ShapeList) obj;
        if (this.shapes.size() != other.shapes.size()) {
            return false;
        }

        // Assuming that the equals method of Shape is correctly implemented
        for (int i = 0; i < this.shapes.size(); i++) {
            if (!this.shapes.get(i).equals(other.shapes.get(i))) {
                return false;
            }
        }

        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        // Deep cloning the ShapeList to avoid shared references issues
        ShapeList cloned = (ShapeList) super.clone();
        cloned.shapes = new ArrayList<>(this.shapes.size());
        for (Shape shape : this.shapes) {
            cloned.shapes.add(shape.clone());
        }
        return cloned;
    }

    // serialization and deserialization methods are assumed to be here.
}