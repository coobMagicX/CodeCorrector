public class DateUtil {
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        System.out.println("Initial Date: " + cal.getTime());
        
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // months are indexed from 0
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        cal.add(Calendar.YEAR, 10); // adds 10 years to the initial date
        System.out.println("Date after adding 10 years: " + cal.getTime());
    }
}