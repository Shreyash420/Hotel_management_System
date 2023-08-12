import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.awt.print.PrinterException;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;

public class HotelManagementApp extends JFrame {
    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtArrivalDate;
    private JTextField txtArrivalTime;
    private JTextField txtDepartureDate;
    private JTextField txtDepartureTime;
    private JTextField txtAadharNumber;
    private JButton btnAddCustomer;
    private JButton btnShowDetails;
    private JButton btnRemoveCustomer;
    private JButton btnGenerateBill;
    private JButton btnPrintBill;
    private JButton btnReset;


    private JComboBox<String> roomTypeComboBox; // Added
    private ArrayList<CustomerDetails> customerDetailsList;
    private final String hotelName = "Hotel Takshashila";
    private static final double NORMAL_ROOM_RATE = 800;
    private static final double DELUXE_ROOM_RATE = 1500;

    public HotelManagementApp() {
        initializeGUI();
        customerDetailsList = new ArrayList<>();
    }
    
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }
    
    private void initializeGUI() {
        setTitle("Hotel Management Application");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel customerDetailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel hotelNameLabel = new JLabel(hotelName);
        hotelNameLabel.setForeground(Color.BLUE);
        hotelNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        customerDetailsPanel.add(hotelNameLabel, gbc);
        gbc.gridy++;

        JLabel nameLabel = new JLabel("Name:");
        customerDetailsPanel.add(nameLabel, gbc);
        gbc.gridx++;
        txtName = new JTextField(20);
        txtAddress = new JTextField(20);

        txtName.setPreferredSize(new Dimension(txtAddress.getPreferredSize().width, txtName.getPreferredSize().height)); // Set same width as Address
        txtAddress.setPreferredSize(new Dimension(200, txtAddress.getPreferredSize().height));
        txtArrivalDate = new JTextField(10);
        txtArrivalDate.setToolTipText("Format: DD/MM/YYYY");
        txtArrivalDate.setPreferredSize(new Dimension(200, txtArrivalDate.getPreferredSize().height)); // Set fixed width
        txtArrivalDate.setText(getCurrentDate()); // Set initial value to the current date

        txtArrivalTime = new JTextField(10);
        txtArrivalTime.setToolTipText("Format: HH:mm");
        txtArrivalTime.setText(getCurrentTime()); // Set arrival time to the current time
        txtArrivalTime.setPreferredSize(new Dimension(200, txtArrivalTime.getPreferredSize().height)); // Set fixed width
       
        try {
    MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
    dateFormatter.setPlaceholderCharacter(' ');
    txtDepartureDate = new JFormattedTextField(dateFormatter);
} catch (ParseException e) {
    e.printStackTrace(); // Handle the exception appropriately
}
txtDepartureDate.setToolTipText("Format: DD/MM/YYYY");
txtDepartureDate.setPreferredSize(new Dimension(200, txtDepartureDate.getPreferredSize().height));

txtDepartureDate.setInputVerifier(new InputVerifier() {
    @Override
    public boolean verify(JComponent input) {
        String inputText = ((JFormattedTextField) input).getText();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false); // Disallow lenient parsing

        try {
            Date parsedDate = dateFormat.parse(inputText);
            if (!inputText.equals(dateFormat.format(parsedDate))) {
                throw new ParseException("Date does not match the format.", 0);
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
});

       
try {
    MaskFormatter timeFormatter = new MaskFormatter("##:##");
    timeFormatter.setPlaceholderCharacter(' ');
    txtDepartureTime = new JFormattedTextField(timeFormatter);
} catch (ParseException e) {
    e.printStackTrace(); // Handle the exception appropriately
}
txtDepartureTime.setToolTipText("Format: HH:mm");
txtDepartureTime.setPreferredSize(new Dimension(200, txtDepartureTime.getPreferredSize().height));

txtDepartureTime.setInputVerifier(new InputVerifier() {
    @Override
    public boolean verify(JComponent input) {
        String inputText = ((JFormattedTextField) input).getText();

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setLenient(false); // Disallow lenient parsing

        try {
            Date parsedTime = timeFormat.parse(inputText);
            if (!inputText.equals(timeFormat.format(parsedTime))) {
                throw new ParseException("Time does not match the format.", 0);
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
});

       
        // Create and add the Aadhaar card number field with formatting
    txtAadharNumber = new JFormattedTextField(createAadharFormatter());
    txtAadharNumber.setPreferredSize(new Dimension(200, txtAadharNumber.getPreferredSize().height));


        customerDetailsPanel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel addressLabel = new JLabel("Address:");
        customerDetailsPanel.add(addressLabel, gbc);
        gbc.gridx++;
        customerDetailsPanel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        customerDetailsPanel.add(new JLabel("Arrival Date:"), gbc);
        gbc.gridx++;
        customerDetailsPanel.add(txtArrivalDate, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        customerDetailsPanel.add(new JLabel("Arrival Time:"), gbc);
        gbc.gridx++;
        customerDetailsPanel.add(txtArrivalTime, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        customerDetailsPanel.add(new JLabel("Departure Date:"), gbc);
        gbc.gridx++;
        customerDetailsPanel.add(txtDepartureDate, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        customerDetailsPanel.add(new JLabel("Departure Time:"), gbc);
        gbc.gridx++;
        customerDetailsPanel.add(txtDepartureTime, gbc);

        String[] roomTypes = {"Normal Room", "Deluxe Room"};
        roomTypeComboBox = new JComboBox<>(roomTypes);
        gbc.gridx = 0;
        gbc.gridy++;
        customerDetailsPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx++;
        customerDetailsPanel.add(roomTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        customerDetailsPanel.add(new JLabel("Aadhar Card Number:"), gbc);
        gbc.gridx++;
        customerDetailsPanel.add(txtAadharNumber, gbc);

            // Add Aadhaar card number input verifier
    txtAadharNumber.setInputVerifier(new InputVerifier() {
        @Override
        public boolean verify(JComponent input) {
            String inputText = txtAadharNumber.getText().replaceAll("\\s+", ""); // Remove spaces
            if (inputText.length() != 12 || !inputText.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Aadhaar card number must be 12 digits and consist only of digits.");
                return false;
            }
            return true;
        }
    });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        btnAddCustomer = new JButton("Add Customer");
        btnShowDetails = new JButton("Show Customer Details");
        btnRemoveCustomer = new JButton("Remove Customer");
        btnGenerateBill = new JButton("Generate Bill");
        btnPrintBill = new JButton("Print Bill");
        btnReset = new JButton("Reset");

        btnAddCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomerDetails();
            }
        });

        btnShowDetails.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomerDetails();
            }
        });

        btnRemoveCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeCustomerDetails();
            }
        });

        btnGenerateBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateBill();
            }
        });

        btnPrintBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printBill();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGUI();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        txtName.requestFocusInWindow();
                    }
                });
            }
        });
        
        
        
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(btnAddCustomer);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(btnShowDetails);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(btnRemoveCustomer);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(btnGenerateBill);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(btnPrintBill);
        buttonsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        buttonsPanel.add(btnReset);
        buttonsPanel.add(Box.createHorizontalGlue());

        add(customerDetailsPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private MaskFormatter createAadharFormatter() {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter("#### #### ####");
            formatter.setPlaceholderCharacter(' ');
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter;
    }

    private void addCustomerDetails() {
        String name = txtName.getText();
        String address = txtAddress.getText();
        String arrivalDate = txtArrivalDate.getText();
        String arrivalTime = txtArrivalTime.getText();
        String departureDate = txtDepartureDate.getText();
        String departureTime = txtDepartureTime.getText();
        String aadharNumber = txtAadharNumber.getText();
    
        if (name.isEmpty() || address.isEmpty() || arrivalDate.isEmpty() || arrivalTime.isEmpty() ||
                departureDate.isEmpty() || departureTime.isEmpty() || aadharNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all customer details.");
            return;
        }
    
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    
        try {
            Date parsedArrivalDate = dateFormat.parse(arrivalDate);
            Date parsedDepartureDate = dateFormat.parse(departureDate);
            Date parsedArrivalTime = timeFormat.parse(arrivalTime);
            Date parsedDepartureTime = timeFormat.parse(departureTime);
    
            if (parsedDepartureDate.before(parsedArrivalDate)) {
                JOptionPane.showMessageDialog(this, "Departure date cannot be before arrival date.");
                return;
            }
    
            if (dateFormat.format(parsedArrivalDate).equals(dateFormat.format(parsedDepartureDate))) {
                long timeDifferenceMillis = parsedDepartureTime.getTime() - parsedArrivalTime.getTime();
                long minimumTimeDifferenceMillis = 2 * 60 * 60 * 1000; // 2 hours in milliseconds
                if (timeDifferenceMillis < minimumTimeDifferenceMillis) {
                    JOptionPane.showMessageDialog(this, "Departure time must be at least 2 hours after arrival time if arrival and departure dates are the same.");
                    return;
                }
            }
    
            // Validation successful, proceed to add customer details
            // ... (rest of the code to add customer details)
    
            // Clear the text fields after adding the details

            // Show a message to indicate that the customer was added successfully
           JOptionPane.showMessageDialog(this, "Customer added successfully.");

            CustomerDetails customer = new CustomerDetails(name, address, arrivalDate, arrivalTime, departureDate, departureTime, aadharNumber);
            customerDetailsList.add(customer); // Add the customer to the list

            txtName.setText("");
            txtAddress.setText("");
            txtArrivalDate.setText(getCurrentDate());
            txtArrivalTime.setText(getCurrentTime());
            txtDepartureDate.setText("");
            txtDepartureTime.setText("");
            txtAadharNumber.setText("");
    
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date or time format.");
        }
    }
        
    private void showCustomerDetails() {
        if (customerDetailsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customer details found.");
            return;
        }
    
        // Create a new dialog box for displaying customer details
        JDialog dialog = new JDialog(this, "Customer Details", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());
    
        // Create a search box for filtering customer details
        JTextField searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(150, 25));
        JButton searchButton = new JButton("Search");
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);
        dialog.add(searchPanel, BorderLayout.NORTH);
    
        // Create the table and table model for the dialog box
        DefaultTableModel dialogTableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        dialogTableModel.addColumn("Room Number");
        dialogTableModel.addColumn("Name");
        dialogTableModel.addColumn("Address");
        dialogTableModel.addColumn("Arrival Date");
        dialogTableModel.addColumn("Arrival Time");
        dialogTableModel.addColumn("Departure Date");
        dialogTableModel.addColumn("Departure Time");
        dialogTableModel.addColumn("Aadhar Card Number");
    
        JTable dialogCustomerTable = new JTable(dialogTableModel);
    
        // Center-align content in all columns except for Name and Address
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < dialogCustomerTable.getColumnCount(); i++) {
            String columnName = dialogCustomerTable.getColumnName(i);
            if (!columnName.equals("Name") && !columnName.equals("Address")) {
                dialogCustomerTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
    
        // Add customer details to the table in the dialog box
        for (int i = 0; i < customerDetailsList.size(); i++) {
            CustomerDetails customer = customerDetailsList.get(i);
            Vector<Object> rowData = new Vector<>();
            rowData.add("Room " + (i + 1));
            rowData.add(customer.getName());
            rowData.add(customer.getAddress());
            rowData.add(customer.getArrivalDate());
            rowData.add(customer.getArrivalTime());
            rowData.add(customer.getDepartureDate());
            rowData.add(customer.getDepartureTime());
            rowData.add(customer.getAadharNumber());
            dialogTableModel.addRow(rowData);
        }
    
        // Create the scroll pane for the table in the dialog box
        JScrollPane dialogTableScrollPane = new JScrollPane(dialogCustomerTable);
        dialogTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        dialogTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
        // Add the table scroll pane to the dialog box
        dialog.add(dialogTableScrollPane, BorderLayout.CENTER);
    
        // Set dialog box properties
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.pack();
    
// Set up search functionality
searchButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String searchText = searchTextField.getText();
        if (searchText.isEmpty()) {
            // Reset the table to show all customer details if search text is empty
            dialogTableModel.setRowCount(0);
            for (int i = 0; i < customerDetailsList.size(); i++) {
                CustomerDetails customer = customerDetailsList.get(i);
                Vector<Object> rowData = new Vector<>();
                rowData.add("Room " + (i + 1));
                rowData.add(customer.getName());
                rowData.add(customer.getAddress());
                rowData.add(customer.getArrivalDate());
                rowData.add(customer.getArrivalTime());
                rowData.add(customer.getDepartureDate());
                rowData.add(customer.getDepartureTime());
                rowData.add(customer.getAadharNumber());
                dialogTableModel.addRow(rowData);
            }
        } else {
            // Filter customer details based on search text
            dialogTableModel.setRowCount(0);
            for (int i = 0; i < customerDetailsList.size(); i++) {
                CustomerDetails customer = customerDetailsList.get(i);
                Vector<Object> rowData = new Vector<>();
                rowData.add("Room " + (i + 1));
                rowData.add(customer.getName());
                rowData.add(customer.getAddress());
                rowData.add(customer.getArrivalDate());
                rowData.add(customer.getArrivalTime());
                rowData.add(customer.getDepartureDate());
                rowData.add(customer.getDepartureTime());
                rowData.add(customer.getAadharNumber());
                dialogTableModel.addRow(rowData);

                if (customer.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                    ("Room " + (i + 1)).toLowerCase().contains(searchText.toLowerCase())) {
                    // Highlight the row if search text matches
                    dialogCustomerTable.setRowSelectionInterval(i, i);
                }
            }
        }
    }
});

  // Set the width of the dialog box to a percentage of the screen width
  int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
  int dialogWidthPercentage = 80; // Adjust the percentage as needed
  int dialogWidth = (int) (screenWidth * (dialogWidthPercentage / 100.0));
  dialog.setPreferredSize(new Dimension(dialogWidth, dialog.getPreferredSize().height));

 // Set the size of the dialog box to a percentage of the screen height
    int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
    int dialogHeight = (int) (screenHeight * 0.8); // Adjust the percentage as needed
    dialog.setPreferredSize(new Dimension(dialog.getPreferredSize().width, dialogHeight));

    dialog.pack();

    // Ensure the dialog box is within the visible screen area
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int maxDialogY = screenSize.height - dialog.getHeight();
    int dialogY = dialog.getY();
    if (dialogY > maxDialogY) {
        dialog.setLocation(dialog.getX(), maxDialogY);
    } else {
        dialog.setLocationRelativeTo(this);
    }

    dialog.setVisible(true);
}     

