import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/*
 * Program: Klasa pomocnicza obs³uguj¹ca okienko do edycji grupy
 *    Plik: GroupOfPokemonWindowDialog.java
 *          
 *   Autor: Damian Bednarz 241283
 *    Data: listopad 2018 r.
 *	
 */

public class GroupOfPokemonWindowDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private GroupOfPokemon group;
	
	Font font = new Font("MonoSpaced", Font.BOLD, 12);
	
	JLabel labelName = new JLabel("Nazwa kolekcji:");
	JLabel labelType = new JLabel("Nazwa typu:");
	
	JTextField fieldName = new JTextField(15);
	JTextField fieldType = new JTextField(15);
	
	JButton newElement = new JButton("Utwórz nowego pokemona");
	JButton changeElement = new JButton ("Zmieñ pokemona");
	JButton groupName = new JButton("Zmieñ nazwê kolekcji");
	JButton typeName = new JButton ("Zmieñ typ kolekcji");
	JButton deleteElement = new JButton("Usuñ pokemona");
	JButton saveButton = new JButton ("Zapisz kolekcjê");
	JButton loadButton = new JButton("Wczytaj kolekcjê");
	JButton binarySaveButton = new JButton ("Zapisz kolekcjê binarnie");
	JButton binaryLoadButton = new JButton("Wczytaj kolekcjê binarnie");
	JButton sortName = new JButton("Posortuj wed³ug nazwy");
	JButton sortNumber = new JButton("Posortuj wed³ug numeru");
	JButton sortMainType = new JButton("Posortuj wed³ug typu");
	JButton sortSecondType = new JButton("Posortuj wed³ug drugiego typu");
	ViewPokemonCollection viewCol;
	
	public GroupOfPokemonWindowDialog (Window parent, GroupOfPokemon group) {
		super(parent,Dialog.ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
		setSize(800,600);
		this.group=group;
		setTitle("Menu grupy");
		
		if(this.group==null) {
			String name = JOptionPane.showInputDialog("Podaj nazwê grupy");
			GroupType type = (GroupType) JOptionPane.showInputDialog(this, 
					"Podaj typ kolekcji",
					"Typ kolekcji",
					JOptionPane.QUESTION_MESSAGE,
					null,
					GroupType.values(),
					GroupType.ARRAY_LIST
					);
			setTitle("Nowa kolekcja");
			try {
				this.group = new GroupOfPokemon (type,name);
			}catch (PokemonException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			}
		}else {
			setTitle("Edytowanie istniej¹cej kolekcji");
		}
		
		typeName.addActionListener(this);
		deleteElement.addActionListener(this);
		newElement.addActionListener(this);
		changeElement.addActionListener(this);
		groupName.addActionListener(this);
		saveButton.addActionListener(this);
		loadButton.addActionListener(this);
		binarySaveButton.addActionListener(this);
		binaryLoadButton.addActionListener(this);
		sortName.addActionListener(this);
		sortNumber.addActionListener(this);
		sortMainType.addActionListener(this);
		sortSecondType.addActionListener(this);
		viewCol=new ViewPokemonCollection(this.group,700,300);
		viewCol.refreshView();
		
		fieldName.setText(this.group.getCollectionName());
		fieldType.setText(this.group.getCollectionType().toString());
		
		JPanel panel = new JPanel();
		
		panel.setBackground(Color.RED);
		
		panel.add(viewCol);
		panel.add(labelName);
		panel.add(fieldName);
		panel.add(labelType);
		panel.add(fieldType);
		panel.add(newElement);
		panel.add(changeElement);
		panel.add(groupName);
		panel.add(typeName);
		panel.add(deleteElement);
		panel.add(saveButton);
		panel.add(loadButton);
		panel.add(binarySaveButton);
		panel.add(binaryLoadButton);
		panel.add(sortName);
		panel.add(sortNumber);
		panel.add(sortMainType);
		panel.add(sortSecondType);
		setContentPane(panel);

		fieldName.setEditable(false);
		fieldType.setEditable(false);
		
		setVisible(true);
		viewCol.refreshView();
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		try {
			if(source==newElement) {
				Pokemon newPokemon = PokemonWindowDialog.createNewPokemon(this);
				if(newPokemon!= null) {
					group.add(newPokemon);
					viewCol.refreshView();
				}
			}
			
			if(source==changeElement) {
				int index = viewCol.getSelectedIndex();
				if(index>=0) {
					Iterator<Pokemon> iterator = group.iterator();
					while (index --> 0) {
					iterator.next();
					PokemonWindowDialog.changePokemonData(this, iterator.next());
					viewCol.refreshView();
					}
				}
			}
			
			if(source==deleteElement) {
				int index = viewCol.getSelectedIndex();
				if(index>=0) {
					Iterator<Pokemon> iterator = group.iterator();
					while (index --> 0) {
					iterator.next();
					iterator.remove();
					viewCol.refreshView();
					}
				}
			}
			
			if(source==groupName) {
				String name = JOptionPane.showInputDialog(this,"Podaj nazwê grupy");
				try {
				group.setCollectionName(name);
				fieldName.setText(name);
				}catch(PokemonException e) {
					JOptionPane.showMessageDialog(this, e.getMessage(),"B³¹d", JOptionPane.ERROR_MESSAGE);
				}
				viewCol.refreshView();
			}
			
			if(source==typeName) {
				GroupType type = (GroupType) JOptionPane.showInputDialog(this, 
						"Podaj typ kolekcji",
						"Typ kolekcji",
						JOptionPane.QUESTION_MESSAGE,
						null,
						GroupType.values(),
						GroupType.ARRAY_LIST
						);
				group.setCollectionType(type);
				fieldType.setText(type.toString());
				viewCol.refreshView();
			}
			
			if(source == saveButton) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
				int index = viewCol.getSelectedIndex();
				if(index>=0) {
					Iterator<Pokemon> iterator = group.iterator();
					while (index --> 0) {
					iterator.next();
					Pokemon.printToFile(fileName, iterator.next());
					viewCol.refreshView();
					}
				}
			}
			
			if(source == binarySaveButton) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
				int index = viewCol.getSelectedIndex();
				if(index>=0) {
					Iterator<Pokemon> iterator = group.iterator();
					while (index --> 0) {
					iterator.next();
					Pokemon.writeObject(fileName, iterator.next());
					viewCol.refreshView();
					}
				}
			}
			
			if(source == loadButton) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
					Pokemon pokemon = Pokemon.readFromFile(fileName);
					group.add(pokemon);
					viewCol.refreshView();
			}
			
			if(source == binaryLoadButton) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
				Pokemon pokemon = Pokemon.readObject(fileName);
				group.add(pokemon);
				viewCol.refreshView();
			}
			
			if(source == sortName) {
				group.sortCollectionName();
				viewCol.refreshView();
			}
			
			if(source == sortNumber) {
				group.sortPokemonNumber();
				viewCol.refreshView();
			}

			if(source == sortMainType) {
				group.sortMainType();
				viewCol.refreshView();
			}
			
			if(source == sortSecondType) {
				group.sortSecondaryType();
				viewCol.refreshView();
			}
			
		}catch (PokemonException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		viewCol.refreshView();
	}
	
	public static GroupOfPokemon createNewGroupOfPokemon(Window parent) {
		GroupOfPokemonWindowDialog dialog = new GroupOfPokemonWindowDialog(parent, null);
		return dialog.group;
	}
}

class ViewPokemonCollection extends JScrollPane{
	private static final long serialVersionUID = 1L;
	private GroupOfPokemon group;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public ViewPokemonCollection ( GroupOfPokemon group,int width,int height) {
		this.group=group;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Kolekcja: "));
		String[] tableHeader = {"Nazwa Pokemona", "Typ g³ówny", "Drugi typ","Numer Pokemona"};
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
		if(group == null)
			return;
		for(Pokemon pokemon : group ) {
			if(pokemon!=null) {
				String[]row = {pokemon.getPokemonName(),""+pokemon.getMainType().toString(),""+pokemon.getSecondType().toString(),""+pokemon.getPokemonNumber()};
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


