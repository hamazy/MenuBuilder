package menubuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Reader input;
		try {
			input = new FileReader("src/menubuilder/menudef.txt");
			
			MenuBuilderLoader loader = new MenuBuilderLoader(input);
			loader.run();
			List<JMenu> menus = loader.getMenus();

			JMenuBar menuBar = new JMenuBar();
			for (JMenu menu : menus) {
				menuBar.add(menu);
			}
			JFrame frame = new JFrame("Demo");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setJMenuBar(menuBar);
			frame.setVisible(true);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
