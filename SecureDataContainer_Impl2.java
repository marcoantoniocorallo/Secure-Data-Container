import MyExceptions.*;
import java.util.*;

import static java.lang.System.exit;

@SuppressWarnings("unchecked") public class SecureDataContainer_Impl2<E extends MyCloneable>
			 implements SecureDataContainer<E>{
	/**IR:	Collection!=null
	 * 		&& for all i.(0<=i<Collection.size()
	 * 			==> (Collection.containsKey(User_i) <==> Collection.get(User_i) != null))
	 *
	 * FA: {<<getKey(User_i, XXX).getID(), XXX>,
	 *			{<Encrypt(Collection.get(getKey(User_i, XXX)).get(j).getInfo(), 'D'),
	 * 				Collection.get(getKey(User_i, XXX)).get(j).getAuthorized()>}>}
	 *
	 *		for all i.(0<=i<Collection.size()
	 *			==> User_i is the username of the i-th user of the collection
	 *				&& Collection.getKey(User_i, XXX).check(User_i, XXX)==true
	 **/
	
	private HashMap<User, Vector<DataInformation>> Collection;
	//Associa ad ogni utente una lista di dati
	
	//Costruttore
	public SecureDataContainer_Impl2(){
		Collection=new HashMap<>();
	}
	
	//Restituisce il numero degli elementi di un utente presenti nella
	//collezione
	public int getSize(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner,passw);
		
		return Collection.get(u).size();
	}
	
	//Restituisce il numero degli utenti autorizzati ad accedere ad un determinato dato
	//Metodo aggiuntivo non richiesto
	public int AuthorizedSize(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword, DataNotFound {
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner,passw);
		
		//Dato cifrato
		E d = (E) Encrypt(data, 'E');
		
		//Controllo esistenza del dato
		int indiceDato = Collection.get(u).indexOf(new DataInformation(d));
		if (indiceDato>=0)
			return Collection.get(u).get(indiceDato).AuthorizedSize();
		
		throw new DataNotFound("Dato non trovato in AuthorizedSize");
	}
	
	//Restituisce l'insieme di dati a cui un utente ha accesso in lettura
	//Metodo aggiuntivo non richiesto
	public Set<E> PrintAuthorizedData(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner,passw);
		
		Set<E> set = new HashSet<>();
		
		//Scansione lista di liste
		for (Vector<DataInformation> i:
			 Collection.values()) {
		
			//Scansione lista dati corrente
			for (DataInformation d:
				 i) {
				
				//Se Owner è autorizzato, aggiungi il dato decifrato all'insieme
				if (d.getAuthorized().contains(u))
					set.add((E) Encrypt((E) d.getInfo(), 'D'));
			}
		}
		
		return set;
	};
	
	//Restituisce il numero di utenti nella struttura
	//Metodo aggiuntivo non richiesto
	public int UsersSize() throws NullPointerException{
		if (Collection==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		
		return Collection.size();
	}
	
	//Ottiene una copia del valore del dato nella collezione
	//se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			NotAuthorized, WrongPassword, DataNotFound, UserNotExists{
		
		//Controlla esistenza dato
		boolean check=false;
		for (Vector<DataInformation> v:
			 Collection.values()) {
			if (v.contains(new DataInformation(Encrypt(data, 'e'))))
				check=true;
		}
		if (check==false) throw new DataNotFound("Dato non trovato, impossibile restituirlo");
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner,passw);
		
		int indicedato;
		Set s; //set d'appoggio
		Iterator<E> it;
		E tmp=null;	//Valore da ritornare
		E d = Encrypt(data, 'E'); //Dato cifrato per i confronti
		
		if ((indicedato=Collection.get(u).indexOf(new DataInformation(d)))>=0)	//Dato di owner
			tmp= (E) Collection.get(u).get(indicedato).getInfo();
		else if ((s=PrintAuthorizedData(Owner,passw)).contains(d)){		//Owner autorizzato
			
			it = s.iterator();
			while (it.hasNext()){
				if ((tmp=it.next()).equals(d))
					break;
			}
		}
		else throw new NotAuthorized("Utente non autorizzato in metodo get");
		
		return Encrypt(tmp, 'D');
	};
	
	//Crea l’identità un nuovo utente della collezione
	public void createUser(String Id, String passw)
			throws IllegalArgumentException, NullPointerException, UserAlreadyExists{
		
		//Controlli di consistenza
		if (this.Collection==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Id==null || passw==null) throw new IllegalArgumentException("Input null alla creazione dell'utente.");
		
		//Unicità degli utenti
		if (Collection.containsKey(new User(Id,passw))) throw new UserAlreadyExists("Utente già esistente.");
		
		Collection.put(new User(Id,passw), new Vector<>() );
	};
	
	//Rimuove l’utente dalla collezione
	public void RemoveUser(String Id, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Id, passw);
		
		Collection.remove(u);
	};
	
	//Inserisce il valore del dato nella collezione
	//se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner,passw);
		
		Collection.get(u).add(new DataInformation(Encrypt(data, 'E')));
		
		if (Collection.get(u).contains(Encrypt(data, 'E'))) return true;
		else return false;
	};
	
	//Rimuove il dato nella collezione
	//se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword, DataNotFound{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner,passw);
		
		//Controlla esistenza dato
		if (!(Collection.get(u).contains(new DataInformation(Encrypt(data, 'E')))))
			throw new DataNotFound("Dato non trovato, errore all'eliminazione.");
		
		E tmp = (E) Collection.get(u).get(
				Collection.get(u).indexOf(new DataInformation(Encrypt(data, 'e')))	//Indice dato
				).getInfo();
		
		int indice = Collection.get(u).indexOf(new DataInformation(Encrypt(data, 'e')));
		return Encrypt((E) Collection.get(u).remove(indice).getInfo(), 'd');
		
	};
	
	//Crea una copia del dato nella collezione
	//se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			WrongPassword, UserNotExists, DataNotFound, NotAuthorized{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner, passw);
		
		//Controlla esistenza dato
		boolean contenuto = false;
		int i;
		E d=null;
		for (Vector<DataInformation> v:
			 Collection.values()) {
			if ((i=v.indexOf(new DataInformation(Encrypt(data, 'E'))))>=0) {
				contenuto = true;
				
				//Controlla autorizzazione
				if (v.get(i).getAuthorized().contains(u.getID()))
					
					//Aggiunge il dato già cifrato
					// ed esce
					Collection.get(u).add(new DataInformation(v.get(i).getInfo()));
					return;
			}
		}
		
		if (contenuto==false)
			throw new DataNotFound("Dato non trovato, impossibile copiarlo.");
		else throw new NotAuthorized("Utente non autorizzato all'operazione.");
		
	};
	
	//Condivide il dato nella collezione con un altro utente
	//se vengono rispettati i controlli di identità
	public void share(String Owner, String passw, String Other, E data)
			throws NullPointerException, IllegalArgumentException,
			WrongPassword, DataNotFound, UserNotExists, UserAlreadyExists{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner, passw);
		
		//Esistenza utente other
		if (!Collection.containsKey(new User(Other,"nonrilevante")))
			throw new UserNotExists(Other + " non trovato.");
		
		//Esistenza dato
		if (Collection.get(u).contains(new DataInformation(Encrypt(data, 'E')))) {
			
			//Presenza dell'utente tra gli autorizzati
			boolean contenuto =
					Collection.get(u).get(
						Collection.get(u).indexOf(new DataInformation(Encrypt(data, 'e')))		//Indice dato
					).getAuthorized().contains(Other);
			
			if (!contenuto)
				Collection.get(u).get(
					Collection.get(u).indexOf(new DataInformation(Encrypt(data, 'E')))    //Indice dato
				).Addlicense(Other);
			else throw new UserAlreadyExists(Other+" già autorizzato a "+ data.toString());
		}
		
		else throw new DataNotFound(data.toString()+" non trovato, impossibile condividerlo.");
	};
	
	//Modifica il contenuto di un dato, se sono rispettati i controlli d'identità
	//Metodo aggiuntivo non richiesto
	public void modify(String Owner, String passw, E OldData, E NewData)
			throws NullPointerException, IllegalArgumentException,
			WrongPassword, UserNotExists, DataNotFound{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner, passw);
		
		//Controlli sui dati
		if (OldData==null || NewData == null)
			throw new IllegalArgumentException("Dati input null, impossibile da modificare");
		
		//Controlla esistenza dato
		int indice;
		try {
			indice = Collection.get(u).indexOf(new DataInformation(Encrypt(OldData, 'E')));
		}
		catch (ArrayIndexOutOfBoundsException e) {throw new DataNotFound("Dato non esistente, impossibile modificarlo.");}
		Collection.get(u).get(indice).setInfo(Encrypt(NewData, 'E'));
	};
	
	//Meccanismo di cifratura dati
	public E Encrypt(E data, char op)
			throws  IllegalArgumentException, NullPointerException{
		
		if (data==null) throw new IllegalArgumentException("dato null, non cifrabile");
		
		switch (op) {
			case ('d'):
			case ('D'):
				return (E) data.decrypt();
			case ('e'):
			case ('E'):
				return (E) data.encrypt();
			default:
				throw new IllegalArgumentException("Operazione " + op + " non programmata");
		}
		
	};
	
	//Metodo che permette di ricavare una copia dell'ID di un utente
	//Metodo aggiuntivo non richiesto, implementato per l'AF
	public User getKey(String name, String pass)
			throws IllegalArgumentException, NullPointerException, UserNotExists, WrongPassword {
		
		//Controlli di consistenza
		if (this.Collection==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (name==null|| pass==null) throw new IllegalArgumentException("Input null in getKey\n.");
		
		//controllo esistenza chiave
		if (Collection.containsKey(new User(name,pass))){
			
			Set<User> s = Collection.keySet();
			for (User u:
				 s) {
				
				if (u.getID().equals(name)) {
					
					//Controllo identità
					if (u.check(name, pass)) {
						
						//Restituisci chiave
						return u;
					} else
						throw new WrongPassword("Password errata in getKey");
				}
			}
			
		}
		else throw new UserNotExists("Chiave non trovata in getKey.");
		
		return null; //Sintatticamente necessario
	};
	
	//Restituisce un iteratore (senza remove) che genera tutti i dati
	//dell’utente in ordine arbitrario
	//se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//getKey effettua i controlli, occupandosi di sollevare le dovute eccezioni
		//Questo metodo li solleva al sottoprogramma chiamante, non occupandosi della gestione
		User u = getKey(Owner, passw);
		return new SDCIterator(u);
		
	}
	
	//Definizione classe iteratore
	public class SDCIterator
	implements Iterator<E>{
		
		int last; //Ultimo elemento iterato
		Vector<DataInformation> v; //Elementi su cui iterare
		
		public SDCIterator(User u){
			last=-1;
			v=Collection.get(u);
		}
		
		@Override
		public boolean hasNext() {
			if (v.size()-1==last)	return false;
			else 					return true;
		}
		
		@Override
		public E next() throws NoSuchElementException {
			if (hasNext()){
				last++;
				
				E tmp;
				tmp = (E) v.get(last).getInfo();
				
				return Encrypt(tmp, 'D');
			}
			else throw new NoSuchElementException();
		}
	}
}
