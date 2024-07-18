import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.sql.*;


public class Item extends JFrame{

    private JLabel i_name;
    private JTextField it_name;

    private JLabel i_qty;
    private JTextField item_qty;

    private JLabel u_price;
    private JTextField ut_price;

    private float Total_amount;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/Invoice";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Sumit@07";

    public Item(String Company_name , String Company_address , String Company_phone , String Customer_name , String Customer_address , String Customer_phone , String Date , String Invoice){

        JFrame jFrame = new JFrame();
        jFrame.setTitle("Item details");

        JLabel title = new JLabel("Item details");
        title.setBounds(20 , 10 , 100 ,20);

        i_name = new JLabel("Item name");
        i_name.setBounds(20 , 50 , 100 ,20);

        it_name = new JTextField();
        it_name.setBounds(120 ,50 , 100 , 20);

        i_qty = new JLabel("Quantity");
        i_qty.setBounds(20 , 80 , 100 , 20);

        item_qty = new JTextField();
        item_qty.setBounds(120 , 80 , 100 , 20);

        u_price = new JLabel("Unit price");
        u_price.setBounds(20 , 110 , 100 , 20);

        ut_price = new JTextField();
        ut_price.setBounds(120 , 110 , 100 , 20);

        JButton btn = new JButton("Add Item");
        btn.setBounds(120 , 150 ,100 , 30);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDataToDatabase();

                it_name.setText("");
                item_qty.setText("");
                ut_price.setText("");
            }
        });

        JButton generate = new JButton("Generate Invoice");
        generate.setBounds(120 , 200 , 150 , 30);


        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    create_Pdf(Company_name , Company_address , Company_phone , Customer_name , Customer_address , Customer_phone , Date , Invoice);
                } catch (FileNotFoundException ex) {
//                    throw new RuntimeException(ex);
                    System.out.println("Company name = " + Company_name);
                    System.out.println("Error // File Not Found");
                }

                openPdfFile();
            }
        });



        jFrame.add(title);
        jFrame.add(i_name);
        jFrame.add(it_name);
        jFrame.add(i_qty);
        jFrame.add(item_qty);
        jFrame.add(u_price);
        jFrame.add(ut_price);
        jFrame.add(btn);
        jFrame.add(generate);

        jFrame.setLayout(null);
        jFrame.setSize(400 , 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void create_Pdf(String Company_name , String Company_address , String Company_phone , String Customer_name , String Customer_address , String Customer_phone , String Date , String Invoice) throws FileNotFoundException{


        String path = "E:\\Invoice.pdf";
        PdfWriter pdfWriter = new PdfWriter(path);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.addNewPage();

        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A4);

        float col = 280f;
        float columnwidth[] = {col, col};
        Table table = new Table(columnwidth);

        table.setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE);

        table.addCell(new Cell().add("INVOICE")
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setMarginTop(30f)
                .setMarginBottom(30f)
                .setFontSize(30f)
                .setBorder(Border.NO_BORDER));


//        Company details



        table.addCell(new Cell().add( Company_name + "\n" + Company_address + "\n" + Company_phone)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(30f)
                .setMarginBottom(30f)
                .setMarginRight(10f)
                .setBorder(Border.NO_BORDER));

        float colWidth[] = {80, 300, 100, 80};
        Table customerinfotable = new Table(colWidth);

        customerinfotable.addCell(new Cell(0, 4).add("Customer Information").setBold().setBorder(Border.NO_BORDER));

        customerinfotable.addCell(new Cell().add("Name").setBorder(Border.NO_BORDER));
        customerinfotable.addCell(new Cell().add(Customer_name).setBorder(Border.NO_BORDER));
        customerinfotable.addCell(new Cell().add("Invoice No.").setBorder(Border.NO_BORDER));
        customerinfotable.addCell(new Cell().add(Invoice).setBorder(Border.NO_BORDER));

        customerinfotable.addCell(new Cell().add("Mob no.").setBorder(Border.NO_BORDER));
        customerinfotable.addCell(new Cell().add(Customer_phone).setBorder(Border.NO_BORDER));
        customerinfotable.addCell(new Cell().add("Date").setBorder(Border.NO_BORDER));
        customerinfotable.addCell(new Cell().add(Date).setBorder(Border.NO_BORDER));

        customerinfotable.addCell(new Cell().add("Address").setBorder(Border.NO_BORDER));
        customerinfotable.addCell(new Cell().add(Customer_address).setBorder(Border.NO_BORDER));



        float itemInfoColWidth[] = {140, 140, 140, 140};
        Table itemInfoTable = new Table(itemInfoColWidth);

        itemInfoTable.addCell(new Cell().add("Items")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE));

        itemInfoTable.addCell(new Cell().add("Quantity")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE));

        itemInfoTable.addCell(new Cell().add("Unit Price (INR)")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE));

        itemInfoTable.addCell(new Cell().add("Amount")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE));

//        Fetch details from database.

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM item_list";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String name = rs.getString("item_name");
                    float qty = rs.getFloat("quantity");
                    float price = rs.getFloat("unit_price");

                    float amount = qty * price;

                    Total_amount += amount;

                    itemInfoTable.addCell(new Cell().add(name));
                    itemInfoTable.addCell(new Cell().add(String.valueOf(qty)));
                    itemInfoTable.addCell(new Cell().add(String.valueOf(price))
                            .setTextAlignment(TextAlignment.RIGHT));
                    itemInfoTable.addCell(new Cell().add(String.valueOf(amount))
                            .setTextAlignment(TextAlignment.RIGHT));

                }

                String sql1 = "TRUNCATE TABLE item_list";
                stmt.executeUpdate(sql1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        itemInfoTable.addCell(new Cell().add("")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add("")
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add("Total Amount")
                .setTextAlignment(TextAlignment.RIGHT)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setBorder(Border.NO_BORDER));
        itemInfoTable.addCell(new Cell().add(String.valueOf(Total_amount))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBackgroundColor(new DeviceRgb(63, 169, 219))
                .setFontColor(Color.WHITE)
                .setBorder(Border.NO_BORDER));


        document.add(table);
        document.add(new Paragraph("\n"));
        document.add(customerinfotable);
        document.add(new Paragraph("\n"));
        document.add(itemInfoTable);
        document.add(new Paragraph("\n(Authorised Signatory)").setTextAlignment(TextAlignment.RIGHT));
        document.close();
        System.out.println("Hello World");

    }

    private void addDataToDatabase() {
        String item_name= it_name.getText();
        float quantity = Float.parseFloat(item_qty.getText());
        float unit_price = Float.parseFloat(ut_price.getText());

        String sql = "INSERT INTO item_list (item_name, quantity, unit_price) VALUES (?, ?, ?)";
        int rowsAffected = 0;

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item_name);
            stmt.setFloat(2, quantity);
            stmt.setFloat(3, unit_price);

            rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item added successfully");
                JOptionPane.showMessageDialog(null, "Item added successfully");
            } else {
                System.out.println("Failed to add Item");
                JOptionPane.showMessageDialog(null, "Item to add data");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPdfFile() {
        // Assuming the PDF file is located in the current directory
        File pdfFile = new File("E:\\Invoice.pdf");

        try {
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            } else {
                System.out.println("File not found: " + pdfFile.getAbsolutePath());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error opening PDF file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void main(String[] args){
        main obj1 = new main();

    }
}
