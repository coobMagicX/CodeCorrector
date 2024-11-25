public class BorderArrangement implements Arrangement, Serializable {

    private static final long serialVersionUID = 506071142274883745L;

    // ...

    @Override
    public Size2D arrange(BlockContainer container, Graphics2D g2, RectangleConstraint constraint) {
        // Implement the arrangement logic here
        return new Size2D(container.getWidth(), container.getHeight());
    }

    protected Size2D arrangeNN(BlockContainer container, Graphics2D g2) {
        // Implement no-constraint arrangement logic here
        return new Size2D(container.getWidth(), container.getHeight());
    }

    // ...
}