package menubuilder;

import static menubuilder.MenuLexer.MENU;
import static menubuilder.MenuLexer.MENU_ITEM;
import static menubuilder.MenuLexer.SUBMENU;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

public class MenuBuilderLoader {

	private Reader input;
	private MfTree ast;
	private List<JMenu> result = new ArrayList<JMenu>();
	private Map<String, JMenu> submenus = new HashMap<String, JMenu>();

	public MenuBuilderLoader(Reader input) {
		this.input = input;
	}
	
	public void run() {
		loadAST();
		loadSymbols();
	}

	private void loadSymbols() {
		
		loadMenus();
		loadSubMenus();
	}

	private void loadSubMenus() {
		
		List<MfTree> submenuNodeList = ast.getChildren(SUBMENU);

		for (MfTree submenuNode : submenuNodeList) {
			
			String submenuName = StringUtil.stripDoubleQuote(submenuNode.getText(0));
			JMenu submenu = submenus.get(submenuName);
			if (submenu == null) continue;
			
			List<MfTree> menuItemNodes = submenuNode.getChildren(MENU_ITEM);
			loadMenuItems(submenu, menuItemNodes);
			
		}
	}

	private void loadMenus() {
		
		List<MfTree> menuNodeList = ast.getChildren(MENU);

		for (MfTree menuNode : menuNodeList) {
			
			String menuName = StringUtil.stripDoubleQuote(menuNode.getText(0));
			List<MfTree> menuItems = menuNode.getChildren(MENU_ITEM);
			
			JMenu menu = new JMenu(menuName);
			result.add(menu);
			loadMenuItems(menu, menuItems);
		}
	}

	private void loadMenuItems(JMenu menu, List<MfTree> menuItemNodes) {
		
		ButtonGroup group = null;
		for (MfTree menuItemNode : menuItemNodes) {
			
			if (menuItemNode.getChildCount() < 1) {
				menu.addSeparator();
				group = null;
				continue;
			}
			
			String first = StringUtil.stripDoubleQuote(menuItemNode.getText(0));
			if (menuItemNode.getChildCount() < 2) {
				menu.add(new JMenuItem(first));
				group = null;
				continue;
			}
			
			String second = StringUtil.stripDoubleQuote(menuItemNode.getText(1));
			if (second.equals("o")) {
				JRadioButtonMenuItem item = new JRadioButtonMenuItem(first, false);
				menu.add(item);
				if (group == null) group = new ButtonGroup();
				group.add(item);
			} else if (second.equals("x")) {
				JRadioButtonMenuItem item = new JRadioButtonMenuItem(first, true);
				menu.add(item);
				if (group == null) group = new ButtonGroup();
				group.add(item);
			} else if (second.equals("v")) {
				JCheckBoxMenuItem item = new JCheckBoxMenuItem(first, true);
				menu.add(item);
				group = null;
			} else if (second.equals("_")) {
				JCheckBoxMenuItem item = new JCheckBoxMenuItem(first, false);
				menu.add(item);
				group = null;
			} else if (second.equals(">")) {
				JMenu submenu = new JMenu(first);
				menu.add(submenu);
				submenus.put(first, submenu);
				group = null;
			} else {
				assert false : "unexpected marker of menu item.";
			}
		}
	}

	private void loadAST() {
		
		try {
			MenuLexer lexer = new MenuLexer(new ANTLRReaderStream(input));
			MenuParser parser = new MenuParser(new CommonTokenStream(lexer));
			parser.helper = this;
			parser.setTreeAdaptor(new MyNodeAdaptor());
			ast = (MfTree) parser.rule().getTree();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (RecognitionException e) {
			throw new RuntimeException(e);
		}
	}

	public List<JMenu> getMenus() {
		return result;
	}
}
