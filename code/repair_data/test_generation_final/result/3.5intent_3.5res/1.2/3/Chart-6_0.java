import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShapeList implements Serializable {

    private List<Shape> shapes;

    public ShapeList() {
        shapes = new ArrayList<>();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ShapeList)) {
            return false;
        }
        
        ShapeList otherList = (ShapeList) obj;
        if (shapes.size() != otherList.shapes.size()) {
            return false;
        }
        
        for (int i = 0; i < shapes.size(); i++) {
            if (!shapes.get(i).equals(otherList.shapes.get(i))) {
                return false;
            }
        }
        
        return true;
    }

}