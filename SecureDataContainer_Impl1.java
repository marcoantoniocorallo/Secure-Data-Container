import MyExceptions.*;
import java.util.*;

@SuppressWarnings("unchecked") public class SecureDataContainer_Impl1<E extends MyCloneable>
			 implements SecureDataContainer<E>{
	/**IR:	Users!=null && Data!=null
	 *
	 * 		&& for all i.(0<=i<Users.size()
	 * 		   ==> Users.get(i)!=null && Users.get(i).getID()!=null && !Users.get(i).isNull()
	 * 		   && for all j.(0<=j<Users.size() && i!=j
	 * 		   	  ==> !Users.get(i).getID.equals(Users.get(j).getID())))
	 *
	 * 		&& for all i.(0<=i<Data.size() ==> Data.get(i)!=null
	 * 		   && for all j.(0<=j<Data.get(i).size()
	 * 		   	  ==> Data.get(i).get(j)!=null && Data.get(i).get(j).getInfo()!=null
	 * 		   	  &&  Data.get(i).get(j).getAuthorized()!=null
	 * 		   	  && for all k.(0<=k<Data.get(i).get(j).AuthorizedSize()
	 * 		   	  	 ==> Data.get(i).get(j).getAuthorized().get(k)!=null)))
	 *
	 * 		&& Users.size()==Data.size()
	 * 		&& Users.size()>=0 && Data.size()>=0
	 *
	 *
	 * FA:{<<Users.get(i).getID(), XXX>,{<Encrypt(Data.get(i).get(j).getInfo(), 'D'),
	 * 									  Data.get(i).get(j).getAuthorized()>}>}
	 *
	 * such as: 0<=i<Users.size() && 0<=j<Data.get(i).size()
	 * 			&& for all i.(Users.get(i).check(Users.get(i).getID(), XXX)==true))
	 */
	
	private Vector<User> Users;
	private Vector<Vector<DataInformation>> Data; //Vettore di liste di dati
	
	//Costruttore
	public SecureDataContainer_Impl1(){
		Users=new Vector<>();
		Data =new Vector<>();
	}
	
	//Restituisce il numero degli elementi di un utente presenti nella
	//collezione
	public int 	getSize(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//Controlli di consistenza
		if (Owner==null) throw new IllegalArgumentException("Id nullo, richiesta quantita' dati annullata.");
		if (passw==null) throw new IllegalArgumentException("Password nulla, richiesta quantita' dati annullata.");
		if (this.Users==null || this.Data==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		
		//Controllo esistenza utente
		if (!Users.contains(new User(Owner,passw))) throw new UserNotExists(Owner +
				"Richiesta quantita' dati annullata.");
		
		//Controllo di identità
		User tmp = Users.get(Users.indexOf(new User(Owner,passw)));
		if (!tmp.check(Owner, passw)) throw new WrongPassword("Password errata!"+
				"Richiesta quantita' dati annullata");
		
		//				data[UtenteInteressato].size()
		return Data.get(Users.indexOf(new User(Owner,passw))).size();
	}
	
	//Restituisce il numero degli utenti autorizzati ad accedere ad un determinato dato
	//Metodo aggiuntivo non richiesto
	public int AuthorizedSize(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword, DataNotFound{
		
		//Controlli di consistenza
		if (Owner==null) throw new IllegalArgumentException("Id nullo. Richiesta utenti autorizzati annullata.");
		if (passw==null) throw new IllegalArgumentException("Password nulla. Richiesta utenti autorizzati annullata.");
		if (data==null) throw new IllegalArgumentException("dati null. Richiesta utenti autorizzati annullata.");
		if (this.Users==null || this.Data==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		
		//Salva indice Owner e Utente corrispondente
		//Se non lo trova solleva l'eccezione
		int IndiceUtente = Users.indexOf(new User(Owner,passw));
		User tmp;
		try {
			tmp = Users.get(IndiceUtente);
		}
		catch (ArrayIndexOutOfBoundsException e) {throw new UserNotExists(Owner);}
		
		//Controllo identità
		if (!tmp.check(Owner, passw)) throw new WrongPassword("Password errata.\nRichiesta utenti autorizzati annullata.");
		
		//Controlla il dato sia presente
		if (!Data.get(IndiceUtente).contains(new DataInformation(Encrypt(data, 'e'))))
			throw new DataNotFound(data.toString()+"\nRichiesta utenti autorizzati annullata.");
		
		//		Dati di Owner
		return (Data.get(IndiceUtente).get(
				/*           Indice del dato 	  					*/
				Data.get(IndiceUtente).indexOf(new DataInformation(Encrypt(data, 'e'))))
				.AuthorizedSize()) ;
	}
	
	//Restituisce il numero di utenti nella struttura
	//Metodo aggiuntivo non richiesto
	public int UsersSize() throws NullPointerException{
		if (this.Data==null || this.Users==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		
		else return Users.size();
	}
	
	//Restituisce l'insieme di dati a cui un utente ha accesso in lettura
	//Metodo aggiuntivo non richiesto
	public Set<E> PrintAuthorizedData(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//Controlli di consistenza
		if (this.Data==null || this.Users==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null) throw new IllegalArgumentException("Id null. Richiesta dati RO annullata.");
		if (passw==null) throw new IllegalArgumentException("Password null. Richiesta dati RO annullata.");
		
		//Salva indice Owner e Utente corrispondente
		//Se non lo trova solleva l'eccezione
		int IndiceUtente = Users.indexOf(new User(Owner,passw));
		User tmp;
		try {
			tmp = Users.get(IndiceUtente);
		}
		catch (ArrayIndexOutOfBoundsException e) {throw new UserNotExists(Owner + "Richiesta dati RO annullata.");}
		if (!tmp.check(Owner, passw)) throw new WrongPassword("Password errata. Richiesta dati RO annullata.");
		
		//Inizializza insieme da restituire
		Set<E> insieme = new HashSet<>();
		
		//Ricerca dati a cui Owner è autorizzato:
		//Scandisce Vettore di insiemi di dati
		for (Vector<DataInformation> v:
			 Data) {
			
			//Scandisce ogni lista di dati utente
			for (DataInformation d:
				 v) {
				
				//Se Owner è autorizzato al dato corrente
				if (d.getAuthorized().contains(new User(Owner,passw)))
					
					//Aggiungi all'insieme, se la clone è definita (controllo in getInfo)
					insieme.add((E) d.getInfo());
			}
		}
		
		//Decifra l'insieme di dati (Aggiunto dopo)
		Set<E> insieme2 = new HashSet<>();
		
		for (E i:
			 insieme) {
			insieme2.add(Encrypt(i, 'd'));
		}
		return insieme2;
	}
	
	//Ottiene una copia del valore del dato nella collezione
	//se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			NotAuthorized, WrongPassword, DataNotFound, UserNotExists {
		
		//Controlli di consistenza
		if (this.Data==null || passw==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null || passw==null || data==null) throw new IllegalArgumentException("Input null.\nRichiesta dato annullata.");
		
		//Salva indice Owner e Utente corrispondente
		//Se non lo trova solleva l'eccezione
		int IndiceUtente = Users.indexOf(new User(Owner,passw));
		User tmp;
		try {
			tmp = Users.get(IndiceUtente);
		}
		catch (ArrayIndexOutOfBoundsException e) {throw new UserNotExists(Owner+"Richiesta dato annullata.");}
		
		//Verifica identità
		if (!tmp.check(Owner, passw)) throw new WrongPassword("Richiesta dato annullata.");
		
		//Cerca il dato scandendo Data
		//Secondo questa scelta progettuale, infatti, non vengono passati i giusti parametri
		//per poter effettuare una ricerca "indicizzata"
		for (Vector<DataInformation> i:
			 Data) {
			for (DataInformation d:
				 i) {
				
					//Confronta parametro con dato attuale decifrato
					E tempo = Encrypt((E) d.getInfo(), 'd');
					if (tempo.toString().equals(data.toString())){
						
						//Se utente autorizzato || Utente proprietario => Restituisci il dato
						if (d.getAuthorized().contains(new User(Owner,passw))
							|| (IndiceUtente==Data.indexOf(i))) {
							
							return tempo;
						}
						else throw new NotAuthorized(Owner + " Non autorizzato.\nRichiesta dato annullata.");
					}
			}
		}
		
		//Dato non trovato
		throw new DataNotFound(data.toString()+" non trovato. Richiesta dato annullata.");
	}
	
	//Crea l’identità un nuovo utente della collezione
	public void createUser(String Id, String passw)
			throws IllegalArgumentException, NullPointerException, UserAlreadyExists{
		
		//Controlli di consistenza
		if (this.Users==null || this.Data==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Id==null) throw new IllegalArgumentException("Id null. Creazione utente fallita.");
		if (passw==null) throw new IllegalArgumentException("Password null. Creazione utente fallita.");
		
		//Elimina spazi alle estremità del nome utente
		Id=Id.trim();
		
		//Controlla la presenza di un omonimo
		if (Users.contains(new User(Id,passw))) throw new UserAlreadyExists("Omonimo presente. Creazione utente fallita.");
		Users.add(new User(Id, passw));
		Data.add(new Vector<>());
	}
	
	//Rimuove l’utente dalla collezione
	public void RemoveUser(String Id, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//Controlli di consistenza
		if (this.Users==null || this.Data==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Id==null) throw new IllegalArgumentException("Id null. Rimozione utente fallita.");
		if (passw==null) throw new IllegalArgumentException("Password null. Rimozione utente fallita.");
		
		//Se l'utente esiste, elimina utente
		if (Users.contains(new User(Id,passw))) {
			if (Users.get(Users.indexOf(new User(Id,passw))).check(Id, passw))
				Users.remove(new User(Id, passw));
			else throw new WrongPassword("Creazione utente fallita. Password errata.");
		}
		else throw new UserNotExists("Utente non trovato. Rimozione utente fallita.");
	}
	
	//Inserisce il valore del dato nella collezione
	//se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//Controlli di consistenza
		if (this.Users==null || this.Data==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null || passw == null || data==null) throw new IllegalArgumentException(
				"Input null. Inserimento dato fallito.");
		
		//Controlla che l'utente esista
		if (!Users.contains(new User(Owner, passw))) throw new UserNotExists("Utente non trovato.\n" +
				"Inserimento dato fallito.");
		
		//Controlla identità
		if (Users.get(Users.indexOf(new User(Owner, passw))).check(Owner, passw)){
			
			//Cifra il dato
			E d =  Encrypt(data, 'e');
			
			//Aggiunge dato
			Data.get(Users.indexOf(new User(Owner,passw))).add(new DataInformation(d));
			
			//Verifica che sia stato aggiunto
			if (Data.get((Users.indexOf(new User(Owner,passw)))).contains(new DataInformation(d)))
				return true;
			else return false;
		}
		throw new WrongPassword("Password errata. Inserimento dato fallito.");
	}
	
	//Rimuove il dato nella collezione
	//se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword, DataNotFound{
		
		//Controlli di consistenza
		if (this.Data==null || this.Users==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null || passw==null || data==null) throw new IllegalArgumentException("Input nullo. Rimozione dato fallita.");
		
		//Controlla esistenza utente
		if (!Users.contains(new User(Owner,passw))) throw new UserNotExists("Utente non trovato.\nRimozione dato fallita.");
		
		//Cerca dato
		int indice=Data.get(Users.indexOf(new User(Owner,passw))).indexOf(new DataInformation(Encrypt(data, 'e')));
		
		//Controllo identità
		if (Users.get(Users.indexOf(new User(Owner,passw))).check(Owner,passw)){
			E tmp=null;
			try {
				tmp = (E) Data.get(Users.indexOf(new User(Owner,passw))).get(indice).getInfo();
			}
			catch (ArrayIndexOutOfBoundsException e) {throw new DataNotFound("Dato non trovato.\n" +
					"Rimozione dato fallita.");
			}
			
			//Elimina dato
			Data.get(Users.indexOf(new User(Owner,passw))).remove(new DataInformation(Encrypt(data, 'e')));
			return Encrypt(tmp, 'd');
		}
		
		throw new WrongPassword("Password errata. Rimozione dato fallita.");
	}
	
	//Crea una copia del dato nella collezione
	//se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			WrongPassword, UserNotExists, DataNotFound, NotAuthorized{
		
		//Controlli di consistenza
		if (this.Data==null || this.Users==null)
			throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null || passw==null || data==null)
			throw new IllegalArgumentException("Input null.\nCopia dato fallita.");
		
		//Controlla presenza utente
		if (!Users.contains(new User(Owner,passw))) throw new UserNotExists("Utente "+Owner+" non trovato.\n" +
				"Copia dato " + data + " fallita.");
		
		//Estraggo il dato e lo inserisco nella collezione dell'utente
		//I controlli sull'identità e l'esistenza del dato vengono effettuati dalla get
		E tmp = get(Owner,passw,data);
		if (put(Owner,passw,tmp))
			System.out.println("Dato copiato nella collezione di "+ Owner);
		else System.out.println("Dato non copiato, errore!");
		
	}
	
	//Condivide il dato nella collezione con un altro utente
	//se vengono rispettati i controlli di identità
	public void share(String Owner, String passw, String Other, E data)
			throws NullPointerException, IllegalArgumentException,
			WrongPassword, DataNotFound, UserNotExists, UserAlreadyExists{
		
		//Controlli di consistenza
		if (this.Data==null || this.Users==null)
			throw new NullPointerException(
					"*******************STATO INCONSISTENTE!!!*******************\n" +
					"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null || passw==null || data==null || Other==null)
			throw new IllegalArgumentException("Condivisione dati fallita.");
		
		//Controlla esistenza utenti
		if (!Users.contains(new User(Owner,passw))) throw new UserNotExists(Owner + " non trovato.\n" +
				"Condivisione dato "+data+" fallita.");
		if (!Users.contains(new User(Owner,passw))) throw new UserNotExists(Other + " non trovato.\n" +
				"Condivisione dato " + data + " fallita.");
		
		//Controlla che il dato esista, e l'utente non sia già autorizzato
		int indice = Data.get(Users.indexOf(new User(Owner,passw))).indexOf(new DataInformation(Encrypt(data, 'e')));
		try {
			if (Data.get(Users.indexOf(new User(Owner,passw))).get(indice).getAuthorized().contains(new User(Other,passw)))
				throw new UserAlreadyExists("Dato già condiviso con "+ Other);
			else {
				
				//Verifica credenziali e concessa autorizzazione
				if (Users.get(Users.indexOf(new User(Owner,passw))).check(Owner, passw))
					Data.get(Users.indexOf(new User(Owner,passw))).get(indice).Addlicense(Other);
				else throw new WrongPassword("Password errata. Condivisione dato fallita.");
			}
		}catch (ArrayIndexOutOfBoundsException e) {throw new DataNotFound("Dato "+data+" non trovato.\nCondivisione fallita.");}
	}
	
	//Modifica il contenuto di un dato, se sono rispettati i controlli d'identità
	//Metodo aggiuntivo non richiesto
	public void modify(String Owner, String passw, E OldData, E NewData)
			throws NullPointerException, IllegalArgumentException,
			WrongPassword, UserNotExists, DataNotFound{
		
		//Controlli di consistenza
		if (this.Data==null || this.Users==null)
			throw new NullPointerException(
					"*******************STATO INCONSISTENTE!!!*******************\n" +
					"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null || passw==null || OldData==null || NewData==null)
			throw new IllegalArgumentException("Input nullo. Modifica annullata");
		
		//Controlla presenza utente
		if (!Users.contains(new User(Owner,passw))) throw new UserNotExists(Owner + " non trovato.\n Modifica annullata.");
		
		//Controlla presenza dato
		int indiceutente = Users.indexOf(new User(Owner,passw));
		int indicedato = Data.get(indiceutente).indexOf(new DataInformation(Encrypt(OldData, 'e')));
		
		if (indicedato<0) throw new DataNotFound("Dato "+ OldData +" non trovato. Modifica annullata");
		else {
			
			//Controllo identita'
			if (Users.get(indiceutente).check(Owner,passw)){
				
					//Modifica dato
					Data.get(indiceutente).get(indicedato).setInfo(Encrypt(NewData, 'e'));
			}
			else throw new WrongPassword("Password errata.\nModifica dato annullata.");
		}
		
	}
	
	//Meccanismo di cifratura dati
	public E Encrypt(E data, char op) throws IllegalArgumentException {
		
		//Controllo parametri
		if (data==null) throw new IllegalArgumentException("Input nullo.");
		
		if (op== 'E' || op == 'e')
			return (E) data.encrypt();
		
		if (op == 'D' || op == 'd')
			return (E) data.decrypt();
		
		throw new IllegalArgumentException("Operazione "+ op + " non programmata.");
	}
	
	//Restituisce un iteratore (senza remove) che genera tutti i dati
	//dell’utente in ordine arbitrario
	//se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword{
		
		//Controlli di consistenza
		if (this.Data==null || this.Users==null) throw new NullPointerException(
				"*******************STATO INCONSISTENTE!!!*******************\n" +
				"*******************TROVATA       FALLA!!!*******************");
		if (Owner==null||passw==null) throw new IllegalArgumentException("Input nullo, iteratore non rilasciato.");
		
		//Controllo esistenza utente e controllo identita'
		if (!Users.contains(new User(Owner,passw))) throw new UserNotExists("Utent non trovato.\nIteratore non rilasciato.");
		if (!Users.get(Users.indexOf(new User(Owner,passw))).check(Owner,passw))
			throw new WrongPassword("Password errata. Iteratore non rilasciato.");
		
		int indiceutente= Users.indexOf(new User(Owner,passw));
		
		//Rilascia iteratore
		return new SDCIterator(Data.get(indiceutente));
	}
	
	//Definizione classe iteratore
	public class SDCIterator
			implements Iterator<E> {
		
		int last;//Ultimo elemento iterato
		Vector<DataInformation> V;//Elementi da iterare
		
		public SDCIterator(Vector<DataInformation> v){
			//Inizializzazione iteratore ai dati dell'oggetto chiamato.
			this.V= v;
			last=-1;
		}
		
		@Override
		public boolean hasNext() {
			if (V.size()-1==last) return false;
			else return true;
		}
		
		@Override
		public E next() throws NoSuchElementException {
			if (hasNext()){
				last++;
				
				E tmp=null;
				tmp= (E) V.get(last).getInfo();
				
				return Encrypt(tmp, 'd');
			}
			else throw new NoSuchElementException();
		}
	}
	
}
