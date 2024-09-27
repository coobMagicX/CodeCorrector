@RunWith(MockitoJUnitRunner.class)
public class InheritedGenericsPolimorphicCallTest {

    @Mock
    private Iterable<String> iterable;

    @Mock
    private Iterator<String> myIterator;

    @Test
    public void shouldStubbingWork() {
        when(iterable.iterator()).thenReturn(myIterator);

        Iterator<String> iterator = iterable.iterator();

        assertNotNull(iterator);
    }
}