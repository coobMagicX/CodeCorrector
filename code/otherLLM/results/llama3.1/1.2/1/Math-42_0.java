public class Main {
    public static void main(String[] args) {
        Tableau tableau = new Tableau();
        
        // Initialize tableau with some data
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tableau.setEntry(i, j, Math.random());
            }
        }
        
        // Print the tableau data
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(tableau.getEntry(i, j) + " ");
            }
            System.out.println();
        }

        // Serialize and deserialize the tableau
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tableau.dat"));
             ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tableau.dat"))) {
            
            // Write the tableau to a file
            oos.writeObject(tableau);
            
            // Read the tableau from the file
            Tableau readTableau = (Tableau) ois.readObject();
            
            System.out.println("Read data from file:");
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    System.out.print(readTableau.getEntry(i, j) + " ");
                }
                System.out.println();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error occurred: " + e.getMessage());
        }
    }
}