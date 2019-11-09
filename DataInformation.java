import java.util.List;
import java.util.Vector;

@SuppressWarnings("unchecked") public class DataInformation<T extends MyCloneable>
			 implements Cloneable{
	
	private T info; //Contenuto dato
	private Vector<String> Authorized; //Lista utenti autorizzati
	
	public DataInformation(T i) throws IllegalArgumentException{
		if (i==null) throw new IllegalArgumentException("Errore durante l'allocazione");
		
		//Se la clone è definita, alloca e inizializza campo informazione
		try {
			info = (T) i.clone();
		}
		catch (CloneNotSupportedException e) {
			System.out.println(
				"Metodo clone non definito." +
						"Errore durante l'allocazione.");
		}
		
		Authorized = new Vector<>();
	}
	
	
	public T getInfo(){
		
		//Restituisce una copia del dato.
		//Se il metodo Clone non è definito, gestisce eccezione
		try {
			return (T) info.clone();
		} catch (CloneNotSupportedException e) {System.out.println("Non è sicuro restituire questo dato");}
		
		//Istruzione sintatticamente necessaria, ma non viene eseguita mai.
		return null;
	}
	
	public void setInfo(T newInfo) throws IllegalArgumentException {
		
		//Controllo sul nuovo dato
		if (newInfo==null) throw new IllegalArgumentException("Settaggio nullo.");
		
		//Se il metodo clone è definito, aggiorna campo informazione
		try {
			info = (T) newInfo.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Errore durante il settaggio di " + newInfo.toString());
		}
	}
	
	public List<String> getAuthorized(){
		
		//Restituisce una copia della lista originale
		//Altrimenti questa potrebbe essere modificata con i metodi della classe Vector
		return (List<String>) Authorized.clone();
	}
	
	public int AuthorizedSize(){return Authorized.size();}
	
	public void Addlicense(String Id) throws IllegalArgumentException{
	
		//Controllo stringa
		if (Id==null) throw new IllegalArgumentException("Condivisione utente fallita.\n Utente nullo.");
		Authorized.add(Id);
	}
	
	@Override
	public DataInformation<T> clone(){
		
		//Parametro info != null necessario
		DataInformation<T> nuovo = new DataInformation<T>(this.info);
		
		//Inserimento copia del valore informazione
		nuovo.info=getInfo();
		
		//Inserimento utenti autorizzati
		nuovo.Authorized= (Vector<String>) getAuthorized();
		
		return nuovo;
	}
	
	@Override
	public boolean equals(Object obj) {
		//Necessario per utilizzare Contains e IndexOf
		
		if (obj.equals(info.toString())) return true;
		return false;
		
	}
}


