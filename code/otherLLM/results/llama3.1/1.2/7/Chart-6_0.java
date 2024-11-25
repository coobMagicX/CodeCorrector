public boolean equals(Object obj) {

    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }

    try {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(this.getClass().getClassLoader().getResourceAsStream("serializedShapeList")));
        ShapeList other = (ShapeList)in.readObject();
        in.close();

        return super.equals(other);
    } catch (IOException | ClassNotFoundException e) {
        // Handle exception
        return false;
    }

}