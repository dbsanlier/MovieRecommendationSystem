import javax.swing.*;


public class Main {

    public static void main(String[] args) {

        String dir = System.getProperty("user.dir");

        DataLoader dataLoader = new DataLoader();

        try {
            dataLoader.loadMainData   (dir + "/main_data.csv");
            dataLoader.loadTargetUsers(dir + "/target_user.csv");
            dataLoader.loadMovies     (dir + "/movies.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "CSV files couldn't be read!\n\n" + e.getMessage()
                + "\n\nMake sure the files are in the working directory:\n" + dir,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        RecommendationEngine engine = new RecommendationEngine(dataLoader);

        SwingUtilities.invokeLater(() -> {
            MainScreen screen = new MainScreen(dataLoader, engine);
            screen.setVisible(true);
        });
    }
}
