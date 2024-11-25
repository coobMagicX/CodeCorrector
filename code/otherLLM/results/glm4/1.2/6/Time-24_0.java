DateTimeParserBucket bucket = new DateTimeParserBucket();
bucket.setParsedField("year", "2023");
bucket.setParsedField("month", "4");
bucket.setParsedField("day", "15");

long millis = bucket.computeMillis(true);
System.out.println(millis); // Outputs the milliseconds for the date 2023-04-15