import java.util.List;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Vector;
import java.util.HashSet;
import java.util.Iterator;


/*
 * Program: Klasa s³u¿¹ca do obs³ugi grup Pokemonów, zawiera wszystkie funkcje potrzebne
 * 			do kustomizacji grupy lub zapisu jego danych do albo z pliku.
 *    Plik: GroupOfPokemon.java
 *          
 *   Autor: Damian Bednarz 241283
 *    Data: listopad 2018 r.
 *	
 */

enum GroupType{
		VECTOR ("Lista (Vector)"),
		ARRAY_LIST("Lista (Array list)"),
		HASH_SET("Lista (Hash set)"),
		TREE_SET("Zbiór (Tree set)"),
		LINKED_LIST("Zbiór (Linked list)");
	
		String type_Name;
		
		private GroupType(String typeName) {
			this.type_Name=typeName;
		}
		
		@Override
		public String toString() {
			return type_Name;
		}
		
		public static GroupType find (String type) {
			for(GroupType typeName : values()) {
				if(typeName.type_Name.equals(type))
					return typeName;
			}
			return null;
		}
		
		public Collection<Pokemon> createCollection () throws PokemonException {
			switch(this) {
			case VECTOR: return new Vector<Pokemon>(); 
			case ARRAY_LIST: return new ArrayList<Pokemon>();
			case HASH_SET: return new HashSet<Pokemon>();
			case TREE_SET: return new TreeSet<Pokemon>();
			case LINKED_LIST: return new LinkedList<Pokemon>();
			default: throw new PokemonException ("Brak takiego typu w bazie");
			}
		}
		
}

public class GroupOfPokemon implements Iterable<Pokemon>,Serializable {
	private static final long serialVersionUID = 1L;
	private String collectionName;
	private GroupType collectionType;
	private Collection<Pokemon> collection;
	
	public GroupOfPokemon (String type_name, String name) throws PokemonException {
		setCollectionName(name);
		GroupType collectionType=GroupType.find(type_name);
		if(collectionType==null)
			throw new PokemonException("brak typu");
		this.collectionType=collectionType;
		collection=this.collectionType.createCollection();
	}
	
	public GroupOfPokemon (GroupType type, String name) throws PokemonException {
		setCollectionName(name);
		if(type==null)
			throw new PokemonException("brak typu");
		this.collectionType=type;
		collection=this.collectionType.createCollection();
	}
	
	public String getCollectionName () {
		return collectionName;
	}
	
	public void setCollectionName (String name) throws PokemonException {
		if(name==null || name.equals("")) {
			throw new PokemonException("Nazwa grupy nieokreœlona");
		}
		this.collectionName=name;
	}

	public GroupType getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(GroupType type) throws PokemonException {
		if(type==null)
			throw new PokemonException ("Brak rodzaju kolekcji");
		if(this.collectionType==type)
			return;
		Collection<Pokemon> oldCollection = collection;
		collection = type.createCollection();
		this.collectionType=type;
		for(Pokemon pokemon : oldCollection) {
			collection.add(pokemon);
		}
	}
	
	public void setCollectionType (String type_name) throws PokemonException {
		for(GroupType type : GroupType.values()) {
			if(type.toString().equals(type_name)) {
				setCollectionType(type_name);
				return;
			}
		}
		throw new PokemonException ("Nie rozpoznano typu kolekcji");
	}
	
	public boolean add (Pokemon e) {
		return collection.add(e);
	}
	
	public Iterator<Pokemon> iterator () {
		return collection.iterator();
	}
	
	public int size () {
		return collection.size();
	}
	
	public void sortCollectionName () throws PokemonException {
		if(collectionType==GroupType.HASH_SET || collectionType==GroupType.TREE_SET)
			throw new PokemonException ("nie mozna sortowac kolekcji SET");
		Collections.sort((List<Pokemon>)collection);
	}
	
	public void sortPokemonNumber () throws PokemonException {
		if(collectionType==GroupType.HASH_SET || collectionType==GroupType.TREE_SET)
			throw new PokemonException ("nie mozna sortowac kolekcji SET");
		Collections.sort((List<Pokemon>)collection,new Comparator<Pokemon>() {
			@Override
			public int compare (Pokemon p1, Pokemon p2) {
				if(p1.getPokemonNumber()>p2.getPokemonNumber())
					return 1;
				if(p1.getPokemonNumber()<p2.getPokemonNumber())
					return -1;
				return 0;
			}
		});
	}
	
