package jade;

import javax.swing.JFrame;

public class Main extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int SCREEN_WIDTH = 900;
	public static final int SCREEN_HEIGHT = 300;
	private Dinosaur dino;
	
	
	public Main() {
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setTitle("Dinosaur Game");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		dino = new Dinosaur();
		addKeyListener(dino);
		add(dino);
	}
	
	public void init() {
		setVisible(true);

		Thread gameThread = new Thread(dino);
		gameThread.start();
}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main().init();
	}

}
