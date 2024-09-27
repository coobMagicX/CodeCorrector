    public void testProperties()
    {
        TypeFactory tf = TypeFactory.defaultInstance();
        JavaType t = tf.constructType(Properties.class);
        assertEquals(MapType.class, t.getClass());
        assertSame(Properties.class, t.getRawClass());

        // so far so good. But how about parameterization?
        assertSame(String.class, ((MapType) t).getKeyType().getRawClass());
        assertSame(String.class, ((MapType) t).getContentType().getRawClass());
    }