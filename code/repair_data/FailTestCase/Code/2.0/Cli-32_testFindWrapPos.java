    public void testFindWrapPos() throws Exception
    {
        HelpFormatter hf = new HelpFormatter();

        String text = "This is a test.";
        // text width should be max 8; the wrap position is 7
        assertEquals("wrap position", 7, hf.findWrapPos(text, 8, 0));
        
        // starting from 8 must give -1 - the wrap pos is after end
        assertEquals("wrap position 2", -1, hf.findWrapPos(text, 8, 8));
        
        // words longer than the width are cut
        text = "aaaa aa";
        assertEquals("wrap position 3", 3, hf.findWrapPos(text, 3, 0));
        
        // last word length is equal to the width
        text = "aaaaaa aaaaaa";
        assertEquals("wrap position 4", 6, hf.findWrapPos(text, 6, 0));
        assertEquals("wrap position 4", -1, hf.findWrapPos(text, 6, 7));
    }