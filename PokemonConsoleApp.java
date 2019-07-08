import java.util.Arrays;

/*
 * Program: Klasa obs³uguje program okienkowy, tworzy pêtlê g³ówn¹ programu, umo¿liwia dodawanie Pokemonów
 * 			i u¿ycie funkcji takich jak wczytanie i zapisanie danych do pliku.
 * 
 *    Plik: PokemonConsoleApp.java
 *          
 *   Autor: Damian Bednarz 241283
 *   
 *    Data: pazdziernik 2018 r.
 *	
 */

public class PokemonConsoleApp {

	private static final String GREETING_MESSAGE = 
			"Program Baza Pokemonów - wersja konsolowa\n" + 
	        "Autor: Damian Bednarz\n" + 
			"Data:  paŸdziernik 2018 r.\n";

	private static final String MENU = 
			"    M E N U   G £ Ó W N E  	\n" +
			"1 - Wprowadz nowego pokemona 	\n" +
			"2 - Usuñ pokemona        		\n" +
			"3 - Modyfikuj pokemona  		\n" +
			"4 - Wczytaj pokemony z pliku   \n" +
			"5 - Zapisz pokemony do pliku   \n" +
			"6 - Zapisz binarnie do pliku   \n" +
			"7 - Wczytaj binarnie z pliku   \n" +
			"0 - Zakoñcz program        	\n";	
	
	private static final String CHANGE_MENU = 
			"   Co zmieniæ?	     \n" + 
	        "1 - Nazwê           \n" + 
			"2 - G³ówny typ      \n" + 
	        "3 - Drugi typ 		 \n" + 
			"4 - Numer           \n" +
	        "0 - Powrót do menu g³ównego\n";

	private static ConsoleUserDialog UI = new ConsoleUserDialog();
	
	
	public static void main(String[] args) {
		PokemonConsoleApp application = new PokemonConsoleApp();
		application.runMainLoop();
	}
		
		private Pokemon currentPokemon = null;
		
		public void runMainLoop() {
			UI.printMessage(GREETING_MESSAGE);

			while (true) {
				UI.clearConsole();
				showCurrentPokemon();

				try {
					switch (UI.enterInt(MENU + "==>> ")) {
					case 1:
						currentPokemon = createNewPokemon();
						break;
					case 2:
						currentPokemon = null;
						UI.printInfoMessage("Wpis o pokemonie zosta³ usuniêty");
						break;
					case 3:
						if (currentPokemon == null) throw new PokemonException("B³¹d - Pokemon nie zosta³ utworzony");
						changePokemonData(currentPokemon);
						break;
					case 4: {
						String file_name = UI.enterString("Podaj nazwê pliku: ");
						currentPokemon = Pokemon.readFromFile(file_name);
						UI.printInfoMessage("Dane Pokemona zosta³y wczytane z pliku" + file_name);
					}
						break;
					case 5: {
						String file_name = UI.enterString("Podaj nazwê pliku: ");
						Pokemon.printToFile(file_name, currentPokemon);
						UI.printInfoMessage("Dane pokemona zosta³y zapisane do pliku " + file_name);
					}
						break;
					case 6: {
						String file_name = UI.enterString("Podaj nazwê pliku: ");
						Pokemon.writeObject(file_name, currentPokemon);
						UI.printInfoMessage("Dane pokemona zosta³y zapisane do pliku " + file_name);
					}
						break;
					case 7: {
						String file_name = UI.enterString("Podaj nazwê pliku: ");
						currentPokemon = Pokemon.readObject(file_name);
						UI.printInfoMessage("Dane pokemona zosta³y zapisane do pliku " + file_name);
					}
						break;
					case 0:
						UI.printInfoMessage("\nProgram zakoñczy³ dzia³anie!");
						System.exit(0);
					}
				} catch (PokemonException e) { 
					UI.printErrorMessage(e.getMessage());
				}
			}
		}
		
		void showCurrentPokemon() {
			showPokemon(currentPokemon);
		} 
		
		static void showPokemon(Pokemon pokemon) {
			StringBuilder sb = new StringBuilder();
			
			if (pokemon != null) {
				sb.append("Aktualna osoba: \n");
				sb.append( "      Nazwa: " + pokemon.getPokemonName() + "\n" );
				sb.append( "  Typ g³ówny:" + pokemon.getMainType() + "\n" );
				sb.append( "  Drugi typ: " + pokemon.getSecondType() + "\n" );
				sb.append( "Numer: 		 " + pokemon.getPokemonNumber() + "\n");
			} else
				sb.append( "Brak danych pokemona" + "\n" );
			UI.printMessage( sb.toString() );
		}
		
		static Pokemon createNewPokemon(){
			String name = UI.enterString("Podaj nazwê: ");
			String pokemon_number = UI.enterString("Podaj numer pokemona: ");
			UI.printMessage("Dozwolone typy:" + Arrays.deepToString(PokemonType.values()));
			String main_type = UI.enterString("Podaj g³ówny typ: ");
			String second_type = UI.enterString("Podaj drugi typ: ");
			Pokemon pokemon;
			try { 
				pokemon = new Pokemon(name);
				pokemon.setMainType(main_type);
				pokemon.setSecondType(second_type);
				pokemon.setPokemonNumber(pokemon_number);
			} catch (PokemonException e) {    
				UI.printErrorMessage(e.getMessage());
				return null;
			}
			return pokemon;
		}
		
		static void changePokemonData(Pokemon pokemon)
		{
			while (true) {
				UI.clearConsole();
				showPokemon(pokemon);

				try {		
					switch (UI.enterInt(CHANGE_MENU + "==>> ")) {
					case 1:
						pokemon.setPokemonName(UI.enterString("Podaj nazwê: "));
						break;
					case 2:
						UI.printMessage("Dozwolone typy: " + Arrays.deepToString(PokemonType.values()));
						pokemon.setMainType(UI.enterString("Podaj g³ówny typ: "));
						break;
					case 3:
						UI.printMessage("Dozwolone typy: " + Arrays.deepToString(PokemonType.values()));
						pokemon.setSecondType(UI.enterString("Podaj podaj drugi typ: "));
						break;
					case 4:
						pokemon.setPokemonNumber(UI.enterString("Podaj numer pokemona: "));
						break;
					
					case 0: return;
					}
				} catch (PokemonException e) {     
					UI.printErrorMessage(e.getMessage());
				}
			}
		}
	} 