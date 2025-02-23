public class OuterClass {
    private InnerClass strength = new InnerClass();
    
    public class InnerClass {
        public String fullStrength() {
            return "inner strength";
        }
    }

    public String displayStrength() {
        return strength.fullStrength();
    }
}

@Test
public void should_spy_inner_class() {
    OuterClass outer = new OuterClass();
    OuterClass.InnerClass spiedStrength = Mockito.spy(outer.new InnerClass());

    Mockito.when(spiedStrength.fullStrength()).thenReturn("inner strength");

    assertEquals("inner strength", spiedStrength.fullStrength());
    assertEquals("inner strength", outer.displayStrength());  // This line might still fail if `displayStrength` uses the original strength field.
}
