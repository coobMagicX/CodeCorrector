    public void testEquals() {
        ShapeList l1 = new ShapeList();
        l1.setShape(0, new Rectangle(1, 2, 3, 4));
        l1.setShape(1, new Line2D.Double(1.0, 2.0, 3.0, 4.0));
        l1.setShape(2, null);

        ShapeList l2 = new ShapeList();
        l2.setShape(0, new Rectangle(1, 2, 3, 4));
        l2.setShape(1, new Line2D.Double(1.0, 2.0, 3.0, 4.0));
        l2.setShape(2, null);

        assertTrue(l1.equals(l2));
        assertTrue(l2.equals(l2));
    }