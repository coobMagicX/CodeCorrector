SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
    public int compare(EventState es0, EventState es1) {
        double t0 = es0.getEventTime();
        double t1 = es1.getEventTime();
        return (orderingSign) * Double.compare(t0, t1);
    }
});