private void removeCustomerDetails() {
    if (customerDetailsList.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No customer details found.");
        return;
    }

    String input = JOptionPane.showInputDialog(this, "Enter Room Number or Customer Name to remove:");
    if (input == null || input.trim().isEmpty()) {
        return; // User canceled or entered empty input
    }

    boolean isInputNumber = input.matches("\\d+");

    if (isInputNumber) {
        // Input is a number, consider it as room number
        int roomNumber = Integer.parseInt(input);
        if (roomNumber >= 1 && roomNumber <= customerDetailsList.size()) {
            customerDetailsList.remove(roomNumber - 1);
            JOptionPane.showMessageDialog(this, "Customer removed successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid room number.");
        }
    } else {
        // Input is not a number, consider it as customer name
        boolean customerFound = false;
        for (int i = 0; i < customerDetailsList.size(); i++) {
            CustomerDetails customer = customerDetailsList.get(i);
            if (customer.getName().equalsIgnoreCase(input)) {
                customerDetailsList.remove(i);
                JOptionPane.showMessageDialog(this, "Customer removed successfully.");
                customerFound = true;
                break;
            }
        }
        if (!customerFound) {
            JOptionPane.showMessageDialog(this, "Customer with the given name not found.");
        }
    }
}


    private int calculateNumberOfNights(String arrivalDate, String departureDate) {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    try {
        Date arrival = sdf.parse(arrivalDate);
        Date departure = sdf.parse(departureDate);
        long difference = departure.getTime() - arrival.getTime();
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        return days;
    } catch (ParseException e) {
        e.printStackTrace();
        return 0;
    }
}


