import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

class MenuItemInfor {
    String menuList;
    double price;

    MenuItemInfor(String menuList, double price) {
        this.menuList = menuList;
        this.price = price;
    }
}

public class Cafe extends JFrame implements Printable {
    private static final long serialVersionUID = 1L;
    private Map<Integer, MenuItemInfor> menu = new HashMap<>();
    private Map<Integer, Integer> order = new HashMap<>();
    private JTextArea textArea;

    public Cafe() {
        setTitle("Cafe International Restaurant");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        getData();
        createUI();
        showMenu();

        pack();
        setVisible(true);
    }

    private void createUI() {
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        add(scrollPane, constraints);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputConstraints = new GridBagConstraints();

        JButton placeOrderButton = new JButton("Place Order");
        inputConstraints.gridx = 4;
        inputPanel.add(placeOrderButton, inputConstraints);
        

        inputConstraints.gridx = 5;
        JButton generateButton = new JButton("Generate Bill");
        inputPanel.add(generateButton, inputConstraints);

        inputConstraints.gridx = 6;
        JButton printBillButton = new JButton("Print Bill");
        inputPanel.add(printBillButton, inputConstraints);

        inputConstraints.gridx = 7;
        JButton printThermalButton = new JButton("Print Thermal Bill");
        inputPanel.add(printThermalButton, inputConstraints);

        // ... (Other buttons)

        inputConstraints.gridx = 8;
        JButton resetButton = new JButton("Reset");
        inputPanel.add(resetButton, inputConstraints);

        inputConstraints.gridx = 8; // Change the gridx value here
        inputConstraints.insets = new Insets(10, 10, 10, 10); // Add this line for spacing
        placeOrderButton.addActionListener(e -> openPlaceOrderDialog());

        // ... (Other buttons)
        generateButton.addActionListener(e -> generateBill());
        printBillButton.addActionListener(e -> printBill());
        printThermalButton.addActionListener(e -> printThermalBill());
        resetButton.addActionListener(e -> resetGUI());

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(inputPanel, constraints);


    }

    private void getData() {
        menu.put(1, new MenuItemInfor("Plain Egg   ", 15.00));
        menu.put(2, new MenuItemInfor("Omelet      ", 30.00));
        menu.put(3, new MenuItemInfor("Paratha     ", 10.00));
        menu.put(4, new MenuItemInfor("French Toast", 20.00));
        menu.put(5, new MenuItemInfor("Fruit Basket", 60.00));
        menu.put(6, new MenuItemInfor("Coffee      ", 15.00));
        menu.put(7, new MenuItemInfor("Tea         ", 10.00));
    }

    private void showMenu() {
        textArea.setText(""); // Clear the text area before displaying the menu
        textArea.append("-------------------------------------------------\n");
        textArea.append("         CAFE INTERNATIONAL RESTAURANT           \n");
        textArea.append("-------------------------------------------------\n");
        textArea.append("\n     Breakfast Items Offered By Restaurant \n");
        textArea.append("-------------------------------------------------\n");
        textArea.append("                 Date: " + getDateTime() + "\n");
        textArea.append("-------------------------------------------------\n");
        textArea.append("     No.   Items                Rates\n");
        textArea.append("                                           \n");
        for (Map.Entry<Integer, MenuItemInfor> entry : menu.entrySet()) {
            int id = entry.getKey();
            MenuItemInfor item = entry.getValue();
            textArea.append(String.format("     %-5d %-20s RS %.2f%n", id, item.menuList, item.price));
        }
        textArea.append("\n");
    }




    private void openPlaceOrderDialog() {
        JComboBox<String> choiceComboBox = new JComboBox<>();
        JTextField quantityField = new JTextField(10);
    
        for (Map.Entry<Integer, MenuItemInfor> entry : menu.entrySet()) {
            int id = entry.getKey();
            MenuItemInfor item = entry.getValue();
            choiceComboBox.addItem(id + ". " + item.menuList);
        }
    
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new GridLayout(0, 1));
        dialogPanel.add(new JLabel("Choose an item:"));
        dialogPanel.add(choiceComboBox);
        dialogPanel.add(new JLabel("Enter quantity:"));
        dialogPanel.add(quantityField);
    
