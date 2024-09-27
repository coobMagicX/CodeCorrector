public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof ShapeList)) {
        return false;
    }
    ShapeList other = (ShapeList) obj;
    if (this.size() != other.size()) {
        return false;
    }
    for (int i = 0; i < this.size(); i++) {
        if (!this.get(i).equals(other.get(i))) {
            return false;
        }
    }
    return true;
}

public void testSerialization() {
    ShapeList l1 = new ShapeList();
    l1.add(new Rectangle(1, 2, 3, 4));
    l1.add(new Circle(5, 6, 7));

    ShapeList l2 = null;
    try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(l1);
        oos.flush();
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        l2 = (ShapeList) ois.readObject();
    } catch (Exception e) {
        fail("Serialization failed: " + e.getMessage());
    }

    assertEquals(l1, l2);
}