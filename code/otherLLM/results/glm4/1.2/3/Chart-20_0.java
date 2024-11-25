import android.graphics.Paint;
import android.graphics.Stroke;

public class ValueMarker extends SuperClass {

    private double value;
    private Paint paint;
    private Stroke stroke;
    private Paint outlinePaint; // Assuming this is for the outline of the marker

    public ValueMarker(double value, Paint paint, Stroke stroke, 
                       Paint outlinePaint, Stroke outlineStroke, float alpha) {
        super(paint, stroke, paint, stroke, alpha);
        this.value = value;
        this.paint = paint;
        this.stroke = stroke;
        this.outlinePaint = outlinePaint; // Assign the outline paint
    }

    public Paint getOutlinePaint() { // Getter for outline paint
        return outlinePaint;
    }

    // Other methods and SuperClass constructor would go here...
}

// Assuming SuperClass is defined somewhere else, it should look something like:
class SuperClass {
    private Paint paint;
    private Stroke stroke;

    public SuperClass(Paint paint, Stroke stroke, Paint fillPaint, Stroke fillStroke, float alpha) {
        this.paint = paint;
        this.stroke = stroke;
        // Initialize fillPaint and fillStroke with some default values if needed
    }

    // Other methods for SuperClass...
}