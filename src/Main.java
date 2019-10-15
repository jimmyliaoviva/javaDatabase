import javax.swing.JFrame;

public class Main {
	public static ContactFrame contactFrame;
	public static void main(String[]args) {
		
		contactFrame = new ContactFrame();
		contactFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contactFrame.setSize(500, 600);
		contactFrame.setVisible(true);

		
		
	}//end main
}//end Main
