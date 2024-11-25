public class Main {
    public static void main(String[] args) {
        try {
            Number num = NumberUtils.createNumber("0xfade");
            assertTrue("createNumber(String) 9b failed", num instanceof Integer && ((Integer) num).intValue() == 0xFADE);
        } catch (NumberFormatException e) {
            fail("createNumber(String) 9b failed: " + e.getMessage());
        }
    }
}