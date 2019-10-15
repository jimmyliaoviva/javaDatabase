import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InsertFrame extends JFrame{
	private JLabel nameLabel;
	private JLabel typeLabel;
	private JLabel phoneLabel;
	private JTextField nameTextField;
	//private JTextField typeTextField;
	private JTextField phoneTextField;
	//private JComboBox typeComboBox;
	private JButton insertButton;
	private JPanel displayPnl;
	private String options[] = {"cell","company","home"};
	private JComboBox typeComboBox = new JComboBox<String>(options);
	private String name,type,phone;
	private Boolean isModified;
	private PersonQueries personQuery = new PersonQueries();
	
	
	public InsertFrame() {
		this("", "", "",false);
		
	}
	public InsertFrame(String name,String type,String phone,boolean isModified) {
		super("Insert");
		
		
		this.name = name;
		this.type = type;
		this.phone = phone;
		this.isModified = isModified;
		//設定視窗基本資訊
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(300,400);
		this.setVisible(true);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		
		//最大的Panel 做排版用
		displayPnl = new JPanel();
		displayPnl.setLayout(new GridLayout(3,2,4,4));
		
		nameLabel = new JLabel("Name");
		displayPnl.add(nameLabel);
		//初始化並設定大小和文字
		nameTextField = new JTextField(10);
		nameTextField.setText(name);
		displayPnl.add(nameTextField);
		typeLabel = new JLabel("Type");
		displayPnl.add(typeLabel);
		
	
		//typeTextField = new JTextField(type);
		displayPnl.add(typeComboBox);
		phoneLabel = new JLabel("Phone");
		displayPnl.add(phoneLabel);
		phoneTextField = new JTextField(10);
		phoneTextField.setText(phone);
		
		displayPnl.add(phoneTextField);
		insertButton = new JButton("Insert");
		insertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//connect();
				String name = nameTextField.getText();
				String type = typeComboBox.getSelectedItem().toString();
				String phone = phoneTextField.getText();
				System.out.printf("%s%n%s%n%s%n", name,type,phone);
				if(type == "cell" && phone.matches("(09)+[\\d]{8}")) {
					System.out.println("cell");
					insert(name,type,phone);
				}//結束cell判斷
				else if(phone.matches("(0)+[\\d]{8}")||phone.matches("(0)+[\\d]{9}")) {
					insert(name,type,phone);
				}
				else {
					JOptionPane.showMessageDialog(null, "Syntax Error!!",
							"Error!", JOptionPane.PLAIN_MESSAGE);
				}//end else
				
				//personQuery.close();
				//上面使用了很多personQuery的preparedStatement，
				try {
					Main.contactFrame.tableModel.setQuery(
							"SELECT name FROM people");
					
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}//end actionPerformed
		});
		add(displayPnl);
		add(insertButton);
	
	}//end constructor
	
	
	public void insert(String name,String type,String phone) {
		if(isModified) {
			//updateData();
			int result = personQuery.updatePerson(
					nameTextField.getText(), typeComboBox.getSelectedItem().toString(),
					phoneTextField.getText(), name);
		
			if(result==1) {
				JOptionPane.showMessageDialog(null,"Person updated!",
						"Person updated", JOptionPane.PLAIN_MESSAGE);
			}//end if
			else
				JOptionPane.showMessageDialog(null, "Person not updated!!",
						"Error!", JOptionPane.PLAIN_MESSAGE);
		}//end if
		else {
			int result = personQuery.addPerson(
					nameTextField.getText(), typeComboBox.getSelectedItem().toString(),
					phoneTextField.getText());
			if(result==1) {
				JOptionPane.showMessageDialog(null,"Person added!",
						"Person added", JOptionPane.PLAIN_MESSAGE);
			
			}//end if
			else 
				JOptionPane.showMessageDialog(null, "Person not added!!",
						"Error!", JOptionPane.PLAIN_MESSAGE);
		}//end else
	}//end insert
	/*
	 
	public void connect() {
		 try {

	            
	            Connection con= DriverManager.getConnection(DATABASE_URL,"java","java");
	            statement= con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	            		ResultSet.CONCUR_UPDATABLE);
	             
	        }catch(Exception ex){
	            ex.printStackTrace();
	        }//end catch
	    
	}//end connect
	*/
/*	public void insertData()
	{
		String query = "INSERT * FROM member.people WHERE name =";
		try {
			resultSet = statement.executeQuery(query);
			resultSet.absolute(1);
			String type = resultSet.getObject(3).toString();
			String phone = resultSet.getObject(4).toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}//end catch
		
	}//end insertData
	
	public void updateData() {
		String query = "UPDATE people SET name = \'"+ nameTextField.getText()+"\',type = \'"+typeTextField.getText()+
				"\',phone = \'"+phoneTextField.getText()+"\'WHERE name = "+ "\'" + name +"\'";
		try {
			statement.executeUpdate(query);
			Main.contactFrame.tableModel.setQuery(DEFAULT_QUERY);
		}//end try
		catch(Exception e) {
			e.printStackTrace();
		}//end catch
	}//end updateData
	*/
}//end class InsertFrame
