import java.io.Serializable;
import java.util.List;

public class ShapeList implements Serializable {
    private static final long serialVersionUID = 1L; // Needed for serialization compatibility

    private List<Shape> shapes;

    public ShapeList(List<Shape> shapes) {
        this.shapes = shapes;
    }

    // Getters and setters omitted for brevity

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShapeList)) {
            return false;
        }
        try {
            ShapeList other = (ShapeList) obj;
            // Assuming Shape also implements Serializable and has a proper equals method
            return this.shapes.equals(other.shapes);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Write the default fields
        // Custom serialization logic for complex objects can go here
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Read the default fields

        // After reading, validate or reassign the shapes list if necessary
        // For example, if we expect a non-null list:
        if (this.shapes == null) {
            this.shapes = new ArrayList<>(); // Or some other method to initialize your list
        }
    }

    // Other methods...
}

// Assuming Shape is also a serializable class with its own implementation of equals and hashCode.