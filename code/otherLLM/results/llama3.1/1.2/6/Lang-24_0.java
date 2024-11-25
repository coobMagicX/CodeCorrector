public class TestNumberUtil {
    @Test
    public void testIsNumber() {
        // Positive test cases
        assertTrue(NumberUtil.isNumber("11"));
        assertTrue(NumberUtil.isNumber("-11"));
        assertTrue(NumberUtil.isNumber("11.11"));
        assertTrue(NumberUtil.isNumber("2."));
        assertTrue(NumberUtil.isNumber("0x11"));

        // Negative test cases
        assertFalse(NumberUtil.isNumber(""));
        assertFalse(NumberUtil.isNumber("."));
        assertFalse(NumberUtil.isNumber("a"));
        assertFalse(NumberUtil.isNumber("11g"));
        assertFalse(NumberUtil.isNumber("11z"));
        assertFalse(NumberUtil.isNumber("11def"));
        assertFalse(NumberUtil.isNumber("11d11"));
        assertFalse(NumberUtil.isNumber("11 11"));
        assertFalse(NumberUtil.isNumber(" 1111"));
        assertFalse(NumberUtil.isNumber("1111 "));

        // Specific scenario test cases
        assertFalse(NumberUtil.isNumber("."));
        assertFalse(NumberUtil.isNumber("-0ABC123"));

        // LANG-521
        assertTrue(NumberUtil.isNumber("2."));

        // LANG-664
        assertFalse(NumberUtil.isNumber("1.1L"));

        // Empty string test case (utilize the existing method)
        assertFalse(NumberUtil.isNumber(StringUtils.EMPTY));

        // Decimal point without digits before it (utilize the existing method)
        assertFalse(NumberUtil.isNumber("."));
    }
}