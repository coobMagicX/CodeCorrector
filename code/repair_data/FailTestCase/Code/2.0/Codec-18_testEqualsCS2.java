    public void testEqualsCS2() {
        Assert.assertTrue(StringUtils.equals("abc", new StringBuilder("abc")));
        Assert.assertFalse(StringUtils.equals(new StringBuilder("abc"), "abcd"));
        Assert.assertFalse(StringUtils.equals("abcd", new StringBuilder("abc")));
        Assert.assertFalse(StringUtils.equals(new StringBuilder("abc"), "ABC"));
    }