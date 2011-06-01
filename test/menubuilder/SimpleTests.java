package menubuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import org.junit.Test;


public class SimpleTests {
	
	@Test
	public void plain_menu_and_separator() throws Exception {
		
		Reader input = new StringReader(
				"menu \"Hoge\" " +
				"\"aaa\" " +
				"--- " +
				"\"bbb\" " +
				"end");
		MenuBuilderLoader loader = new MenuBuilderLoader(input);
		loader.run();
		List<JMenu> menus = loader.getMenus();
		assertNotNull(menus);
		assertEquals(1, menus.size());
		
		JMenu hogeMenu = menus.get(0);
		assertEquals(3, hogeMenu.getMenuComponentCount());
		
		JMenuItem aaaMenuItem = (JMenuItem) hogeMenu.getMenuComponent(0);
		assertEquals("aaa", aaaMenuItem.getText());
		
		assertTrue(hogeMenu.getMenuComponent(1) instanceof JSeparator);
		
		JMenuItem bbbMenuItem = (JMenuItem) hogeMenu.getMenuComponent(2);
		assertEquals("bbb", bbbMenuItem.getText());
	}
	
	@Test
	public void multiple_menu() throws Exception {
		
		Reader input = new StringReader(
				"menu \"Hoge\" " +
				"\"aaa\" " +
				"end " +
				"menu \"Fuga\" " +
				"\"bbb\" " +
				"end");
		
		MenuBuilderLoader loader = new MenuBuilderLoader(input);
		loader.run();
		List<JMenu> menus = loader.getMenus();
		assertNotNull(menus);
		assertEquals(2, menus.size());
	}
	
	@Test
	public void checkbox_menu() throws Exception {

		Reader input = new StringReader(
				"menu \"Hoge\" " +
					"\"plain\" " +
					"--- " +
					"v \"checked\" " +
					"_ \"unchecked\" " +
				"end");
		
		MenuBuilderLoader loader = new MenuBuilderLoader(input);
		loader.run();
		List<JMenu> menus = loader.getMenus();
		assertNotNull(menus);
		assertEquals(1, menus.size());
		
		JMenu hogeMenu = menus.get(0);
		assertEquals(4, hogeMenu.getMenuComponentCount());
		assertEquals("Hoge", hogeMenu.getText());
		
		JCheckBoxMenuItem checked = (JCheckBoxMenuItem) hogeMenu.getMenuComponent(2);
		assertEquals("checked", checked.getText());
		assertTrue(checked.isSelected());
		
		JCheckBoxMenuItem unchecked = (JCheckBoxMenuItem) hogeMenu.getMenuComponent(3);
		assertEquals("unchecked", unchecked.getText());
		assertTrue(!unchecked.isSelected());
	}

	@Test
	public void radiobutton_menu() throws Exception {

		Reader input = new StringReader(
				"menu \"Hoge\" " +
					"\"plain\" " +
					"--- " +
					"x \"checked\" " +
					"o \"unchecked\" " +
				"end");
		
		MenuBuilderLoader loader = new MenuBuilderLoader(input);
		loader.run();
		List<JMenu> menus = loader.getMenus();
		assertNotNull(menus);
		assertEquals(1, menus.size());
		
		JMenu hogeMenu = menus.get(0);
		assertEquals(4, hogeMenu.getMenuComponentCount());
		
		JRadioButtonMenuItem checked = (JRadioButtonMenuItem) hogeMenu.getMenuComponent(2);
		assertEquals("checked", checked.getText());
		assertTrue(checked.isSelected());
		
		JRadioButtonMenuItem unchecked = (JRadioButtonMenuItem) hogeMenu.getMenuComponent(3);
		assertEquals("unchecked", unchecked.getText());
		assertTrue(!unchecked.isSelected());
	}

	@Test
	public void submenu() throws Exception {

		Reader input = new StringReader(
				"menu \"Hoge\" " +
					"> \"sub\" " +
				"end " +
				"submenu \"sub\" " +
				    "\"sub1\" " +
				    "--- " +
				    "v \"sub2\" " +
				"end");
		
		MenuBuilderLoader loader = new MenuBuilderLoader(input);
		loader.run();
		List<JMenu> menus = loader.getMenus();
		assertNotNull(menus);
		assertEquals(1, menus.size());
		
		JMenu hogeMenu = menus.get(0);
		assertEquals("Hoge", hogeMenu.getText());
		assertEquals(1, hogeMenu.getMenuComponentCount());
		
		JMenu submenu = (JMenu) hogeMenu.getMenuComponent(0);
		assertNotNull(submenu);
		assertEquals(3, submenu.getMenuComponentCount());
		assertEquals("sub", submenu.getText());
		
		JMenuItem sub1Menu = (JMenuItem) submenu.getMenuComponent(0);
		assertEquals("sub1", sub1Menu.getText());
		
	}
}
