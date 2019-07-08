import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Program: Aplikacja okienkowa z GUI, która umo¿liwia testowanie 
 *          operacji wykonywanych na obiektach klasy Pokemon.
 *    Plik: PokemonWindowApp.java
 *          
 *   Autor: Damian Bednarz 241283
 *    Data: pazdziernik 2018 r.
 */

public class PokemonWindowApp extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private static final String GREETING_MESSAGE = 
			"Program Pokemon - wersja okienkowa\n" + 
	        "Autor: Damian Bednarz\n" + 
			"Data:  paŸdziernik 2018 r.\n";


	public static void main (String[] args) {
		new PokemonWindowApp();
	}
	
	private Pokemon currentPokemon;
	
	Font font = new Font("MonoSpaced", Font.BOLD, 12);
	
	JLabel pokemonNameLabel	 	= new JLabel ("Nazwa Pokemona: ");
	JLabel mainTypeLabel 		= new JLabel ("	   Typ g³ówny: ");
	JLabel secondTypeLabel 	= new JLabel ("		Drugi typ: ");
	JLabel pokemonNumberLabel 	= new JLabel ("Numer pokemona: ");
	
	JTextField pokemonNameField = new JTextField(10);
	JTextField pokemonNumberField = new JTextField(10);
	JTextField mainTypeField = new JTextField(10);
	JTextField secondTypeField = new JTextField(10);
	
	JButton newButton    = new JButton("Nowy pokemon");
	JButton editButton   = new JButton("Zmieñ parametry");
	JButton saveButton   = new JButton("Zapisz do pliku");
	JButton loadButton   = new JButton("Wczytaj z pliku");
	JButton binarySaveButton   = new JButton("Zapisz binarnie do pliku");
	JButton binaryLoadButton   = new JButton("Wczytaj binarnie z pliku");
	JButton chooseSaveButton   = new JButton("Zapisz do pliku z mo¿liwoœci¹ wyboru");
	JButton chooseLoadButton   = new JButton("Wczytaj z pliku z mo¿liwoœci¹ wyboru");
	JButton deleteButton = new JButton("Usuñ pokemona");
	JButton infoButton   = new JButton("O programie");
	JButton exitButton   = new JButton("Zakoñcz aplikacjê");

	JMenuBar menu = new JMenuBar ();
	
	JMenu file = new JMenu("Plik");
	JMenu edit = new JMenu("Edytuj");
	
	JMenuItem newItem = new JMenuItem("Nowy pokemon");
	JMenuItem editItem = new JMenuItem("Edytuj pokemona");
	JMenuItem saveItem = new JMenuItem("Zapisz pokemona");
	JMenuItem loadItem = new JMenuItem("Wczytaj pokemona");
	JMenuItem binarySaveItem = new JMenuItem("Zapisz binarnie pokemona");
	JMenuItem binaryLoadItem = new JMenuItem("Wczytaj binarnie pokemona");
	JMenuItem chooseSaveItem = new JMenuItem("Wybierz folder do zapisu");
	JMenuItem chooseLoadItem = new JMenuItem("Wybierz folder do wczytania");
	JMenuItem deleteItem = new JMenuItem("Usuñ pokemona");
	JMenuItem infoItem = new JMenuItem("Informacje o autorze");
	JMenuItem exitItem = new JMenuItem("Wyjdz z programu");
	
	public PokemonWindowApp() {
		setTitle("PokemonWindowApp");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500,300);
		setResizable(false);
		setLocationRelativeTo(null);
		
		pokemonNameLabel.setFont(font);
		mainTypeLabel.setFont(font);
		secondTypeLabel.setFont(font);
		pokemonNumberLabel.setFont(font);
		
		pokemonNameField.setEditable(false);
		mainTypeField.setEditable(false);
		secondTypeField.setEditable(false);
		pokemonNumberField.setEditable(false);
		
		newButton.addActionListener(this);
		editButton.addActionListener(this);
		saveButton.addActionListener(this);
		loadButton.addActionListener(this);
		binaryLoadButton.addActionListener(this);
		binarySaveButton.addActionListener(this);
		chooseLoadButton.addActionListener(this);
		chooseSaveButton.addActionListener(this);
		deleteButton.addActionListener(this);
		infoButton.addActionListener(this);
		exitButton.addActionListener(this);
		
		newItem.addActionListener(this);
		editItem.addActionListener(this);
		saveItem.addActionListener(this);
		loadItem.addActionListener(this);
		binaryLoadItem.addActionListener(this);
		binarySaveItem.addActionListener(this);
		chooseLoadItem.addActionListener(this);
		chooseSaveItem.addActionListener(this);
		deleteItem.addActionListener(this);
		infoItem.addActionListener(this);
		exitItem.addActionListener(this);
		
		JPanel panel = new JPanel();
		
		panel.add(pokemonNameLabel);
		panel.add(pokemonNameField);
		
		panel.add(mainTypeLabel);
		panel.add(mainTypeField);
		
		panel.add(secondTypeLabel);
		panel.add(secondTypeField);
		
		panel.add(pokemonNumberLabel);
		panel.add(pokemonNumberField);

		panel.add(newButton);
		panel.add(deleteButton);
		panel.add(saveButton);
		panel.add(loadButton);
		panel.add(editButton);
		panel.add(binaryLoadButton);
		panel.add(binarySaveButton);
		panel.add(chooseLoadButton);
		panel.add(chooseSaveButton);
		panel.add(infoButton);
		panel.add(exitButton);
		
		menu.add(file);
		menu.add(edit);
		
		file.add(loadItem);
		file.add(saveItem);
		file.add(binaryLoadItem);
		file.add(binarySaveItem);
		file.add(chooseLoadItem);
		file.add(chooseSaveItem);
		edit.add(newItem);
		edit.add(deleteItem);
		edit.add(editItem);
		edit.add(infoItem);
		edit.add(exitItem);
		
		setContentPane(panel);
		setJMenuBar(menu);
		
		showCurrentPokemon();
		
		setVisible(true);
		
	}

	void showCurrentPokemon() {
		if(currentPokemon==null) {
			pokemonNameField.setText("");
			mainTypeField.setText("");
			secondTypeField.setText("");
			pokemonNumberField.setText("");
		}else {
			pokemonNameField.setText(currentPokemon.getPokemonName());
			mainTypeField.setText(""+currentPokemon.getMainType());
			secondTypeField.setText(""+currentPokemon.getSecondType());
			pokemonNumberField.setText(""+currentPokemon.getPokemonNumber());
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object eventSource = event.getSource();
		try {
			if(eventSource == newButton || eventSource == newItem) {
				currentPokemon = PokemonWindowDialog.createNewPokemon(this);
			}
			if(eventSource == deleteButton || eventSource == deleteItem) {
				currentPokemon=null;
			}
			if(eventSource == saveButton || eventSource == saveItem) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
				Pokemon.printToFile(fileName, currentPokemon);
			}
			if(eventSource == loadButton || eventSource == loadItem) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
				currentPokemon=Pokemon.readFromFile(fileName);
			}
			if(eventSource == binarySaveButton || eventSource == binarySaveItem) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
				Pokemon.writeObject(fileName,currentPokemon);
			}
			if(eventSource == binaryLoadButton || eventSource == binaryLoadItem) {
				String fileName = JOptionPane.showInputDialog("Podaj nazwê pliku");
				if (fileName == null || fileName.equals("")) return;
				currentPokemon=Pokemon.readObject(fileName);
			}
			if(eventSource == chooseSaveButton || eventSource == chooseSaveItem) {
				Pokemon.printToFile(Pokemon.chooseSaveFile(this), currentPokemon);
			}
			if(eventSource == chooseLoadButton || eventSource == chooseLoadItem) {
				currentPokemon=Pokemon.readFromFile(Pokemon.chooseLoadFile(this));
			}
			if(eventSource == editButton || eventSource == editItem) {
				if(currentPokemon == null) throw new PokemonException ("Pokemon nie zosta³ utworzony");
				PokemonWindowDialog.changePokemonData (this, currentPokemon);
			}
			if(eventSource == infoButton || eventSource == infoItem) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
			if (eventSource == exitButton || eventSource == exitItem) {
				System.exit(0);
			}
		}catch(PokemonException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		
		showCurrentPokemon();
	}
	

}