import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Objects;

import javax.swing.JFileChooser;

/*
 * Program: Klasa s³u¿¹ca do obs³ugi modelu danych Pokemonów, zawiera wszystkie funkcje potrzebne
 * 			do kustomizacji Pokemona lub zapisu jego danych do albo z pliku.
 *    Plik: Pokemon.java
 *          
 *   Autor: Damian Bednarz 241283
 *    Data: listopad 2018 r.
 *	
 */


enum PokemonType{
	NORMAL("Normalny"), 
	FIRE("Ognisty"), 
	WATER("Wodny"), 
	GRASS("Trawiasty"), 
	ELECTRIC("Elektryczny"), 
	ICE("Lodowy"),
	FIGHTING("Walcz¹cy"),
	POISON("Truj¹cy"),
	PSYCHIC("Psychiczny"),
	GHOST("Duch"),
	DARK("Mroczny"),
	GROUND("Ziemny"),
	ROCK("Skalny"),
	FLYING("Lataj¹cy"),
	BUG("Robaczy"),
	DRAGON("Smoczy"),
	STEEL("Stalowy"),
	FAIRY("Wró¿ka"),
	UNKNOWN("-------");

	String pokemonType;

	private PokemonType(String pokemon_type) {
		pokemonType = pokemon_type;
	}

	
	@Override
	public String toString() {
		return pokemonType;
	}
}
class PokemonException extends Exception{
	private static final long serialVersionUID = 1L;

	public PokemonException(String message) {
		super(message);
	}
}
public class Pokemon implements Serializable,Comparable <Pokemon> {

	private static final long serialVersionUID = 1L;
	private String pokemonName;
	private PokemonType mainType;
	private PokemonType secondType;
	private int pokemonNumber;
	
	public Pokemon (String pokemon_name) throws PokemonException{
		setPokemonName(pokemon_name);
		mainType=PokemonType.UNKNOWN;
		secondType=PokemonType.UNKNOWN;
	}

	public String getPokemonName() {
		return pokemonName;
	}

	public void setPokemonName(String pokemonName) throws PokemonException {
		if((pokemonName==null) || pokemonName.equals("")) 
			throw new PokemonException("Pole <Nazwa Pokemona> musi byæ wype³nione.");
		this.pokemonName = pokemonName;
	}

	public PokemonType getMainType() {
		return mainType;
	}

	public void setMainType(PokemonType mainType) {
		this.mainType = mainType;
	}
	
	public void setMainType(String mainType) throws PokemonException {
		if((mainType==null) || mainType.equals("")) 
			this.mainType=PokemonType.UNKNOWN;
		for(PokemonType type : PokemonType.values()){
			if (type.pokemonType.equals(mainType)) {
				this.mainType = type;
				return;
			}
		}
		throw new PokemonException("Nie ma takiego typu.");
	}
	public PokemonType getSecondType() {
		return secondType;
	}

	public void setSecondType(PokemonType secondType) {
		this.secondType = secondType;
	}
	
	public void setSecondType(String secondType) throws PokemonException {
		if((secondType==null) || secondType.equals("")) 
			this.secondType=PokemonType.UNKNOWN;
		for(PokemonType type : PokemonType.values()){
			if (type.pokemonType.equals(secondType)) {
				this.secondType = type;
				return;
			}
		}
		throw new PokemonException("Nie ma takiego typu.");
	}

	public int getPokemonNumber() {
		return pokemonNumber;
	}

	public void setPokemonNumber(int pokemonNumber) {
		this.pokemonNumber = pokemonNumber;
	}
	
	public void setPokemonNumber(String pokemonNumber) throws PokemonException {
		if((pokemonNumber==null) || pokemonNumber.equals("")) {
			setPokemonNumber(0);
			return;
		}
		try { 
			setPokemonNumber(Integer.parseInt(pokemonNumber));
		}
		catch (NumberFormatException e){
			throw new PokemonException ("Numer Pokemona musi byæ liczb¹ ca³kowit¹.");
		}
	}
	
