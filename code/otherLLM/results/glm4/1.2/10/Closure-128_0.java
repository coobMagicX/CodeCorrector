import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleNumberTest {

    @Test
    public void testIsSimpleNumber() {
        assertTrue(isSimpleNumber("123"));
        assertFalse(isSimpleNumber("12a3"));
        assertFalse(isSimpleNumber("0123"));
        assertFalse(isSimpleNumber(" 123")); // This should fail the assertion
        assertFalse(isSimpleNumber("123 "));
        assertFalse(isSimpleNumber(" "));   // This should also fail the assertion
    }
}