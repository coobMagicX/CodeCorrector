public class TimeSeries implements Cloneable {
    private List<RegularTimePeriod> data;

    // Other methods and constructors...

    @Override
    public Object clone() throws CloneNotSupportedException {
        // Create a deep copy of the original object using super.clone()
        TimeSeries clone = (TimeSeries) super.clone();
        
        // Ensure that the 'data' field is not shared between the original and the cloned instance
        clone.data = new java.util.ArrayList<>(this.data); // Use a shallow copy for data structure
        
        return clone;
    }

    public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
            throws CloneNotSupportedException {

        if (start == null) {
            throw new IllegalArgumentException("Null 'start' argument.");
        }
        if (end == null) {
            throw new IllegalArgumentException("Null 'end' argument.");
        }
        if (start.compareTo(end) > 0) {
            throw new IllegalArgumentException(
                    "Start period must not be after end period.");
        }
        boolean emptyRange = false;
        int startIndex = getIndex(start);
        if (startIndex < 0) {
            startIndex = -(startIndex + 1);
            if (startIndex == this.data.size()) {
                emptyRange = true; // start is after last data item
            }
        }
        int endIndex = getIndex(end);
        if (endIndex < 0) {             // end period is not in original series
            endIndex = -(endIndex + 1); // this is first item AFTER end period
            endIndex = endIndex - 1;    // so this is last item BEFORE end 
        }
        if (endIndex < 0) {
            emptyRange = true;
        }
        if (emptyRange) {
            TimeSeries copy = new TimeSeries(); // Create a new instance with an empty data list
            return copy;
        } else {
            TimeSeries copy = new TimeSeries(); // Create a new instance for the specified range
            copy.data = new java.util.ArrayList<>(this.data.subList(startIndex, endIndex + 1));
            return copy;
        }
    }

    public int getIndex(RegularTimePeriod period) {
        // Method to get index of period in data list
        // Implement this method based on your TimeSeries structure and logic
        // ...
    }

    public int getItemCount() {
        // Method to get the number of items in the series
        // Implement this method based on your TimeSeries structure and logic
        // ...
    }
}