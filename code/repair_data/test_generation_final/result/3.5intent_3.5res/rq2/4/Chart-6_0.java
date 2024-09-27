import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShapeList implements Serializable {
    private List<Shape> shapes;

    public ShapeList() {
        shapes = new ArrayList<>();
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShapeList)) {
            return false;
        }
        
        ShapeList other = (ShapeList) obj;
        
        if (shapes.size() != other.shapes.size()) {
            return false;
        }
        
        for (int i = 0; i < shapes.size(); i++) {
            Shape shape1 = shapes.get(i);
            Shape shape2 = other.shapes.get(i);
            
            if (!shape1.equals(shape2)) {
                return false;
            }
        }
        
        return true;
    }
}
