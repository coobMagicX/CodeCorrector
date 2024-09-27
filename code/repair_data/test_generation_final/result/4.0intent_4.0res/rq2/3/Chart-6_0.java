import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShapeList implements Serializable {
    private List<Shape> shapes = new ArrayList<>();

    // Other methods for ShapeList

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
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
            Shape s1 = this.shapes.get(i);
            Shape s2 = other.shapes.get(i);
            if (s1 == null && s2 == null) {
                continue;
            }
            if (s1 == null || s2 == null || !s1.equals(s2)) {
                return false;
            }
        }
        return true;
    }
}