	public void sortMainType () throws PokemonException {
		if(collectionType==GroupType.HASH_SET || collectionType==GroupType.TREE_SET)
			throw new PokemonException ("nie mozna sortowac kolekcji SET");
		Collections.sort((List<Pokemon>)collection,new Comparator<Pokemon>() {
			@Override
			public int compare (Pokemon p1, Pokemon p2) {
				return p1.getMainType().toString().compareTo(p2.getMainType().toString());
			}
		});
	}
	
	public void sortSecondaryType () throws PokemonException {
		if(collectionType==GroupType.HASH_SET || collectionType==GroupType.TREE_SET)
			throw new PokemonException ("nie mozna sortowac kolekcji SET");
		Collections.sort((List<Pokemon>)collection,new Comparator<Pokemon>() {
			@Override
			public int compare (Pokemon p1, Pokemon p2) {
				return p1.getSecondType().toString().compareTo(p2.getSecondType().toString());
			}
		});
	}
	
	@Override
	public String toString() {
		return collectionName + "  [" + collectionType + "]";
	}

	public static void printToFile (PrintWriter writer, GroupOfPokemon group) {
		writer.println(group.getCollectionName());
		writer.println(group.getCollectionType());
		for(Pokemon pokemon : group.collection)
			Pokemon.printToFile(writer, pokemon);
	}
	
	public static void printToFile (String file_name, GroupOfPokemon group) throws PokemonException {
		try(PrintWriter writer = new PrintWriter(file_name)){
			printToFile(writer, group);
		}catch (FileNotFoundException e) {
			throw new PokemonException ("Nie znaleziono pliku");
		}
	}
	
	public static GroupOfPokemon readFromFile (BufferedReader reader) throws PokemonException {
		try {
			String collectionName=reader.readLine();
			String collectionType=reader.readLine();
			GroupOfPokemon groupOfPokemon = new GroupOfPokemon (collectionType, collectionName);
			Pokemon pokemon; 
			
			while((pokemon = Pokemon.readFromFile(reader))!=null) {
				groupOfPokemon.collection.add(pokemon);
			}
			return groupOfPokemon;
		}catch(IOException e) {
			throw new PokemonException ("b³¹d odczytu z pliku");
		}
	}
	
	public static GroupOfPokemon readFromFile (String fileName) throws PokemonException {
		try(BufferedReader reader = new BufferedReader (new FileReader(new File(fileName)))){
			return GroupOfPokemon.readFromFile(reader);
		}catch(FileNotFoundException e) {
			throw new PokemonException ("nie znaleziono pliku");
		}catch(IOException e) {
			throw new PokemonException ("b³¹d odczytu z pliku");
		}
				
	}
	
	public static GroupOfPokemon createGroupUnion(GroupOfPokemon g1,GroupOfPokemon g2) throws PokemonException {
		String name = "(" + g1.collectionName + " OR " + g2.collectionName +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.collectionType;
		} else {
			type = g1.collectionType;
		}
		GroupOfPokemon group = new GroupOfPokemon(type, name);
		group.collection.addAll(g1.collection);
		group.collection.addAll(g2.collection);
		return group;
	}
	
	public static GroupOfPokemon createGroupIntersection(GroupOfPokemon g1,GroupOfPokemon g2) throws PokemonException {
		String name = "(" + g1.collectionName + " AND " + g2.collectionName +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.collectionType;
		} else {
			type = g1.collectionType;
		}
		GroupOfPokemon group = new GroupOfPokemon(type, name);
		
		for(Pokemon pokemon : g1) {
			if(g2.collection.contains(pokemon)) {
				group.add(pokemon);
			}
		}
		
		return group;
	}
	
	public static GroupOfPokemon createGroupDifference(GroupOfPokemon g1,GroupOfPokemon g2) throws PokemonException {
		String name = "(" + g1.collectionName + " SUB " + g2.collectionName +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.collectionType;
		} else {
			type = g1.collectionType;
		}
		GroupOfPokemon group = new GroupOfPokemon(type, name);
		
		for(Pokemon pokemon : g1) {
			if(!(g2.collection.contains(pokemon))) {
				group.add(pokemon);
			}
		}
		
		return group;
	}
	
	public static GroupOfPokemon createGroupSymmetricDiff(GroupOfPokemon g1,GroupOfPokemon g2) throws PokemonException {
		String name = "(" + g1.collectionName + " XOR " + g2.collectionName +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.collectionType;
		} else {
			type = g1.collectionType;
		}
		GroupOfPokemon group = new GroupOfPokemon(type, name);
		
		for(Pokemon pokemon : g1) {
			if(!(g2.collection.contains(pokemon))) {
				group.add(pokemon);
			}
		}
		
		for(Pokemon pokemon : g2) {
			if(!(g1.collection.contains(pokemon))) {
				if(!(group.collection.contains(pokemon)));
				group.add(pokemon);
			}
		}
		return group;
	}

}
