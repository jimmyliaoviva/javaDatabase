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
	//這邊是資料庫的基本設定
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
		
		//設定字體大小
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
							//搜尋可以有兩種方法，一個是名字，一個是電話號碼
							//利用 例外處理來分辨是否為電話號碼，若是可以parseInt那就是數字，不行就是文字
							try {
							
								int phoneNum = Integer.parseInt(text);
								//上面那行沒有出現例外才會執行下面
								//利用SQL搜尋
								String query = "SELECT name FROM people WHERE phone like"+ "\'%" + text +"%\'";
								try {
									tableModel.setQuery(query);
									}//end try
								catch(Exception ex) {
									ex.printStackTrace();
									}//end catch
								}//end try
							catch(Exception ex) {
								//有一種可能是JTable 被上面的setQuery搜過一遍了，所以抓的資料庫不一樣，要先重新抓回DEFAULT_QUERY
								try {
									tableModel.setQuery(DEFAULT_QUERY);
								} catch (IllegalStateException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}//end catch
								//這邊採用課本sorter寫法來做文字搜尋
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
		
	//這是用來抓雙擊資料用的  
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
			resultSet.absolute(1);//指定哪一行
			String type = resultSet.getObject(3).toString();
			String phone = resultSet.getObject(4).toString();
			String options[] = {"Update","Delete"};
			int choice = JOptionPane.showOptionDialog(null, type+" : "+phone, name,
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, "Update");
			if (choice == 0) {
				System.out.println("進入新增畫面");
				InsertFrame insertFrame = new InsertFrame(name,type,phone,true);
				//insertFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//insertFrame.setSize(500, 600);
				//insertFrame.setVisible(true);
				
			}//end if
			else if(choice ==1) {
				System.out.println("刪除");
				//執行刪除的部分
				statement.executeUpdate("DELETE FROM people WHERE name="+"'"+name+"'");
				//要重新執行一次setQuery()
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
