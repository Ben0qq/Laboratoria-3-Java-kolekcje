import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/*
 * Program: Klasa obs³uguj¹ca aplikacjê okienkow¹ do zarz¹dzania grupami
 *    Plik: GroupManagerApp.java
 *          
 *   Autor: Damian Bednarz 241283
 *    Data: listopad 2018 r.
 *	
 */

public class GroupManagerApp extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;

		private static final String GREETING_MESSAGE =
				"Program do zarz¹dzania grupami osób " +
		        "- wersja okienkowa\n\n" +
		        "Autor: Damian Bednarz\n" +
				"Data:  listopad 2018 r.\n";
		
		private static final String ALL_GROUPS_FILE = "grupy.bin";

		public static void main (String[] args) {
			new GroupManagerApp();
		}
		
		WindowAdapter windowListener = new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				JOptionPane.showMessageDialog(null, "Wy³¹czono!");
			}

			@Override
			public void windowClosing(WindowEvent e) {
				windowClosed(e);
			}

		};
		
		private List<GroupOfPokemon> currentList = new ArrayList<GroupOfPokemon>();
		
		JMenuBar menuBar        = new JMenuBar();
		JMenu menuGroups        = new JMenu("Grupy");
		JMenu menuSpecialGroups = new JMenu("Grupy specjalne");
		JMenu menuAbout         = new JMenu("O programie");
		
		JMenuItem menuNewGroup           = new JMenuItem("Utwórz grupê");
		JMenuItem menuEditGroup          = new JMenuItem("Edytuj grupê");
		JMenuItem menuDeleteGroup        = new JMenuItem("Usuñ grupê");
		JMenuItem menuLoadGroup          = new JMenuItem("Za³aduj grupê z pliku");
		JMenuItem menuSaveGroup          = new JMenuItem("Zapisz grupê do pliku");

		JMenuItem menuGroupUnion         = new JMenuItem("Po³¹czenie grup");
		JMenuItem menuGroupIntersection  = new JMenuItem("Czêœæ wspólna grup");
		JMenuItem menuGroupDifference    = new JMenuItem("Ró¿nica grup");
		JMenuItem menuGroupSymmetricDiff = new JMenuItem("Ró¿nica symetryczna grup");

		JMenuItem menuAuthor             = new JMenuItem("Autor");

		JButton buttonNewGroup = new JButton("Utwórz");
		JButton buttonEditGroup = new JButton("Edytuj");
		JButton buttonDeleteGroup = new JButton(" Usuñ ");
		JButton buttonLoadGroup = new JButton("Otwórz");
		JButton buttonSaveGroup = new JButton("Zapisz");

		JButton buttonGroupUnion = new JButton("Suma");
		JButton buttonGroupIntersection = new JButton("Iloczyn");
		JButton buttonGroupDifference = new JButton("Ró¿nica");
		JButton buttonGroupSymmetricDiff = new JButton("Ró¿nica symetryczna");
		
		ViewGroupList viewList;
		
		public GroupManagerApp () {
			setTitle("GroupManager");
			setSize(450,400);
			setResizable(false);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent event) {
					try {
						saveGroupListToFile(ALL_GROUPS_FILE);
						JOptionPane.showMessageDialog(null, "Dane zosta³y zapisane do pliku " + ALL_GROUPS_FILE);
					} catch (PokemonException e) {
						JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
					}
				}

				@Override
				public void windowClosing(WindowEvent e) {
					windowClosed(e);
				}
			});
			
			try {
				loadGroupListFromFile(ALL_GROUPS_FILE);
				JOptionPane.showMessageDialog(null, "Dane zosta³y wczytane z pliku " + ALL_GROUPS_FILE);
			} catch (PokemonException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			}
			
			setJMenuBar(menuBar);
			menuBar.add(menuGroups);
			menuBar.add(menuSpecialGroups);
			menuBar.add(menuAbout);

			menuGroups.add(menuNewGroup);
			menuGroups.add(menuEditGroup);
			menuGroups.add(menuDeleteGroup);
			menuGroups.addSeparator();
			menuGroups.add(menuLoadGroup);
			menuGroups.add(menuSaveGroup);

			menuSpecialGroups.add(menuGroupUnion);
			menuSpecialGroups.add(menuGroupIntersection);
			menuSpecialGroups.add(menuGroupDifference);
			menuSpecialGroups.add(menuGroupSymmetricDiff);

			menuAbout.add(menuAuthor);

			menuNewGroup.addActionListener(this);
			menuEditGroup.addActionListener(this);
			menuDeleteGroup.addActionListener(this);
			menuLoadGroup.addActionListener(this);
			menuSaveGroup.addActionListener(this);
			menuGroupUnion.addActionListener(this);
			menuGroupIntersection.addActionListener(this);
			menuGroupDifference.addActionListener(this);
			menuGroupSymmetricDiff.addActionListener(this);
			menuAuthor.addActionListener(this);

			buttonNewGroup.addActionListener(this);
			buttonEditGroup.addActionListener(this);
			buttonDeleteGroup.addActionListener(this);
			buttonLoadGroup.addActionListener(this);
			buttonSaveGroup.addActionListener(this);
			buttonGroupUnion.addActionListener(this);
			buttonGroupIntersection.addActionListener(this);
			buttonGroupDifference.addActionListener(this);
			buttonGroupSymmetricDiff.addActionListener(this);

			viewList = new ViewGroupList(currentList, 400, 250);
			viewList.refreshView();

			JPanel panel = new JPanel();

			panel.add(viewList);
			panel.add(buttonNewGroup);
			panel.add(buttonEditGroup);
			panel.add(buttonDeleteGroup);
			panel.add(buttonLoadGroup);
			panel.add(buttonSaveGroup);
			panel.add(buttonGroupUnion);
			panel.add(buttonGroupIntersection);
			panel.add(buttonGroupDifference);
			panel.add(buttonGroupSymmetricDiff);

			setContentPane(panel);

			setVisible(true);
		}
		
		@SuppressWarnings("unchecked")
		void loadGroupListFromFile (String fileName) throws PokemonException {
			try (ObjectInputStream in= new ObjectInputStream (new FileInputStream(fileName))) {
				currentList = (List<GroupOfPokemon>)in.readObject();
			}catch(FileNotFoundException e) {
				throw new PokemonException ("nie znaleziono pliku");
			}catch(Exception e) {
				throw new PokemonException ("b³¹d odczytu");
			}
		}
		
		void saveGroupListToFile (String fileName) throws PokemonException {
			try (ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream(fileName))){
				out.writeObject(currentList);
			}catch(FileNotFoundException e) {
				throw new PokemonException ("nie znaleziono pliku");
			}catch(IOException e) {
				throw new PokemonException ("wyst¹pi³ b³¹d zapisu");
			}
		}
		
		private GroupOfPokemon chooseGroup (Window parent, String message) {
			Object[] groups = currentList.toArray();
			GroupOfPokemon group = (GroupOfPokemon)JOptionPane.showInputDialog(
									parent,
									message,
									"Wybierz grupê",
									JOptionPane.QUESTION_MESSAGE,
									null,
									groups,
									null);
			return group;
		}
		
		@Override
		public void actionPerformed (ActionEvent event) {
			Object source = event.getSource();
			
			try {
				if(source==menuNewGroup || source==buttonNewGroup) {
					GroupOfPokemon group = GroupOfPokemonWindowDialog.createNewGroupOfPokemon(this);
					if (group != null) {
						currentList.add(group);
					}
				}
				
				if(source==menuEditGroup || source==buttonEditGroup) {
					int index = viewList.getSelectedIndex();
					if (index >= 0) {
						Iterator<GroupOfPokemon> iterator = currentList.iterator();
						while (index-- > 0)
							iterator.next();
						new GroupOfPokemonWindowDialog(this, iterator.next());
					}
				}
				
				if(source==menuDeleteGroup || source==buttonDeleteGroup) {
					int index = viewList.getSelectedIndex();
					if (index >= 0) {
						Iterator<GroupOfPokemon> iterator = currentList.iterator();
						while (index-- > 0)
							iterator.next();
						iterator.next();
						iterator.remove();
					}
				}
				
				if(source == menuLoadGroup || source==buttonLoadGroup) {
					JFileChooser chooser = new JFileChooser (".");
					int returnVal = chooser.showOpenDialog(this);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						GroupOfPokemon group = GroupOfPokemon.readFromFile(chooser.getSelectedFile().getName());
						currentList.add(group);
					}
				}
				
				if(source == menuSaveGroup || source==buttonSaveGroup) {
					int index = viewList.getSelectedIndex();
					if (index >= 0) {
						Iterator<GroupOfPokemon> iterator = currentList.iterator();
						while (index-- > 0)
							iterator.next();
						GroupOfPokemon group = iterator.next();	
						
						JFileChooser chooser = new JFileChooser (".");
						int returnVal = chooser.showSaveDialog(this);
						if(returnVal == JFileChooser.APPROVE_OPTION) {
							GroupOfPokemon.printToFile(chooser.getSelectedFile().getName(),group);
						}
					}
				}
				
				if(source == menuGroupUnion || source==buttonGroupUnion) {
					String message1 = "SUMA GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby z obu grup\n"+
									  "Grupa 1:";
					String message2 = "SUMA GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby z obu grup\n"+
									  "Grupa 2:";
					GroupOfPokemon group1 = chooseGroup (this,message1);
					if(group1==null) return;
					GroupOfPokemon group2 = chooseGroup (this,message2);
					if(group2==null) return;
					currentList.add(GroupOfPokemon.createGroupUnion(group1, group2));
				}
				
				if(source == menuGroupIntersection || source==buttonGroupIntersection) {
					String message1 = "ILOCZYN GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby które znajduja siê w obu grupach\n"+
									  "Grupa 1:";
					String message2 = "ILOCZYN GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby które znajduja siê w obu grupach\n"+
									  "Grupa 2:";
					GroupOfPokemon group1 = chooseGroup (this,message1);
					if(group1==null) return;
					GroupOfPokemon group2 = chooseGroup (this,message2);
					if(group2==null) return;
					currentList.add(GroupOfPokemon.createGroupIntersection(group1, group2));
				}
				
				if(source == menuGroupDifference || source==buttonGroupDifference) {
					String message1 = "RÓ¯NICA GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby z grupy 1 których nie ma w grupie 2\n"+
									  "Grupa 1:";
					String message2 = "RÓ¯NICA GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby z grupy 1 których nie ma w grupie 2\n"+
									  "Grupa 2:";
					GroupOfPokemon group1 = chooseGroup (this,message1);
					if(group1==null) return;
					GroupOfPokemon group2 = chooseGroup (this,message2);
					if(group2==null) return;
					currentList.add(GroupOfPokemon.createGroupDifference(group1, group2));
				}
				
				if(source == menuGroupSymmetricDiff || source==buttonGroupSymmetricDiff) {
					String message1 = "RÓ¯NICA SYMETRYCZNA GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby z obu grup\n"+
									  "oprócz tych które wystêpuj¹ w obu grupach\n"+
									  "Grupa 1:";
					String message2 = "RÓ¯NICA SYMETRYCZNA GRUP \n\n"+
									  "Nowa grupa zawiera wszystkie osoby z obu grup\n"+
									  "oprócz tych które wystêpuj¹ w obu grupach\n"+
									  "Grupa 1:";
					GroupOfPokemon group1 = chooseGroup (this,message1);
					if(group1==null) return;
					GroupOfPokemon group2 = chooseGroup (this,message2);
					if(group2==null) return;
					currentList.add(GroupOfPokemon.createGroupSymmetricDiff(group1, group2));
				}
				
				if (source == menuAuthor) {
					JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
				}
				
			}catch (PokemonException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			}
			viewList.refreshView();
		}
}

class ViewGroupList extends JScrollPane{
	private static final long serialVersionUID = 1L;
	private List<GroupOfPokemon> list;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public ViewGroupList (List<GroupOfPokemon> list,int width,int height) {
		this.list=list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista grup:"));
		String[] tableHeader = {"Nazwa grupy", "Typ kolekcji", "Liczba osób"};
		tableModel = new DefaultTableModel(tableHeader,0);
		table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
		};
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}
	
	void refreshView () {
		tableModel.setRowCount(0);
		for(GroupOfPokemon group : list) {
			if(group!=null) {
				String[]row = {group.getCollectionName(), group.getCollectionType().toString(),""+group.size()};
				tableModel.addRow(row);
			}
		}
	}
	
	int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index<0) {
			JOptionPane.showMessageDialog(this, "¯adana grupa nie jest zaznaczona.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}
}
