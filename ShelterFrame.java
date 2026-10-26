package petadoptionsystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShelterFrame extends JFrame {
    private final AdoptionService service; private final DefaultListModel<String> petModel = new DefaultListModel<>();
    private final JList<String> petList = new JList<>(petModel); private int page = 0; private String category = "ALL";
    public ShelterFrame(AdoptionService service) { this.service = service; setTitle("Powerpuff Shelter"); setSize(760, 520); setLocationRelativeTo(null); setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); build(); refreshPets(); addWindowListener(new java.awt.event.WindowAdapter() { public void windowClosing(java.awt.event.WindowEvent e) { service.saveNow(); dispose(); } }); }
    private void build() {
        JPanel root = new JPanel(new BorderLayout(10, 10)); root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        JLabel title = new JLabel("POWERPUFF SHELTER — Pet Adoption Management System", SwingConstants.CENTER); title.setFont(new Font("SansSerif", Font.BOLD, 18)); root.add(title, BorderLayout.NORTH);
        JPanel controls = new JPanel(); JButton browse = new JButton("Find a Pet / Browse"), profile = new JButton("Fill in Information"), appointments = new JButton("List of Appointments"), exit = new JButton("Exit and Save");
        controls.add(browse); controls.add(profile); controls.add(appointments); controls.add(exit); root.add(controls, BorderLayout.SOUTH);
        root.add(new JScrollPane(petList), BorderLayout.CENTER); browse.addActionListener(e -> browseDialog()); profile.addActionListener(e -> profileDialog()); appointments.addActionListener(e -> appointmentsDialog()); exit.addActionListener(e -> { service.saveNow(); dispose(); }); setContentPane(root);
    }
    private void refreshPets() { petModel.clear(); for (Pet p : service.getPage(page, category)) petModel.addElement(p.shortDescription()); }
    private void browseDialog() {
        JPanel panel = new JPanel(new BorderLayout(8,8)); JComboBox<String> filter = new JComboBox<>(new String[]{"ALL", "Cat", "Dog"}); JPanel nav = new JPanel(); JButton prev = new JButton("Previous"), next = new JButton("Next"), view = new JButton("View / Adopt"), back = new JButton("Back"); nav.add(prev); nav.add(next); nav.add(view); nav.add(back); panel.add(filter, BorderLayout.NORTH); panel.add(new JScrollPane(petList), BorderLayout.CENTER); panel.add(nav, BorderLayout.SOUTH);
        JDialog dialog = new JDialog(this, "Browse Available Pets", true); dialog.setContentPane(panel); dialog.setSize(650, 400); dialog.setLocationRelativeTo(this); page = 0; refreshPets();
        filter.addActionListener(e -> { category = (String) filter.getSelectedItem(); page = 0; refreshPets(); }); prev.addActionListener(e -> { if (page > 0) { page--; refreshPets(); } }); next.addActionListener(e -> { if (page + 1 < service.pageCount(category)) { page++; refreshPets(); } }); view.addActionListener(e -> { int i = petList.getSelectedIndex(); List<Pet> current = service.getPage(page, category); if (i >= 0 && i < current.size()) adoptDialog(current.get(i)); }); back.addActionListener(e -> dialog.dispose()); dialog.setVisible(true); category = "ALL"; page = 0; refreshPets();
    }
    private void adoptDialog(Pet pet) {
        JTextArea area = new JTextArea(pet.fullDescription()); area.setEditable(false); area.setLineWrap(true); area.setWrapStyleWord(true); int answer = JOptionPane.showConfirmDialog(this, new JScrollPane(area), "Pet Details", JOptionPane.YES_NO_OPTION); if (answer != JOptionPane.YES_OPTION) return;
        AdopterProfile p = service.getProfile(); if (p == null) p = profileDialog(); if (p == null || p.getAge() < 18) { JOptionPane.showMessageDialog(this, "You must be at least 18 years old to adopt."); return; }
        JTextField date = new JTextField("DD-MM-YYYY"), time = new JTextField("08:00 / 10:00 / 14:00"); JPanel form = new JPanel(new GridLayout(2,2)); form.add(new JLabel("Appointment date:")); form.add(date); form.add(new JLabel("Available time:")); form.add(time);
        if (JOptionPane.showConfirmDialog(this, form, "Set Appointment", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) try { LocalDate d = LocalDate.parse(date.getText().trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy")); LocalTime t = LocalTime.parse(time.getText().trim()); service.bookAppointment(pet, p, d, t); JOptionPane.showMessageDialog(this, "Appointment set successfully."); refreshPets(); } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Appointment", JOptionPane.ERROR_MESSAGE); }
    }
    private AdopterProfile profileDialog() {
        JTextField name = new JTextField(), gender = new JTextField(), address = new JTextField(), age = new JTextField(), living = new JTextField(); JPanel form = new JPanel(new GridLayout(5,2,5,5)); String[] labels = {"Full name:","Gender:","Address:","Age:","Living situation:"}; JTextField[] fields = {name,gender,address,age,living}; for (int i=0;i<labels.length;i++){form.add(new JLabel(labels[i]));form.add(fields[i]);}
        if (JOptionPane.showConfirmDialog(this, form, "Adopter Information", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return null; try { AdopterProfile p = new AdopterProfile(name.getText(), gender.getText(), address.getText(), Integer.parseInt(age.getText()), living.getText()); service.setProfile(p); return p; } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Invalid Information", JOptionPane.ERROR_MESSAGE); return null; }
    }
    private void appointmentsDialog() { List<Appointment> list = service.getAppointments(); if (list.isEmpty()) { JOptionPane.showMessageDialog(this, "No appointments found."); return; } String[] choices = new String[list.size()]; for (int i=0;i<list.size();i++) choices[i] = (i+1)+". "+list.get(i).summary(); String selected = (String) JOptionPane.showInputDialog(this, "Select an appointment:", "Appointments", JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]); if (selected == null) return; int index = Integer.parseInt(selected.substring(0, selected.indexOf('.'))) - 1; Appointment a = list.get(index); Object[] options = {"Close", "Cancel appointment", "Mark completed"}; int result = JOptionPane.showOptionDialog(this, a.details(), "Appointment Details", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]); if (result == 1) { service.cancelAppointment(a.getId()); refreshPets(); JOptionPane.showMessageDialog(this, "Appointment cancelled and the slot is available again."); } else if (result == 2) { service.markCompleted(a.getId()); JOptionPane.showMessageDialog(this, "Appointment marked as completed."); } }
}
