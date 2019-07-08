import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Program: Aplikacja okienkowa z GUI, która umo¿liwia testowanie 
 *          operacji wykonywanych na obiektach klasy Pokemon.
 *    Plik: PokemonWindowDialog.java
 *          
 *   Autor: Damian Bednarz 241283
 *    Data: pazdziernik 2018 r.
 *
 */

public class PokemonWindowDialog extends JDialog implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private Pokemon pokemon;
	
	Font font = new Font("MonoSpaced", Font.BOLD, 12);

	JLabel pokemonNameLabel	 	= new JLabel ("Nazwa Pokemona: ");
	JLabel mainTypeLabel 		= new JLabel ("Typ g³ówny: 	   ");
	JLabel secondTypeLabel 		= new JLabel ("Drugi typ: 	   ");
	JLabel pokemonNumberLabel 	= new JLabel ("Numer pokemona: ");
	
	JTextField pokemonNameField = new JTextField(10);
	JTextField pokemonNumberField = new JTextField(11);
	JComboBox<PokemonType> pokemonTypeBox1 = new JComboBox<PokemonType>(PokemonType.values());
	JComboBox<PokemonType> pokemonTypeBox2 = new JComboBox<PokemonType>(PokemonType.values());
	
	JButton OKButton = new JButton("  OK  ");
	JButton CancelButton = new JButton ("Anuluj");
	
	private PokemonWindowDialog (Window parent, Pokemon pokemon) {
		super(parent,Dialog.ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(200,300);
		setLocationRelativeTo(parent);
		this.pokemon=pokemon;
		if(pokemon==null) {
			setTitle("Nowy pokemon");
		}else {
			setTitle(pokemon.toString());
			pokemonNameField.setText(pokemon.getPokemonName());
			pokemonTypeBox1.setSelectedItem(pokemon.getMainType());
			pokemonTypeBox2.setSelectedItem(pokemon.getSecondType());
			pokemonNumberField.setText(""+pokemon.getPokemonNumber());
		}
		OKButton.addActionListener( this );
		CancelButton.addActionListener( this );
		
		JPanel panel = new JPanel();
		
		panel.setBackground(Color.RED);
		
		panel.add(pokemonNameLabel);
		panel.add(pokemonNameField);
		
		panel.add(pokemonNumberLabel);
		panel.add(pokemonNumberField);
		
		panel.add(mainTypeLabel);
		panel.add(pokemonTypeBox1);
		
		panel.add(secondTypeLabel);
		panel.add(pokemonTypeBox2);
		
		panel.add(OKButton);
		panel.add(CancelButton);
		
		setContentPane(panel);
		
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		Object source = event.getSource();
		
		if(source == OKButton) {
			try {
				if (pokemon==null) {
					pokemon=new Pokemon(pokemonNameField.getText());
				}else {
					pokemon.setPokemonName(pokemonNameField.getText());
				}
				pokemon.setMainType((PokemonType)pokemonTypeBox1.getSelectedItem());
				pokemon.setSecondType((PokemonType)pokemonTypeBox2.getSelectedItem());
				pokemon.setPokemonNumber(pokemonNumberField.getText());
				
				dispose();
			}catch(PokemonException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if(source == CancelButton) {
			dispose();
		}
	}

	public static Pokemon createNewPokemon(Window parent) {
		PokemonWindowDialog dialog = new PokemonWindowDialog(parent, null);
		return dialog.pokemon;
	}
	
	public static void changePokemonData(Window parent, Pokemon pokemon) {
		new PokemonWindowDialog(parent, pokemon);
	}

}
