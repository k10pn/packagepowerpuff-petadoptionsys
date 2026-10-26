package petadoptionsystem;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ShelterFrame(new AdoptionService()).setVisible(true);
            } catch (Exception exception) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "The application could not start: " + exception.getMessage(),
                        "Startup Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
