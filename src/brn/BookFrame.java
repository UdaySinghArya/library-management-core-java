package brn;
import com.mysql.cj.jdbc.result.UpdatableResultSet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;  // It is Used for MySql and JDBC
import java.util.ArrayList; //for Arraylist class
import javax.swing.*;  //It is used for GUI
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class BookFrame {
    Connection con;  //Interface
    PreparedStatement ps; //For query Writing and It is a class
    JFrame frame = new JFrame("Book Project");
    JTabbedPane tabbedPane;
    JPanel insertPanel, viewPanel;

    JLabel l1, l2, l3, l4, l5;
    JTextField t1, t2, t3, t4, t5;
    JButton saveButton, updateButton, deleteButton;
    JTable table;
    JScrollPane scrollPane;
    DefaultTableModel tm;
    String[] colNames = {"book Id", "title", "price", "author", "publisher"};

    //First Call
    public BookFrame() {
        getConnectionFromMySql();
        initComponents();
    }

    void getConnectionFromMySql() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "root1");
            System.out.println("Connection Established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void initComponents() {
        //Components for Insert form

        //label
        l1 = new JLabel();
        l1.setText("book Id");
        l2 = new JLabel();
        l2.setText("title");
        l3 = new JLabel();
        l3.setText("price");
        l4 = new JLabel();
        l4.setText("author");
        l5 = new JLabel();
        l5.setText("publisher");

        //JTextfield
        t1 = new JTextField();
        t2 = new JTextField();
        t3 = new JTextField();
        t4 = new JTextField();
        t5 = new JTextField();

        //Button
        saveButton = new JButton("Save Button");

        //setBounds
        l1.setBounds(100, 100, 100, 20);
        l2.setBounds(100, 150, 100, 20);
        l3.setBounds(100, 200, 100, 20);
        l4.setBounds(100, 250, 100, 20);
        l5.setBounds(100, 300, 100, 20);
        t1.setBounds(250, 100, 100, 20);
        t2.setBounds(250, 150, 100, 20);
        t3.setBounds(250, 200, 100, 20);
        t4.setBounds(250, 250, 100, 20);
        t5.setBounds(250, 300, 100, 20);
        saveButton.setBounds(100, 350, 150, 30);


        insertPanel = new JPanel();
        insertPanel.setLayout(null);
        insertPanel.add(l1);
        insertPanel.add(l2);
        insertPanel.add(l3);
        insertPanel.add(l4);
        insertPanel.add(l5);
        insertPanel.add(t1);
        insertPanel.add(t2);
        insertPanel.add(t3);
        insertPanel.add(t4);
        insertPanel.add(t5);
        insertPanel.add(saveButton);


        //SaveButton Event handling
        saveButton.addActionListener(new InsertBookRecord());

        ArrayList<Book> bookList=fetchBookRecords();           //Caller
        setDataOnTable(bookList);                             //Caller

        updateButton=new JButton("Update Book");
        //Update Button event Handing
        updateButton.addActionListener(new updateBookRecord());

        deleteButton=new JButton("Delete Book");

        //Delete Button Event handling
        deleteButton.addActionListener(new DeletedBookRecord());

        //object of Panel
        viewPanel=new JPanel();
        viewPanel.add(updateButton);
        viewPanel.add(deleteButton);

        //Object Of Scroll Pane
        scrollPane=new JScrollPane(table);

        viewPanel.add(scrollPane);




        //JTabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.add(insertPanel);
        tabbedPane.add(viewPanel);
        tabbedPane.addChangeListener(new TabChangeHandler());

        //Add TabbedPane on Frame
        frame.add(tabbedPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    //Set Data on Table
    void setDataOnTable(ArrayList<Book>  bookList){
        Object [][]obj=new Object[bookList.size()][5];
        for(int i=0;i<bookList.size();i++){
            obj[i][0]=bookList.get(i).getBookId();
            obj[i][1]=bookList.get(i).getTitle();
            obj[i][2]=bookList.get(i).getPrice();
            obj[i][3]=bookList.get(i).getAuthor();
            obj[i][4]=bookList.get(i).getPublisher();

        }
        //Object of JTable
        table=new JTable();

        //DefaultTableModel
        tm=new DefaultTableModel();
        tm.setColumnCount(5);  //set columns of table
        tm.setRowCount(bookList.size());  //set rows of table
        tm.setColumnIdentifiers(colNames);   //set columns names of table
        for(int i=0;i<bookList.size();i++){
            tm.setValueAt(obj[i][0],i,0);
            tm.setValueAt(obj[i][1],i,1);
            tm.setValueAt(obj[i][2],i,2);
            tm.setValueAt(obj[i][3],i,3);
            tm.setValueAt(obj[i][4],i,4);
        }
        table.setModel(tm);

    }
    void updateTable(ArrayList<Book>  bookList){
        Object [][]obj=new Object[bookList.size()][5];
        for(int i=0;i<bookList.size();i++){
            obj[i][0]=bookList.get(i).getBookId();
            obj[i][1]=bookList.get(i).getTitle();
            obj[i][2]=bookList.get(i).getPrice();
            obj[i][3]=bookList.get(i).getAuthor();
            obj[i][4]=bookList.get(i).getPublisher();

        }
        tm.setRowCount(bookList.size());  //set rows of table

        for(int i=0;i<bookList.size();i++){
            tm.setValueAt(obj[i][0],i,0);
            tm.setValueAt(obj[i][1],i,1);
            tm.setValueAt(obj[i][2],i,2);
            tm.setValueAt(obj[i][3],i,3);
            tm.setValueAt(obj[i][4],i,4);
        }
        table.setModel(tm);

    }
    //To Fetch Book Record
    ArrayList<Book> fetchBookRecords(){
        ArrayList <Book>bookList=new ArrayList<Book>();
        String q="select * from book";
        try{
            ps=con.prepareStatement(q);
            ResultSet rs=ps.executeQuery();

            while(rs.next()){
                Book b=new Book();
                b.setBookId(rs.getInt(1));
                b.setTitle(rs.getString(2));
                b.setPrice(rs.getInt(3));
                b.setAuthor(rs.getString(4));
                b.setPublisher(rs.getString(5));
                bookList.add(b);  //Store values of reference variable b in the ArrayList.
            }
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        finally {
            return bookList;
        }

    }
    class InsertBookRecord implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Book b1=readFormData();

            String q="insert into book(bookId,title,price,author,publisher)  values(?,?,?,?,?)";

            try{
                ps=con.prepareStatement(q);
                ps.setInt(1,b1.getBookId());
                ps.setString(2,b1.getTitle());
                ps.setDouble(3,b1.getPrice());
                ps.setString(4,b1.getAuthor());
                ps.setString(5,b1.getPublisher());

                ps.execute();

                //To Make Empty Of TextField
                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");
                t5.setText("");

            }
            catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        }
            Book readFormData(){
            Book b1=new Book();
            b1.setBookId(Integer.parseInt(t1.getText()));
            b1.setTitle(t2.getText());
            b1.setPrice(Integer.parseInt(t3.getText()));
            b1.setAuthor(t4.getText());
            b1.setPublisher(t5.getText());

            return b1;
        }
    }

    class TabChangeHandler implements ChangeListener{

        @Override
        public void stateChanged(ChangeEvent e) {
            int index=tabbedPane.getSelectedIndex();
            if(index==0){
                System.out.println("Insert");
            }
            if(index==1){
                ArrayList <Book>bookList=fetchBookRecords();
                updateTable(bookList);
            }
        }
    }
    class updateBookRecord implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Book> updatedBookList = readTableData();
            String q = "update book set title=?,price=?,author=?,publisher=? where bookId=?";
            try {
                for (int i = 0; i < updatedBookList.size(); i++) {
                    ps = con.prepareStatement(q);
                    ps.setString(1, updatedBookList.get(i).getTitle());
                    ps.setInt(2, updatedBookList.get(i).getPrice());
                    ps.setString(3, updatedBookList.get(i).getAuthor());
                    ps.setString(4, updatedBookList.get(i).getPublisher());
                    ps.setInt(5, updatedBookList.get(i).getBookId());
                    ps.executeUpdate();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            } finally {
                // Close the PreparedStatement after execution
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }

                // Refresh the table with updated data
                ArrayList<Book> bookList = fetchBookRecords();
                updateTable(bookList);
            }
        }
        ArrayList<Book> readTableData() {
            ArrayList<Book> updatedBookList = new ArrayList<Book>();
            for (int i = 0; i < table.getRowCount(); i++) {
                Book b = new Book();

                b.setTitle(table.getValueAt(i, 1).toString());
                b.setPrice(Integer.parseInt(table.getValueAt(i, 2).toString()));
                b.setAuthor(table.getValueAt(i, 3).toString());
                b.setPublisher(table.getValueAt(i, 4).toString());
                b.setBookId(Integer.parseInt(table.getValueAt(i, 0).toString()));
                updatedBookList.add(b);

            }

            return updatedBookList;
        }
    }
    class DeletedBookRecord implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int rowNo=table.getSelectedRow();
            if(rowNo!=1){
                int id=(int)table.getValueAt(rowNo,0);
                String q="delete from book where bookId=?";
                try{
                    ps=con.prepareStatement(q);
                    ps.setInt(1,id);  //put id(bookid) in place of ?(Question mark)
                    ps.execute();
                }
                catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                finally{
                    ArrayList <Book> bookList=fetchBookRecords();
                    updateTable(bookList);
                }

            }
        }
    }
}