        int result = JOptionPane.showConfirmDialog(this, dialogPanel, "Place Order",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                int selectedIndex = choiceComboBox.getSelectedIndex();
                int quantity = Integer.parseInt(quantityField.getText());
                if (selectedIndex >= 0 && quantity > 0) {
                    int id = (int) menu.keySet().toArray()[selectedIndex];
                    order.put(id, order.getOrDefault(id, 0) + quantity);
                    textArea.append("You have selected: " + menu.get(id).menuList + "\n");
                } else {
                    textArea.append("Invalid input\n\n");
                }
            } catch (NumberFormatException ex) {
                textArea.append("Invalid input\n\n");
            }
        }
    }
    

    private void generateBill() {
        if (order.isEmpty()) {
            textArea.append("No items in order. Please add items to generate the bill.\n");
            return;
        }
    
        // Get the bill content to be displayed
        StringBuilder billContent = new StringBuilder();
        billContent.append("----------------------------------------------------\n");
        billContent.append("                  CAFE INTERNATIONAL                \n");
        billContent.append("----------------------------------------------------\n");
        billContent.append("                         Date: " + getDateTime() + "\n");
        billContent.append("----------------------------------------------------\n");
        billContent.append(" Qnt.     Items\t\t  Rates\t\tTotal\n\n");
        double total = 0;

for (Map.Entry<Integer, Integer> entry : order.entrySet()) {
    int id = entry.getKey();
    int quantity = entry.getValue();
    MenuItemInfor item = menu.get(id);  // Fix the variable name here
    if (item != null && quantity > 0) {
        billContent.append(String.format(" %-8d %-15s RS %.1f \tRS %.1f%n", quantity, item.menuList, item.price,
                item.price * quantity));
        total += (item.price * quantity);
    }
}


        double tax = total * 0.05;
        double due = total + tax;
        billContent.append("\n----------------------------------------------------\n");
        billContent.append(String.format(" Tax                                    RS %.1f%n", tax));
        billContent.append("----------------------------------------------------\n");
        billContent.append(String.format(" Total Amount                           RS %.1f%n", due));
        billContent.append("----------------------------------------------------\n");
    
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); // Set the font to monospaced
        textArea.setText(billContent.toString());
    }
    

    private void printBill() {
        if (order.isEmpty()) {
            textArea.append("No items in order. Please add items to generate the bill.\n");
            return;
        }
    
        // Get the bill content to be printed
        StringBuilder billContent = new StringBuilder();
        billContent.append("----------------------------------------------------\n");
        billContent.append("                  CAFE INTERNATIONAL                \n");
        billContent.append("----------------------------------------------------\n");
        billContent.append("                         Date: " + getDateTime() + "\n");
        billContent.append("----------------------------------------------------\n");
        billContent.append(" Qnt.     Items\t\t  Rates\t\tTotal\n\n");
        double total = 0;

        for (Map.Entry<Integer, Integer> entry : order.entrySet()) {
            int id = entry.getKey();
            int quantity = entry.getValue();
            MenuItemInfor item = menu.get(id);  // Fix the variable name here
            if (item != null && quantity > 0) {
                billContent.append(String.format(" %-8d %-15s RS %.1f \tRS %.1f%n", quantity, item.menuList, item.price,
                        item.price * quantity));
                total += (item.price * quantity);
            }
        }
        
        double tax = total * 0.05;
        double due = total + tax;
        billContent.append("\n----------------------------------------------------\n");
        billContent.append(String.format(" Tax                                    RS %.1f%n", tax));
        billContent.append("----------------------------------------------------\n");
        billContent.append(String.format(" Total Amount                           RS %.1f%n", due));
        billContent.append("----------------------------------------------------\n");
    
        // Attempt to print the bill using the system default printer
        try {
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); // Set the font to monospaced
            textArea.setText(billContent.toString());
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(this);
            if (job.printDialog()) {
                job.print();
                resetGUIWithDelay(); // Reset the GUI with a delay of 1 second after printing
            }
        } catch (PrinterException ex) {
            // If printing fails, save the bill as a PDF instead
            saveBillAsPDF(billContent.toString());
            resetGUIWithDelay(); // Reset the GUI with a delay of 1 second after saving as PDF
        }
    }
    

    private void printThermalBill() {
        if (order.isEmpty()) {
            textArea.append("No items in order. Please add items to generate the bill.\n");
            return;
        }
    
        // Get the bill content to be printed in 80mm thermal format
        StringBuilder billContent = new StringBuilder();
        billContent.append("----------------------------------------\n");
        billContent.append("         CAFE INTERNATIONAL\n");
        billContent.append("----------------------------------------\n");
        billContent.append("              Date: " + getDateTime() + "\n");
        billContent.append("----------------------------------------\n");
        billContent.append(" Qnt.  Items\t     Rates   Total\n\n");
        double total = 0;

        for (Map.Entry<Integer, Integer> entry : order.entrySet()) {
            int id = entry.getKey();
            int quantity = entry.getValue();
            MenuItemInfor item = menu.get(id);  // Fix the variable name here
            if (item != null && quantity > 0) {
                billContent.append(String.format(" %-5d %-13s RS %.0f   RS %.0f%n", quantity, item.menuList, item.price,
                        item.price * quantity));
                total += (item.price * quantity);
            }
        }
        

        double tax = total * 0.05;
        double due = total + tax;
        billContent.append("\n----------------------------------------\n");
        billContent.append(String.format(" Tax                         RS %.0f%n", tax));
        billContent.append("----------------------------------------\n");
        billContent.append(String.format(" Total                       RS %.0f%n", due));
        billContent.append("----------------------------------------\n");
    
        // Attempt to print the thermal bill using the system default printer
        try {
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); // Set the font to monospaced
            textArea.setText(billContent.toString());
            PrinterJob job = PrinterJob.getPrinterJob();
            Paper paper = new Paper();
            double margin = 5; // 5 mm margin
            paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2 * margin);
            PageFormat pageFormat = new PageFormat();
            pageFormat.setPaper(paper);
            job.setPrintable(this, pageFormat);
            if (job.printDialog()) {
                job.print();
                resetGUIWithDelay(); // Reset the GUI with a delay of 1 second after printing
            }
        } catch (PrinterException ex) {
            // If printing fails, save the bill as a PDF instead
            saveBillAsPDF(billContent.toString());
            resetGUIWithDelay(); // Reset the GUI with a delay of 1 second after saving as PDF
        }
    }
    

    private void saveBillAsPDF(String billContent) {
        String fileName = "Cafe_International_Bill.pdf";
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(fileName))) {
            writer.print(billContent);
            textArea.append("Bill saved as PDF: " + fileName + "\n");
        } catch (FileNotFoundException e) {
            textArea.append("Error saving bill as PDF.\n");
            e.printStackTrace();
        }
    }
    
    private String getDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
    

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
    
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    
        // Printing the content of the textArea directly to the graphics
        textArea.printAll(graphics);
    
        return Printable.PAGE_EXISTS;
    }
    

    private void resetGUIWithDelay() {
        Timer timer = new Timer(1000, e -> resetGUI()); // Delay of 1 second (1000 milliseconds)
        timer.setRepeats(false); // Set to non-repeating
        timer.start(); // Start the timer
    }
    

    private void resetGUI() {
        order.clear(); // Clear the order map to reset the order
        showMenu(); // Show the main menu page again
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Cafe());
    }
}
