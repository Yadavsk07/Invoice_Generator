
import java.io.FileNotFoundException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main extends JFrame{

    private JTextField invoice_no;
    private JTextField date;

    private JTextField com_name;
    private JTextArea company_address;
    private JTextField ph_no;

    private JTextField customer_name;
    private JTextArea customer_address;
    private JTextField customer_ph;

    public main(){

        JFrame jFrame = new JFrame();
        jFrame.setTitle("Invoice Generator");

        JLabel invoice_number = new JLabel("Invoice N0.:");
        invoice_number.setBounds(20 , 10 , 100 , 20);

        invoice_no = new JTextField();
        invoice_no.setBounds(120 , 10 , 100 , 20);

        JLabel dte = new JLabel("Date");
        dte.setBounds(20 , 40 , 100 , 20);

        date = new JTextField("dd/mm/yyyy");
        date.setBounds(120 , 40 , 100 , 20);


        JLabel company = new JLabel("Company Details");
        company.setBounds(20 , 60 , 100 , 20);

        JLabel cname = new JLabel("Company name:");
        cname.setBounds(20 ,90 ,100 , 20 );

        com_name = new JTextField();
        com_name.setBounds(120 , 90 ,150 ,20);

        JLabel c_address = new JLabel("Address:");
        c_address.setBounds(20 ,120 ,100 ,20);

        company_address = new JTextArea();
        company_address.setBounds(120 ,120 ,200 ,100);

        JLabel phone = new JLabel("Phone no.:");
        phone.setBounds(20, 230 ,100 , 20);

        ph_no = new JTextField();
        ph_no.setBounds(120 , 230 ,150 ,20);

        JLabel customer= new JLabel("Customer Details");
        customer.setBounds(20 , 270 ,120 ,20);

        JLabel custom_name = new JLabel("Customer name:");
        custom_name.setBounds(20 ,300 ,120 , 20 );

        customer_name = new JTextField();
        customer_name.setBounds(120 , 300 ,150 ,20);

        JLabel custom_address = new JLabel("Address:");
        custom_address.setBounds(20 ,330 ,100 ,20);

        customer_address = new JTextArea();
        customer_address.setBounds(120 ,330 ,200 ,100);

        JLabel customer_phone = new JLabel("Phone no.:");
        customer_phone.setBounds(20, 440 ,100 , 20);

        customer_ph = new JTextField();
        customer_ph.setBounds(120 , 440 ,150 ,20);



        JButton btn = new JButton("Next");
        btn.setBounds(150 , 500 , 80 ,30);

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String company_name = com_name.getText();
                String Company_address = company_address.getText();
                String Company_phone = ph_no.getText();

                String Customer_name = customer_name.getText();
                String Customer_address = customer_address.getText();
                String Customer_phone = customer_ph.getText();

                String Date = date.getText();
                String Invoice = invoice_no.getText();

                jFrame.setVisible(false);
                new Item(company_name , Company_address , Company_phone , Customer_name , Customer_address , Customer_phone , Date , Invoice).setVisible(true);
            }
        });

        jFrame.add(btn);
        jFrame.add(custom_name);
        jFrame.add(customer_name);
        jFrame.add(custom_address);
        jFrame.add(customer_address);
        jFrame.add(customer_phone);
        jFrame.add(customer_ph);
        jFrame.add(customer);
        jFrame.add(phone);
        jFrame.add(ph_no);
        jFrame.add(c_address);
        jFrame.add(company_address);
        jFrame.add(com_name);
        jFrame.add(company);
        jFrame.add(cname);

        jFrame.add(dte);
        jFrame.add(date);
        jFrame.add(invoice_number);
        jFrame.add(invoice_no);
        jFrame.setLayout(null);
        jFrame.setSize(400 , 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }



    public static void main(String[] args) throws FileNotFoundException {

        main obj = new main();
//        obj.create_Pdf();


    }
}
