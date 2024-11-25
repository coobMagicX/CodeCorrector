DateTimeField field = BasicMonthDateTimeField.getInstance();
long instant = 1643723400000L; // January 1st, 2022

int month = field.get(instant); // Get the month from the instant
System.out.println("Month: " + month);

instant = field.add(instant, 6); // Add 6 months to the instant
System.out.println("Updated Instant: " + instant);