private void generateBill() {
    if (customerDetailsList.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No customer details found.");
        return;
    }

    String input = JOptionPane.showInputDialog(this, "Enter Room Number or Name to generate bill:");
    if (input == null || input.trim().isEmpty()) {
        return; // User canceled or entered empty input
    }

    CustomerDetails selectedCustomer = null;
    if (input.matches("\\d+")) {
        int roomNumber = Integer.parseInt(input);
        if (roomNumber >= 1 && roomNumber <= customerDetailsList.size()) {
            selectedCustomer = customerDetailsList.get(roomNumber - 1);
        }
    } else {
        for (CustomerDetails customer : customerDetailsList) {
            if (customer.getName().equalsIgnoreCase(input)) {
                selectedCustomer = customer;
                break;
            }
        }
    }

    if (selectedCustomer != null) {
        int numberOfNights = calculateNumberOfNights(selectedCustomer.getArrivalDate(), selectedCustomer.getDepartureDate());
        double roomRate = (roomTypeComboBox.getSelectedItem().toString().equals("Normal Room")) ? NORMAL_ROOM_RATE : DELUXE_ROOM_RATE;
        double totalAmount = numberOfNights * roomRate;

        String billContent = "------------------------------------------------------------\n" +
                "                     HOTEL LODGING BILL                     \n" +
                "------------------------------------------------------------\n" +
                "Hotel Name       : " + hotelName + "\n" +
                "Address          : Siddhivinayak road, Pune, India\n" +
                "Phone            : 07256-240444\n" +
                "Email            : info@takshashilahotel.com\n" +
                "Website          : www.hoteltakshashila.com\n\n" +
                "Guest Name       : " + selectedCustomer.getName() + "\n" +
                "Address          : " + selectedCustomer.getAddress() + "\n" +
                "Aadhar Card      : " + selectedCustomer.getAadharNumber() + "\n" +
                "Check-in Date    : " + selectedCustomer.getArrivalDate() + "\n" +
                "Check-out Date   : " + selectedCustomer.getDepartureDate() + "\n" +
                "Room Type        : " + roomTypeComboBox.getSelectedItem().toString() + "\n" +
                "No. of Nights    : " + numberOfNights + "\n\n" +
                "------------------------------------------------------------\n" +
                "Total Amount     : Rs. " + totalAmount + "\n" +
                "------------------------------------------------------------\n" +
                "                        Thank you!\n";

        JTextPane billTextPane = new JTextPane();
        billTextPane.setEditable(false);
        billTextPane.setFont(new Font("Monospaced", Font.PLAIN, 12));
        billTextPane.setText(billContent);

        JScrollPane scrollPane = new JScrollPane(billTextPane);
        scrollPane.setPreferredSize(new Dimension(600, 800));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JOptionPane optionPane = new JOptionPane(scrollPane, JOptionPane.PLAIN_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "Lodging Bill");

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
        int taskbarHeight = insets.bottom;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int availableHeight = screenSize.height - taskbarHeight;

        int maxDialogHeight = (int) (availableHeight * 0.8);
        int dialogHeight = Math.min(maxDialogHeight, scrollPane.getPreferredSize().height);
        dialog.setSize(600, dialogHeight);

        int dialogX = (screenSize.width - dialog.getWidth()) / 2;
        int dialogY = (screenSize.height - dialog.getHeight()) / 2;
        dialog.setLocation(dialogX, dialogY);

        dialog.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Customer with the given room number or name not found.");
    }
}


    
    private void printBillContent(String billContent) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
    
        Printable printable = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                if (pageIndex >= 1) {
                    return NO_SUCH_PAGE;
                }
    
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));
                String[] lines = billContent.split("\n");
                int y = 0;
                for (String line : lines) {
                    g2d.drawString(line, 0, y);
                    y += 14; // Adjust line height
                }
    
                return PAGE_EXISTS;
            }
        };
    
        job.setPrintable(printable, pf);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while printing.");
            }
        }
    }


    private void printBill() {
        if (customerDetailsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customer details found.");
            return;
        }
    
        String input = JOptionPane.showInputDialog(this, "Enter Room Number or Name to print bill:");
        if (input == null || input.trim().isEmpty()) {
            return; // User canceled or entered empty input
        }
    
        CustomerDetails selectedCustomer = null;
        if (input.matches("\\d+")) {
            int roomNumber = Integer.parseInt(input);
            if (roomNumber >= 1 && roomNumber <= customerDetailsList.size()) {
                selectedCustomer = customerDetailsList.get(roomNumber - 1);
            }
        } else {
            for (CustomerDetails customer : customerDetailsList) {
                if (customer.getName().equalsIgnoreCase(input)) {
                    selectedCustomer = customer;
                    break;
                }
            }
        }
    
        if (selectedCustomer != null) {
            int numberOfNights = calculateNumberOfNights(selectedCustomer.getArrivalDate(), selectedCustomer.getDepartureDate());
            double roomRate = (roomTypeComboBox.getSelectedItem().toString().equals("Normal Room")) ? NORMAL_ROOM_RATE : DELUXE_ROOM_RATE;
            double totalAmount = numberOfNights * roomRate;
    
            String billContent = "------------------------------------------------------------\n" +
                    "                     HOTEL LODGING BILL                     \n" +
                    "------------------------------------------------------------\n" +
                    "Hotel Name       : " + hotelName + "\n" +
                    "Address          : Siddhivinayak road, Pune, India\n" +
                    "Phone            : 07256-240444\n" +
                    "Email            : info@takshashilahotel.com\n" +
                    "Website          : www.hoteltakshashila.com\n\n" +
                    "Guest Name       : " + selectedCustomer.getName() + "\n" +
                    "Address          : " + selectedCustomer.getAddress() + "\n" +
                    "Aadhar Card      : " + selectedCustomer.getAadharNumber() + "\n" +
                    "Check-in Date    : " + selectedCustomer.getArrivalDate() + "\n" +
                    "Check-out Date   : " + selectedCustomer.getDepartureDate() + "\n" +
                    "Room Type        : " + roomTypeComboBox.getSelectedItem().toString() + "\n" +
                    "No. of Nights    : " + numberOfNights + "\n\n" +
                    "------------------------------------------------------------\n" +
                    "Total Amount     : Rs. " + totalAmount + "\n" +
                    "------------------------------------------------------------\n" +
                    "                        Thank you!\n";
    
            JTextArea billTextArea = new JTextArea(billContent);
            billTextArea.setEditable(false);
            billTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set monospaced font
    
            JScrollPane scrollPane = new JScrollPane(billTextArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
    
            JPanel printPanel = new JPanel(new BorderLayout());
            printPanel.add(scrollPane, BorderLayout.CENTER);
    
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Create a panel to center the button
            JButton printButton = new JButton("Print");
            printButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    printBillContent(billContent);
                }
            });
    
            printButton.setPreferredSize(new Dimension(80, 25)); // Adjust width and height as needed
            printButton.setFont(printButton.getFont().deriveFont(12.0f)); // Adjust the font size
    
            buttonPanel.add(printButton);
            printPanel.add(scrollPane, BorderLayout.CENTER);
            printPanel.add(buttonPanel, BorderLayout.SOUTH);
    
            JDialog dialog = new JDialog(this, "Print Bill", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.add(printPanel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
    
        } else {
            JOptionPane.showMessageDialog(this, "Customer with the given room number or name not found.");
        }
    }
    
    

    private void resetGUI() {
     // Clear all text fields
     txtName.setText("");
     txtAddress.setText("");
     txtArrivalDate.setText(getCurrentDate());
     txtArrivalTime.setText(getCurrentTime());
     txtDepartureDate.setText("");
     txtDepartureTime.setText("");
     txtAadharNumber.setText("");
 
     // Set the focus to the name field
     txtName.requestFocusInWindow();
    }
    
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date());
    }

    private class CustomerDetails {
        private String name;
        private String address;
        private String arrivalDate;
        private String arrivalTime;
        private String departureDate;
        private String departureTime;
        private String aadharNumber;
    
        public CustomerDetails(String name, String address, String arrivalDate, String arrivalTime,
                               String departureDate, String departureTime, String aadharNumber) {
            this.name = name;
            this.address = address;
            this.arrivalDate = arrivalDate;
            this.arrivalTime = arrivalTime;
            this.departureDate = departureDate;
            this.departureTime = departureTime;
            this.aadharNumber = aadharNumber;
        }
    
        public String getName() {
            return name;
        }
    
        public String getAddress() {
            return address;
        }
    
        public String getArrivalDate() {
            return arrivalDate;
        }
    
        public String getArrivalTime() {
            return arrivalTime;
        }
    
        public String getDepartureDate() {
            return departureDate;
        }
    
        public String getDepartureTime() {
            return departureTime;
        }
    
        public String getAadharNumber() {
            return aadharNumber;
        }
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HotelManagementApp();
            }
        });
    }
}
