import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ContactFrame extends JFrame{
	//�o��O��Ʈw���򥻳]�w
	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/member";
	private static final String USERNAME = "java";
	private static final String PASSWORD = "java";
	
	private static final String DEFAULT_QUERY = "SELECT name FROM people";
	
	private final JPanel northPnl;
	private final JTextField searchTextField;
	private final JButton searchButton;
	private final JLabel contactLabel = new JLabel("Contact");
	private final JLabel addLabel = new JLabel();
	public ImageIcon addIcon = new ImageIcon(
			new ImageIcon(getClass().getResource("add.JPG")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

	
	public static ResultSetTableModel tableModel;
	private JTable resultTable;
	private Statement statement;
	private ResultSet resultSet;
	
	public ContactFrame(){
		super("Contacts");
		setLayout(new BorderLayout());
		
		
		connect();
		
		
		northPnl = new JPanel();
		searchTextField = new JTextField();
		searchButton = new JButton("Search");
		addLabel.setIcon(addIcon);
		
		//�]�w�r��j�p
		contactLabel.setFont(contactLabel.getFont().deriveFont(30f));
		try {
			tableModel = new ResultSetTableModel(
					DATABASE_URL,USERNAME,PASSWORD,DEFAULT_QUERY);
			
			JPanel northnorthBox = new JPanel(new BorderLayout());
			Box northsouthBox = Box.createHorizontalBox();
			
			northnorthBox.add(contactLabel,BorderLayout.WEST);
			northnorthBox.add(addLabel,BorderLayout.EAST);
			
			resultTable = new JTable(tableModel);
			final TableRowSorter<TableModel> sorter =
					new TableRowSorter<TableModel>(tableModel);
			resultTable.setRowSorter(sorter);
			
			resultTable.addMouseListener(
					new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if(e.getClickCount()==2) {
								System.out.println("double click");
								String name = resultTable.getValueAt(resultTable.getSelectedRow(), resultTable.getSelectedColumn()).toString();
								//System.out.println(name);
								
								getData(name);
							
							}//end if
						}//end mouseClicked
					});
			
			searchButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							String text = searchTextField.getText();
							//�j�M�i�H����ؤ�k�A�@�ӬO�W�r�A�@�ӬO�q�ܸ��X
							//�Q�� �ҥ~�B�z�Ӥ���O�_���q�ܸ��X�A�Y�O�i�HparseInt���N�O�Ʀr�A����N�O��r
							try {
							
								int phoneNum = Integer.parseInt(text);
								//�W������S���X�{�ҥ~�~�|����U��
								//�Q��SQL�j�M
								String query = "SELECT name FROM people WHERE phone like"+ "\'%" + text +"%\'";
								try {
									tableModel.setQuery(query);
									}//end try
								catch(Exception ex) {
									ex.printStackTrace();
									}//end catch
								}//end try
							catch(Exception ex) {
								//���@�إi��OJTable �Q�W����setQuery�j�L�@�M�F�A�ҥH�쪺��Ʈw���@�ˡA�n�����s��^DEFAULT_QUERY
								try {
									tableModel.setQuery(DEFAULT_QUERY);
								} catch (IllegalStateException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}//end catch
								//�o��ĥνҥ�sorter�g�k�Ӱ���r�j�M
								if(text.length()==0) {
									sorter.setRowFilter(null);
								}//end if
								else {
									sorter.setRowFilter(RowFilter.regexFilter(text));
								}//end else
							}//end catch
							
							
						}//end actionPerformed
					});
			
			
			
			addLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					addPerson();
						
				}//end mouseClicked
			});
			
			
			
			northsouthBox.add(searchTextField);
			northsouthBox.add(searchButton);
			
			northPnl.setLayout(new BorderLayout());
			northPnl.add(northnorthBox, BorderLayout.NORTH);
			northPnl.add(northsouthBox, BorderLayout.SOUTH);
			
			
			add(new JScrollPane(resultTable),BorderLayout.CENTER);
			add(northPnl,BorderLayout.NORTH);
			
			
		} catch (SQLException e) {
			 //TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//�o�O�Ψӧ�������ƥΪ�  
			/*try {

		            
		           Connection con= DriverManager.getConnection(DATABASE_URL,"java","java");
		           statement= con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
		            		ResultSet.CONCUR_UPDATABLE);
		             
		       }catch(Exception ex){
		           ex.printStackTrace();
		       }//end catch
		    */
	
			
	}//end constructor
	
	public void connect() {
		 try {

	            
	            Connection con= DriverManager.getConnection(DATABASE_URL,"java","java");
	            statement= con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
	            		ResultSet.CONCUR_UPDATABLE);
	             
	        }catch(Exception ex){
	            ex.printStackTrace();
	        }//end catch
	    
	}//end connect
	
	
	public void getData(String name) {
		String query = "SELECT * FROM member.people WHERE name = "+ "\'" + name +"\'";
		try {
			resultSet = statement.executeQuery(query);
			resultSet.absolute(1);//���w���@��
			String type = resultSet.getObject(3).toString();
			String phone = resultSet.getObject(4).toString();
			String options[] = {"Update","Delete"};
			int choice = JOptionPane.showOptionDialog(null, type+" : "+phone, name,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, "Update");
			if (choice == 0) {
				System.out.println("�i�J�s�W�e��");
				InsertFrame insertFrame = new InsertFrame(name,type,phone,true);
				//insertFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//insertFrame.setSize(500, 600);
				//insertFrame.setVisible(true);
				
			}//end if
			else if(choice ==1) {
				System.out.println("�R��");
				//����R��������
				statement.executeUpdate("DELETE FROM people WHERE name="+"'"+name+"'");
				//�n���s����@��setQuery()
				//tableModel.fireTableStructureChanged();
				tableModel.setQuery(DEFAULT_QUERY);
				//resultTable.setModel(tableModel);
				//validate();
				//repaint();
			}//end else if
			
			//System.out.println(type);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}//end getData
	
	public void addPerson() {
		
		

			InsertFrame insertFrame = new InsertFrame();
			//resultSet = statement.executeQuery(DEFAULT_QUERY);
			
			//tableModel.setQuery(DEFAULT_QUERY);
		
		
	}//end addPerson
}//end class ContactFrame
