package petadoptionsystem;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ShelterFrame extends JFrame {

    private final AdoptionService service;
    private final DefaultListModel<String> petModel =
            new DefaultListModel<>();
    private final JList<String> petList =
            new JList<>(petModel);

    private int page = 0;
    private String category = "ALL";

    public ShelterFrame(AdoptionService service) {
        this.service = service;

        setTitle("Powerpuff Shelter");
        setMinimumSize(new Dimension(760, 420));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        build();
        pack();
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                service.saveNow();
                dispose();
            }
        });
        
    }
    
    private final CardLayout cardLayout = new CardLayout();

    private final JPanel contentPanel =
            new JPanel(cardLayout);

    private static final String HOME_SCREEN = "HOME";
    private static final String BROWSE_SCREEN = "BROWSE";
    
    private void styleOptionButtons(
        Container container
        ) {
            for (Component component :
                    container.getComponents()) {

                if (component instanceof JButton) {
                    JButton button =
                            (JButton) component;

                    styleButton(button);
                }

                if (component instanceof Container) {
                    styleOptionButtons(
                            (Container) component
                    );
                }
            }
        }
    
    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(190, 42));
        button.setMinimumSize(new Dimension(190, 42));
        button.setMaximumSize(new Dimension(190, 42));

        button.setBackground(
                new Color(138, 96, 56)
        );

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(138, 96, 56),
                        2,
                        true
                )
        );

        button.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );
    }

    private void fitDialogToButtons(
            JDialog dialog,
            int buttonCount
    ) {
        dialog.pack();

        int buttonWidth = 190;
        int buttonGap = 8;
        int sidePadding = 80;

        int requiredWidth =
                buttonCount * buttonWidth
                + Math.max(0, buttonCount - 1) * buttonGap
                + sidePadding;

        Dimension currentSize = dialog.getSize();

        dialog.setSize(
                Math.max(currentSize.width, requiredWidth),
                currentSize.height
        );

        dialog.setLocationRelativeTo(this);
    }
    
    private void showHomeScreen() {
        cardLayout.show(contentPanel, HOME_SCREEN);

        contentPanel.revalidate();
        contentPanel.repaint();

        setSize(760, 420);
        setLocationRelativeTo(null);
    }

    private void showBrowseScreen() {
        JPanel browsePanel = createBrowsePanel();

        contentPanel.add(browsePanel, BROWSE_SCREEN);

        contentPanel.revalidate();
        contentPanel.repaint();

        cardLayout.show(contentPanel, BROWSE_SCREEN);

        pack();
        setLocationRelativeTo(null);
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );


        JLabel imageLabel = new JLabel();

        java.net.URL imageURL = getClass().getResource(
                "/petadoptionsystem/images/mainmenu.png"
        );

        if (imageURL == null) {
            throw new IllegalArgumentException(
                    "Image not found: mainmenu.png"
            );
        }

        ImageIcon originalIcon = new ImageIcon(imageURL);

        Image scaledImage = originalIcon.getImage().getScaledInstance(
                500,                 // width
                350,                 // height
                Image.SCALE_SMOOTH
        );

        imageLabel.setIcon(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel welcomePanel = new JPanel(new GridBagLayout());

        welcomePanel.setBorder(
                BorderFactory.createEmptyBorder(8, 0, 0, 0)
        );

        GridBagConstraints imageConstraints =
                new GridBagConstraints();

        imageConstraints.gridx = 0;
        imageConstraints.gridy = 0;
        imageConstraints.anchor = GridBagConstraints.NORTH;
        imageConstraints.weightx = 1.0;
        imageConstraints.weighty = 1.0;

        welcomePanel.add(imageLabel, imageConstraints);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBorder(
                BorderFactory.createEmptyBorder(8, 0, 8, 8)
        );

        JButton browse = new JButton("Find a Pet / Browse");
        JButton profile = new JButton("Fill in Information");
        JButton appointments = new JButton("List of Appointments");
        JButton exit = new JButton("Exit and Save");

        JButton[] buttons = {
                browse, profile, appointments, exit
        };

        Dimension buttonSize = new Dimension(190, 42);

        for (JButton button : buttons) {
            styleButton(button);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);

            controls.add(button);
           

            if (button != exit) {
                controls.add(Box.createVerticalStrut(4));
            }
        }
        
        JLabel lowerImageLabel = new JLabel();

        java.net.URL lowerImageURL = getClass().getResource(
                "/petadoptionsystem/images/mainmenu2.png"
        );

        if (lowerImageURL == null) {
            throw new IllegalArgumentException(
                    "Image not found: mainmenu2.png"
            );
        }

        ImageIcon lowerOriginalIcon = new ImageIcon(lowerImageURL);

        Image lowerScaledImage =
                lowerOriginalIcon.getImage().getScaledInstance(
                        210,                 // width
                        166,                 // height
                        Image.SCALE_SMOOTH
                );

        lowerImageLabel.setIcon(
                new ImageIcon(lowerScaledImage)
        );

        lowerImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        controls.add(Box.createVerticalStrut(10));
        controls.add(lowerImageLabel);

        JPanel mainArea = new JPanel(new BorderLayout(12, 0));

        mainArea.add(controls, BorderLayout.WEST);
        contentPanel.add(welcomePanel, HOME_SCREEN);
        mainArea.add(contentPanel, BorderLayout.CENTER);

        root.add(mainArea, BorderLayout.CENTER);
        
        Color backgroundColor = new Color(255, 253, 251);

        root.setBackground(backgroundColor);
        mainArea.setBackground(backgroundColor);
        controls.setBackground(backgroundColor);
        welcomePanel.setBackground(backgroundColor);
        

        browse.addActionListener(e -> showBrowseScreen());
        profile.addActionListener(e -> profileDialog());
        appointments.addActionListener(e -> appointmentsDialog());

        exit.addActionListener(e -> {
            service.saveNow();
            dispose();
        });

        setContentPane(root);
    }

    private void refreshPets() {
        petModel.clear();

        for (Pet p : service.getPage(page, category)) {
            petModel.addElement(
                    p.getName()
                    + " — "
                    + p.getSpecies()
                    + ", "
                    + p.getBreed()
            );
        }
    }

    private JPanel createBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setPreferredSize(new Dimension(850, 400));

        JComboBox<String> filter =
                new JComboBox<>(new String[]{"ALL", "Cat", "Dog"});

        filter.setFont(new Font("SansSerif", Font.PLAIN, 18));

        petList.setFont(
                new Font("SansSerif", Font.PLAIN, 23)
        );

        petList.setFixedCellHeight(34);

        JPanel nav = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 8, 8)
        );

        JButton prev = new JButton("Previous");
        JButton next = new JButton("Next");
        JButton view = new JButton("View / Adopt");
        JButton back = new JButton("Back");

        JButton[] navigationButtons = {
                prev, next, view, back
        };

        for (JButton button : navigationButtons) {
            styleButton(button);
            nav.add(button);
        }

        panel.add(filter, BorderLayout.NORTH);
        panel.add(new JScrollPane(petList), BorderLayout.CENTER);
        panel.add(nav, BorderLayout.SOUTH);

        category = "ALL";
        page = 0;
        refreshPets();

        filter.addActionListener(e -> {
            category = (String) filter.getSelectedItem();
            page = 0;
            refreshPets();
        });

        prev.addActionListener(e -> {
            if (page > 0) {
                page--;
                refreshPets();
            }
        });

        next.addActionListener(e -> {
            if (page + 1 < service.pageCount(category)) {
                page++;
                refreshPets();
            }
        });

        view.addActionListener(e -> {
            int selectedIndex = petList.getSelectedIndex();

            List<Pet> currentPets =
                    service.getPage(page, category);

            if (selectedIndex >= 0
                    && selectedIndex < currentPets.size()) {
                adoptDialog(currentPets.get(selectedIndex));
            }
        });

        back.addActionListener(e -> {
            category = "ALL";
            page = 0;
            showHomeScreen();
        });

        return panel;
    }
    
    private ImageIcon createSquarePetIcon(
        java.net.URL imageURL,
        int imageSize
    ) {
        if (imageURL == null) {
            return null;
        }

        try {
            BufferedImage originalImage =
                    ImageIO.read(imageURL);

            int originalWidth =
                    originalImage.getWidth();

            int originalHeight =
                    originalImage.getHeight();

            int squareSize =
                    Math.min(
                            originalWidth,
                            originalHeight
                    );

            int cropX =
                    (originalWidth - squareSize) / 2;

            int cropY =
                    (originalHeight - squareSize) / 2;

            BufferedImage croppedImage =
                    originalImage.getSubimage(
                            cropX,
                            cropY,
                            squareSize,
                            squareSize
                    );

            BufferedImage resizedImage =
                    new BufferedImage(
                            imageSize,
                            imageSize,
                            BufferedImage.TYPE_INT_ARGB
                    );

            Graphics2D graphics =
                    resizedImage.createGraphics();

            graphics.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR
            );

            graphics.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            graphics.drawImage(
                    croppedImage,
                    0,
                    0,
                    imageSize,
                    imageSize,
                    null
            );

            graphics.dispose();

            return new ImageIcon(resizedImage);

        } catch (IOException | RuntimeException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private void adoptDialog(Pet pet) {
        JTextPane area = new JTextPane();

        area.setEditable(false);
        area.setFont(
                new Font("SansSerif", Font.PLAIN, 20)
        );
        area.setMargin(new Insets(8, 8, 8, 8));

        StyledDocument document = area.getStyledDocument();

        Style regularStyle = area.addStyle(
                "Regular",
                null
        );
        Style boldStyle = area.addStyle(
                "Bold",
                null
        );

        StyleConstants.setFontFamily(
                regularStyle,
                "SansSerif"
        );
        StyleConstants.setFontSize(
                regularStyle,
                24
        );

        StyleConstants.setFontFamily(
                boldStyle,
                "SansSerif"
        );
        StyleConstants.setFontSize(
                boldStyle,
                24
        );
        StyleConstants.setBold(
                boldStyle,
                true
        );

        String[] lines =
                pet.fullDescription().split("\\R");

        try {
            for (String line : lines) {
                int colonIndex = line.indexOf(':');

                if (colonIndex >= 0) {
                    document.insertString(
                            document.getLength(),
                            line.substring(0, colonIndex + 1),
                            boldStyle
                    );

                    document.insertString(
                            document.getLength(),
                            line.substring(colonIndex + 1) + "\n",
                            regularStyle
                    );
                } else {
                    document.insertString(
                            document.getLength(),
                            line + "\n",
                            regularStyle
                    );
                }
            }
        } catch (BadLocationException exception) {
            exception.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(
                new Dimension(650, 400)
        );

        String imageFileName =
                pet.getName().toLowerCase(Locale.ROOT) + ".png";

        java.net.URL petImageURL = getClass().getResource(
                "/petadoptionsystem/images/" + imageFileName
        );

        int imageSize = 300;

        ImageIcon petIcon =
                createSquarePetIcon(
                        petImageURL,
                        imageSize
                );

        JLabel petImageLabel = new JLabel();

        if (petIcon != null) {
            petImageLabel.setIcon(petIcon);
        }

        petImageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        petImageLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        JPanel imagePanel =
                new JPanel(new BorderLayout());

        imagePanel.add(
                petImageLabel,
                BorderLayout.CENTER
        );

        JPanel detailsPanel =
                new JPanel(new BorderLayout(0, 10));

        detailsPanel.add(
                imagePanel,
                BorderLayout.NORTH
        );

        detailsPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        detailsPanel.setPreferredSize(
                new Dimension(650, 650)
        );

        Object[] options = {
                "Cancel",
                "Proceed"
        };

        JOptionPane optionPane = new JOptionPane(
                detailsPanel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                options[0]
        );

        JDialog dialog = optionPane.createDialog(
                this,
                "Pet Details"
        );

        styleOptionButtons(optionPane);
        dialog.setVisible(true);

        Object selectedOption = optionPane.getValue();

        if (!"Proceed".equals(selectedOption)) {
            return;
        }

        AdopterProfile profile = service.getProfile();

        if (profile == null) {
            profile = profileDialog();
        }

        if (profile == null) {
            return;
        }

        if (profile.getAge() < 18) {
            showUnderageDialog();
            return;
        }

        JTextField date = new JTextField();
        date.setPreferredSize(new Dimension(180, 35));
        addPlaceholder(date, "DD-MM-YYYY");

        JComboBox<String> time = new JComboBox<>(
                new String[]{
                        "08:00",
                        "10:00",
                        "14:00"
                }
        );

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setOpaque(true);

        date.setPreferredSize(new Dimension(220, 25));
        date.setMinimumSize(new Dimension(220, 25));

        time.setPreferredSize(new Dimension(220, 25));
        time.setMinimumSize(new Dimension(220, 25));
        time.setBackground(Color.WHITE);
        time.setForeground(Color.BLACK);
        time.setOpaque(true);
        time.setUI(new javax.swing.plaf.basic.BasicComboBoxUI());
        time.setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 210, 210),
                        1
                )
        );

        time.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        false,
                        false
                );

                label.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                return label;
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        // Appointment date label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        form.add(new JLabel("Appointment date:"), gbc);

        // Appointment date text box
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(date, gbc);

        // Available time label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        form.add(new JLabel("Available time:"), gbc);

        // Available time dropdown
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(time, gbc);

        Object[] appointmentOptions = {
        "Cancel",
        "Proceed"
        };
        
        ImageIcon appointmentPetIcon =
        createSquarePetIcon(petImageURL, 150);
      

            JOptionPane appointmentPane = new JOptionPane(
                    form,
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.DEFAULT_OPTION,
                    appointmentPetIcon,
                    appointmentOptions,
                    appointmentOptions[0]
            );

            JDialog appointmentDialog =
                    appointmentPane.createDialog(
                            this,
                            "Set Appointment"
                    );

            // Makes Cancel and Proceed match the other buttons
            styleOptionButtons(appointmentPane);

            appointmentDialog.setVisible(true);

            Object appointmentSelection =
                    appointmentPane.getValue();

            if (!"Proceed".equals(appointmentSelection)) {
                return;
            }

            try {
                if (date.getText().trim().equals("DD-MM-YYYY")) {
                    throw new IllegalArgumentException(
                            "Please enter an appointment date."
                    );
                }

                LocalDate appointmentDate =
                        LocalDate.parse(
                                date.getText().trim(),
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy"
                                )
                        );

                LocalTime appointmentTime =
                        LocalTime.parse(
                                ((String) time.getSelectedItem()).trim()
                        );

                service.bookAppointment(
                        pet,
                        profile,
                        appointmentDate,
                        appointmentTime
                );

                java.net.URL appointmentImageURL =
        getClass().getResource(
                "/petadoptionsystem/images/appointmentset.png"
        );

        ImageIcon appointmentIcon = null;

        if (appointmentImageURL != null) {
            Image originalImage =
                    new ImageIcon(appointmentImageURL).getImage();

            Image scaledImage =
                    originalImage.getScaledInstance(
                            120,
                            120,
                            Image.SCALE_SMOOTH
                    );

            appointmentIcon = new ImageIcon(scaledImage);
        }

        Object[] successOptions = {
                "OK"
        };

        JOptionPane successPane = new JOptionPane(
                "Appointment set successfully.",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                appointmentIcon,
                successOptions,
                successOptions[0]
        );

        JDialog successDialog =
                successPane.createDialog(
                        this,
                        "Message"
                );

        styleOptionButtons(successPane);

        successDialog.setVisible(true);

                refreshPets();

            } catch (Exception ex) {
                java.net.URL invalidImageURL =
                        getClass().getResource(
                                "/petadoptionsystem/images/invalidappointment.png"
                        );

                ImageIcon invalidAppointmentIcon = null;

                if (invalidImageURL != null) {
                    Image originalImage =
                            new ImageIcon(invalidImageURL).getImage();

                    Image scaledImage =
                            originalImage.getScaledInstance(
                                    120,
                                    120,
                                    Image.SCALE_SMOOTH
                            );

                    invalidAppointmentIcon =
                            new ImageIcon(scaledImage);
                }

                Object[] invalidOptions = {
                        "OK"
                };

                JOptionPane invalidPane = new JOptionPane(
                        ex.getMessage(),
                        JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.DEFAULT_OPTION,
                        invalidAppointmentIcon,
                        invalidOptions,
                        invalidOptions[0]
                );

                JDialog invalidDialog =
                        invalidPane.createDialog(
                                this,
                                "Invalid Appointment"
                        );

                styleOptionButtons(invalidPane);
                invalidDialog.setVisible(true);
            }
        }

    private void addPlaceholder(JTextField field, String placeholder) {
        Color placeholderColor = new Color(145, 145, 145);
        Color inputColor = UIManager.getColor("TextField.foreground");

        field.setText(placeholder);
        field.setForeground(placeholderColor);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(inputColor);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(placeholderColor);
                }
            }
        });
    }

    private AdopterProfile profileDialog() {
        AdopterProfile existingProfile = service.getProfile();
        ImageIcon adopterIcon = loadScaledIcon(
                "/petadoptionsystem/images/adoptinfo.png",
                120
        );

        if (existingProfile != null) {
            JTextArea savedInformation = new JTextArea(
                    "Saved adopter information:\n\n"
                    + "Name: " + existingProfile.getFullName() + "\n"
                    + "Gender: " + existingProfile.getGender() + "\n"
                    + "Address: " + existingProfile.getAddress() + "\n"
                    + "Age: " + existingProfile.getAge() + "\n"
                    + "Phone number: "
                    + existingProfile.getPhoneNumber() + "\n"
                    + "Living situation: "
                    + existingProfile.getLivingSituation()
            );

            savedInformation.setEditable(false);
            savedInformation.setOpaque(false);
            savedInformation.setFont(
                    new Font("SansSerif", Font.PLAIN, 16)
            );

            Object[] savedOptions = {
                    "Clear",
                    "Edit",
                    "Cancel"
            };

            JOptionPane savedPane = new JOptionPane(
                    savedInformation,
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.DEFAULT_OPTION,
                    adopterIcon,
                    savedOptions,
                    savedOptions[1]
            );

            JDialog savedDialog = savedPane.createDialog(
                    this,
                    "Adopter Information"
            );

            styleOptionButtons(savedPane);
            fitDialogToButtons(
                    savedDialog,
                    savedOptions.length
            );
            savedDialog.setVisible(true);

            Object savedSelection = savedPane.getValue();

            if ("Clear".equals(savedSelection)) {
                service.setProfile(null);
                return null;
            }

            if (!"Edit".equals(savedSelection)) {
                return null;
            }
        }

        JTextField name = new JTextField(
                existingProfile == null
                        ? ""
                        : existingProfile.getFullName()
        );
        JTextField gender = new JTextField(
                existingProfile == null
                        ? ""
                        : existingProfile.getGender()
        );
        JTextField address = new JTextField(
                existingProfile == null
                        ? ""
                        : existingProfile.getAddress()
        );
        JTextField age = new JTextField(
                existingProfile == null
                        ? ""
                        : String.valueOf(existingProfile.getAge())
        );
        JTextField phone = new JTextField(
                existingProfile == null
                        ? ""
                        : existingProfile.getPhoneNumber()
        );
        JTextField living = new JTextField(
                existingProfile == null
                        ? ""
                        : existingProfile.getLivingSituation()
        );

        JPanel form = new JPanel(
                new GridLayout(6, 2, 8, 8)
        );

        String[] labels = {
                "Full name:",
                "Gender:",
                "Address:",
                "Age:",
                "Phone number:",
                "Living situation:"
        };

        JTextField[] fields = {
                name,
                gender,
                address,
                age,
                phone,
                living
        };

        for (int i = 0; i < labels.length; i++) {
            form.add(new JLabel(labels[i]));
            form.add(fields[i]);
        }

        Object[] formOptions = {
                "Cancel",
                "OK"
        };

        JOptionPane formPane = new JOptionPane(
                form,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                adopterIcon,
                formOptions,
                formOptions[1]
        );

        JDialog formDialog = formPane.createDialog(
                this,
                "Adopter Information"
        );

        styleOptionButtons(formPane);
        formDialog.setVisible(true);

        if (!"OK".equals(formPane.getValue())) {
            return null;
        }

        try {
            AdopterProfile profile = new AdopterProfile(
                    name.getText(),
                    gender.getText(),
                    address.getText(),
                    Integer.parseInt(age.getText()),
                    living.getText(),
                    phone.getText().trim()
            );

            if (profile.getAge() < 18) {
                showUnderageDialog();
                return null;
            }

            service.setProfile(profile);
            showProfileSavedDialog();
            return profile;

        } catch (Exception ex) {
            showInvalidInformationDialog();
            return null;
        }
    }

    private ImageIcon loadScaledIcon(String resourcePath, int imageSize) {
        java.net.URL imageURL = getClass().getResource(resourcePath);

        if (imageURL == null) {
            return null;
        }

        Image image = new ImageIcon(imageURL).getImage();
        Image scaledImage = image.getScaledInstance(
                imageSize,
                imageSize,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(scaledImage);
    }

    private void showProfileSavedDialog() {
        ImageIcon successIcon = loadScaledIcon(
                "/petadoptionsystem/images/appointmentset.png",
                120
        );

        Object[] options = {
                "OK"
        };

        JOptionPane successPane = new JOptionPane(
                "Information filled in successfully",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                successIcon,
                options,
                options[0]
        );

        JDialog successDialog = successPane.createDialog(
                this,
                "Message"
        );

        styleOptionButtons(successPane);
        successDialog.setVisible(true);
    }

    private void showUnderageDialog() {
        ImageIcon invalidIcon = loadScaledIcon(
                "/petadoptionsystem/images/invalidappointment.png",
                120
        );

        Object[] options = {
                "OK"
        };

        JOptionPane underagePane = new JOptionPane(
                "You must be at least 18 years old to adopt.",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                invalidIcon,
                options,
                options[0]
        );

        JDialog underageDialog = underagePane.createDialog(
                this,
                "Invalid Appointment"
        );

        styleOptionButtons(underagePane);
        underageDialog.setVisible(true);
    }

    private void showInvalidInformationDialog() {
        ImageIcon invalidIcon = loadScaledIcon(
                "/petadoptionsystem/images/invalidappointment.png",
                120
        );

        Object[] options = {
                "OK"
        };

        JOptionPane invalidPane = new JOptionPane(
                "Invalid information entered.",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                invalidIcon,
                options,
                options[0]
        );

        JDialog invalidDialog = invalidPane.createDialog(
                this,
                "Invalid Information"
        );

        styleOptionButtons(invalidPane);
        invalidDialog.setVisible(true);
    }

    private void appointmentsDialog() {
        List<Appointment> list =
                service.getAppointments();

        if (list.isEmpty()) {
            showNoAppointmentsDialog();
            return;
        }

        String[] choices =
                new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            choices[i] =
                    (i + 1) + ". " + list.get(i).summary();
        }

        JComboBox<String> appointmentDropdown =
                new JComboBox<>(choices);

        appointmentDropdown.setPreferredSize(
                new Dimension(360, 35)
        );
        appointmentDropdown.setBackground(Color.WHITE);
        appointmentDropdown.setForeground(Color.BLACK);
        appointmentDropdown.setOpaque(true);
        appointmentDropdown.setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 210, 210),
                        1
                )
        );

        appointmentDropdown.setUI(
                new javax.swing.plaf.basic.BasicComboBoxUI() {
                    @Override
                    protected JButton createArrowButton() {
                        Color buttonColor =
                                new Color(138, 96, 56);

                        return new javax.swing.plaf.basic.BasicArrowButton(
                                SwingConstants.SOUTH,
                                buttonColor,
                                buttonColor,
                                Color.WHITE,
                                buttonColor
                        );
                    }
                }
        );

        appointmentDropdown.setRenderer(
                new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus
                    ) {
                        JLabel label =
                                (JLabel) super.getListCellRendererComponent(
                                        list,
                                        value,
                                        index,
                                        false,
                                        false
                                );

                        label.setBackground(Color.WHITE);
                        label.setForeground(Color.BLACK);
                        return label;
                    }
                }
        );

        Object[] selectionOptions = {
                "Cancel",
                "OK"
        };

        JOptionPane selectionPane = new JOptionPane(
                appointmentDropdown,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                null,
                selectionOptions,
                selectionOptions[1]
        );

        JDialog selectionDialog = selectionPane.createDialog(
                this,
                "Appointments"
        );

        styleOptionButtons(selectionPane);
        fitDialogToButtons(
                selectionDialog,
                selectionOptions.length
        );
        selectionDialog.setVisible(true);

        if (!"OK".equals(selectionPane.getValue())) {
            return;
        }

        String selected =
                (String) appointmentDropdown.getSelectedItem();

        int index =
                Integer.parseInt(
                        selected.substring(
                                0,
                                selected.indexOf('.')
                        )
                ) - 1;

        Appointment appointment =
                list.get(index);

        ImageIcon petIcon = loadScaledIcon(
                "/petadoptionsystem/images/"
                        + appointment.getPet().getName().toLowerCase(Locale.ROOT)
                        + ".png",
                120
        );

        JTextArea details = new JTextArea(
                appointment.details()
        );
        details.setEditable(false);
        details.setOpaque(false);
        details.setFont(
                new Font("SansSerif", Font.PLAIN, 16)
        );

        Object[] options;

        if (appointment.getStatus() == Appointment.Status.SCHEDULED) {
            options = new Object[]{
                    "Mark completed",
                    "Cancel appointment",
                    "Close"
            };
        } else {
            options = new Object[]{
                    "Close",
                    "Delete"
            };
        }

        JOptionPane detailsPane = new JOptionPane(
                details,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                petIcon,
                options,
                options[options.length - 1]
        );

        JDialog detailsDialog = detailsPane.createDialog(
                this,
                "Appointment Details"
        );

        styleOptionButtons(detailsPane);
        fitDialogToButtons(
                detailsDialog,
                options.length
        );
        detailsDialog.setVisible(true);

        Object selectedAction = detailsPane.getValue();

        if ("Delete".equals(selectedAction)) {
            service.deleteAppointment(appointment.getId());
            refreshPets();
        } else if ("Cancel appointment".equals(selectedAction)) {
            service.cancelAppointment(appointment.getId());
            refreshPets();
        } else if ("Mark completed".equals(selectedAction)) {
            service.markCompleted(appointment.getId());
        }
    }

    private void showNoAppointmentsDialog() {
        ImageIcon noAppointmentsIcon = loadScaledIcon(
                "/petadoptionsystem/images/noappsfound.png",
                120
        );

        Object[] options = {
                "OK"
        };

        JOptionPane noAppointmentsPane = new JOptionPane(
                "No appointments found.",
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                noAppointmentsIcon,
                options,
                options[0]
        );

        JDialog noAppointmentsDialog =
                noAppointmentsPane.createDialog(
                        this,
                        "Appointments"
                );

        styleOptionButtons(noAppointmentsPane);
        fitDialogToButtons(
                noAppointmentsDialog,
                options.length
        );
        noAppointmentsDialog.setVisible(true);
    }
}