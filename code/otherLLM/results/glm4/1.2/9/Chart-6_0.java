import java.io.*;
import java.util.*;

public class ShapeList implements Serializable {
    private List<Shape> shapes;

    // Existing methods...
    // ...

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShapeList)) {
            return false;
        }

        ShapeList other = (ShapeList) obj;
        if (this.shapes.size() != other.shapes.size()) {
            return false;
        }

        for (int i = 0; i < this.shapes.size(); i++) {
            if (this.shapes.get(i) == null ? other.shapes.get(i) != null : !this.shapes.get(i).equals(other.shapes.get(i))) {
                return false;
            }
        }

        return true;
    }

    // Existing methods...
    // ...
}