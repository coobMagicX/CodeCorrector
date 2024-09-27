    public void shouldVerificationWorks() {
        iterable.iterator();
        
        verify(iterable).iterator();
        verify((Iterable) iterable).iterator();
    }