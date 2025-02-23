SortedSet<EventState> occuringEvents = new TreeSet<EventState>(new Comparator<EventState>() {
    public int compare(EventState es0, EventState es1) {
        double t0 = es0.getEventTime();
        double t1 = es1.getEventTime();
        if (interpolator.isForward()) {
            return Double.compare(t0, t1);
        } else {
            return Double.compare(t1, t0);
        }
    }
});
