    public void testPcts() {
        f.addValue(oneL);
        f.addValue(twoL);
        f.addValue(oneI);
        f.addValue(twoI);
        f.addValue(threeL);
        f.addValue(threeL);
        f.addValue(3);
        f.addValue(threeI);
        assertEquals("one pct",0.25,f.getPct(1),tolerance);
        assertEquals("two pct",0.25,f.getPct(Long.valueOf(2)),tolerance);
        assertEquals("three pct",0.5,f.getPct(threeL),tolerance);
        // MATH-329
        assertEquals("three (Object) pct",0.5,f.getPct((Object) (Integer.valueOf(3))),tolerance);
        assertEquals("five pct",0,f.getPct(5),tolerance);
        assertEquals("foo pct",0,f.getPct("foo"),tolerance);
        assertEquals("one cum pct",0.25,f.getCumPct(1),tolerance);
        assertEquals("two cum pct",0.50,f.getCumPct(Long.valueOf(2)),tolerance);
        assertEquals("Integer argument",0.50,f.getCumPct(Integer.valueOf(2)),tolerance);
        assertEquals("three cum pct",1.0,f.getCumPct(threeL),tolerance);
        assertEquals("five cum pct",1.0,f.getCumPct(5),tolerance);
        assertEquals("zero cum pct",0.0,f.getCumPct(0),tolerance);
        assertEquals("foo cum pct",0,f.getCumPct("foo"),tolerance);
    }