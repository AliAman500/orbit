package ali.orbit;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		App app = new App();
		JFrame frame = new JFrame("Orbit");
		frame.setSize(1014, 737);
		frame.add(app);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		app.start();
	}

}
