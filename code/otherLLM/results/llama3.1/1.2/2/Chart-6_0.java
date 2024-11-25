public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    // Compare the contents of ShapeList objects
    try {
        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(SerialUtilities.serialize((ShapeList)obj)));
        stream.defaultReadObject();
        int count = stream.readInt();
        for (int i = 0; i < count; i++) {
            int index = stream.readInt();
            if (index != -1) {
                Shape shape = SerialUtilities.readShape(stream);
                Shape myShape = getShape(index);
                if (myShape == null || !shape.equals(myShape)) {
                    return false;
                }
            }
        }
    } catch (IOException | ClassNotFoundException e) {
        // Handle serialization exception
        return false;
    }

    // If we've reached this point, the ShapeList objects are equal
    return true;

}