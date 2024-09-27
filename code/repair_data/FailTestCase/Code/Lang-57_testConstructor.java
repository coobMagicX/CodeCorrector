    public void testConstructor() {
        assertNotNull(new LocaleUtils());
        Constructor[] cons = LocaleUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertEquals(true, Modifier.isPublic(cons[0].getModifiers()));
        assertEquals(true, Modifier.isPublic(LocaleUtils.class.getModifiers()));
        assertEquals(false, Modifier.isFinal(LocaleUtils.class.getModifiers()));
    }