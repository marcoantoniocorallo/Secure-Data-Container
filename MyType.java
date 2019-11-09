/**Classe di dato creata per utilizzare SecureDataContainer
 * Nella batteria di test viene utilizzata per emulare il comportamento di un oggetto di tipo String,
 * che in più garantisce la definizione del metodo Clone.
 * Per questo motivo la variabile d'istanza s è dichiarata come public, snellendo codice e lavoro.
 * La variable d'istanza key ha il ruolo di chiave per la funzione di cifratura.
 * Questa viene dichiarata final e inizializzata nella classe per questioni di semplicità,
 * ma nulla vieta di aggiungere un parametro al costruttore.
 **/

public class MyType extends MyCloneable{
	
	public String s;
	private final String key="XXX";
	
	public MyType (String t) {s = t;}
	
	@Override
	public String toString() {
		return s;
	}
	
	@Override
	public MyCloneable encrypt() {
		
		//Restituisce la stringa concatenata alla chiave.
		//Metodo di cifratura più che banale,
		//ma facilmente sostituibile da chi utilizza l'API
		return new MyType(s+key);
	}
	
	@Override
	public MyCloneable decrypt() {
		
		//Restituisce una sottostringa contenente la stringa originale,
		//eliminando il numero di caratteri della chiave.
		return new MyType(s.substring(0,s.length()-(key.length())));
	}
}