	public static void printToFile(PrintWriter writer, Pokemon pokemon){
		writer.println(pokemon.pokemonName + "#" + pokemon.mainType + 
				"#" + pokemon.secondType + "#" + pokemon.pokemonNumber);
	}
	
	public static void printToFile(String file_name, Pokemon pokemon) throws PokemonException {
		try (PrintWriter writer = new PrintWriter(file_name)) {
			printToFile(writer, pokemon);
		} catch (FileNotFoundException e){
			throw new PokemonException("Nie odnaleziono pliku " + file_name);
		}
	}
	
	public static Pokemon readFromFile(BufferedReader reader) throws PokemonException{
		try {
			String line = reader.readLine();
			String[] txt = line.split("#");
			Pokemon pokemon = new Pokemon(txt[0]);
			pokemon.setMainType(txt[1]);
			pokemon.setSecondType(txt[2]);
			pokemon.setPokemonNumber(txt[3]);	
			return pokemon;
		} catch(IOException e){
			throw new PokemonException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}	
	}
	
	public static Pokemon readFromFile(String file_name) throws PokemonException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
			return Pokemon.readFromFile(reader);
		} catch (FileNotFoundException e){
			throw new PokemonException("Nie odnaleziono pliku " + file_name);
		} catch(IOException e){
			throw new PokemonException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}	
	}

	public static void writeObject (String fileName,Pokemon pokemon) throws PokemonException{
			try (FileOutputStream fos = new FileOutputStream(fileName);) {
				ObjectOutputStream oos = new ObjectOutputStream(fos);
	            writeObject(oos,pokemon);
			}catch(FileNotFoundException e) {
				throw new PokemonException("Nie znaleziono pliku");
			}catch(IOException e) {
				throw new PokemonException("B³¹d zapisu do pliku");
			}

		}

	public static void writeObject (ObjectOutputStream output, Pokemon pokemon) throws PokemonException{
			try{
				output.writeObject(pokemon);
			}catch(IOException e){
				throw new PokemonException("B³¹d zapisu do pliku");
			}
		}
	
	public static Pokemon readObject(ObjectInputStream ois) throws PokemonException{
		try {
			Pokemon pokemon = (Pokemon) ois.readObject();	
			return pokemon;
		} catch(IOException e){
			throw new PokemonException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}catch(ClassNotFoundException e){
			throw new PokemonException ("Nie znaleziono klasy");
		}
	}
	
	public static Pokemon readObject(String file_name) throws PokemonException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(file_name)))) {
			return Pokemon.readObject(ois);
		} catch (FileNotFoundException e){
			throw new PokemonException("Nie odnaleziono pliku " + file_name);
		} catch(IOException e){
			throw new PokemonException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}	
	}
	
	public static String chooseSaveFile (Window parent) {
		JFileChooser chooser = new JFileChooser();
		int retVal = chooser.showSaveDialog(parent);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			String fileName = chooser.getSelectedFile().getPath();
			return fileName;
		}
		return "";
	}
	
	public static String chooseLoadFile (Window parent) {
		JFileChooser chooser = new JFileChooser();
		int retVal = chooser.showOpenDialog(parent);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			String fileName = chooser.getSelectedFile().getPath();
			return fileName;
		}
		return "";
	}
	
	@Override
	public int compareTo (Pokemon pokemon) {
		return String.CASE_INSENSITIVE_ORDER.compare(this.getPokemonName(), pokemon.getPokemonName());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this.hashCode()==o.hashCode()) {
			return true;
		}else
			return false;
	}
	
	@Override
	public String toString() {
		return (pokemonName+" numer: "+pokemonNumber+mainType.toString()+secondType.toString());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.pokemonName,this.pokemonNumber,this.mainType,this.secondType);
	}
}
