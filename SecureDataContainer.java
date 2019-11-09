import java.util.Iterator;
import java.util.Set;
import MyExceptions.*;

public interface SecureDataContainer<E> {
/**
*Overview: Is a mutable collection for memorizing and sharing user data.
*	       Each of this data is protected by a correspondence <User,Password>.
* 		   User B has to be authorized by A for acceding to some A's Data.
*
*Typical Element:{<<User_0, Pass_0>,{<e_00,{UAuthorized_00_0, UAuthorized_00_1,...,UAuthorized_00_k}>,
* 			   	   				     <e_01,{UAuthorized_01_0, UAuthorized_01_1,...,UAuthorized_01_h}>,
* 				   	  				 ::
* 				   	  				 <e_0m,{UAuthorized_0m_0, UAuthorized_0m_1,...,UAuthorized_0m_p}>,
* 				   	  				}>
* 				   ::
* 				   <<User_(n-1), Pass_(n-1)>,
* 				   					{<e_(n-1)0,{UAuthorized_(n-1)0_0, UAuthorized_(n-1)0_1,...,UAuthorized_(n-1)0_s}>,
* 	   			   				     <e_(n-1)1,{UAuthorized_(n-1)1_0, UAuthorized_(n-1)1_1,...,UAuthorized_(n-1)1_w}>,
* 	   			   		  			 ::
* 	   			   		  			 <e_(n-1)q,{UAuthorized_(n-1)q_0, UAuthorized_(n-1)q_1,...,UAuthorized_(n-1)q_t}>,
* 	   			   		  		    }>
* 	 			 }
*
* 	 			 such as: n = #Users
*				 && for all i.(0<=i<n
*				 				==> User_i is an identifier of an user of collection
*				 				&& Pass_i is the password of that user)
*				 && for all i.(0<=i<n
*				 	==> for all j.(0<=j<getSize(User_i) ==> eij is a Ui's Data))
*				 && for all i.(0<=i<n
*					==> for all j.(0<=j<getSize(User_i)
*						==> for all k.(0<=k<AuthorizedSize(User_i, e_ij)
*							==>Uauthorized_ij_k is an user authorized from User_i to access to him j-th data)))
*/

	/*Observers*/
	
	//Restituisce il numero degli elementi di un utente presenti nella
	//collezione
	public int 	getSize(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword;
	//@REQUIRES:Owner != null && passw != null
	//			&& this != null
	//			&& exists i.(0<=i<n && User_i.equals(Owner) && Pass_i.equals(passw))
	//@RETURNS:	#Owner's data
	//@THROWS:	NullPointerException if this == null
	//			IllegalArgumentException if Owner==null || passw==null
	//			UserNotExists if !exists i.(0<=i<n && User_i.equals(Owner))
	//			WrongPassword if  exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	
	//Restituisce il numero degli utenti autorizzati ad accedere ad un determinato dato
	//Metodo aggiuntivo non richiesto
	public int AuthorizedSize(String Owner, String passw, E data)
			   throws NullPointerException, IllegalArgumentException,
			   UserNotExists, WrongPassword, DataNotFound;
	//@REQUIRES:this!=null
	//			&& Owner!=null && passw!=null && data!=null
	//			&& exists i.(0<=i<n && User_i.equals(Owner) && Pass_i.equals(passw)
	// 			   && exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypt(data, 'E'))))
	//@RETURNS:	#Authorized users to data
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null || passw==null || data==null
	//			WrongPassword if  exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	//			UserNotExists if !exists i.(0<=i<n && User_i.equals(Owner))
	//			NotAuthorized if  exists i.(0<=i<n && User_i.equals(Owner) )
	//			DataNotFound  if for all i.(0<=i<n
	//							 ==>(!exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypt(data, 'E')))))
	
	//Restituisce un iteratore (senza remove) che genera tutti i dati
	//dell’utente in ordine arbitrario
	//se vengono rispettati i controlli di identità
	public Iterator<E> getIterator(String Owner, String passw)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword;
	//@REQUIRES:this!=null && Owner!=null && passw!=null
	//			&& exists i.(0<=i<n && User_i.equals(Owner) && Pass_i.equals(passw))
	//@RETURNS:	an iterator over elements of type E
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null || passw==null
	//			UserNotExists if for all i.(0<=i<n ==> !User_i.equals(Owner))
	//			WrongPassword if exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	
	//Restituisce l'insieme di dati a cui un utente ha accesso in lettura
	//Metodo aggiuntivo non richiesto
	public Set<E> PrintAuthorizedData(String Owner, String passw)
				  throws NullPointerException, IllegalArgumentException,
				  UserNotExists, WrongPassword;
	//@REQUIRES:this!=null && Owner!=null && passw!=null
	//			&& exists i.(0<=i<n && User_i.equals(Owner) && Pass_i.equals(passw))
	//@RETURNS:	a set of decrypted copies of elements to whom Owner is authorized to access
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null || passw==null
	//			UserNotExists if for all i.(0<=i<n ==> !User_i.equals(Owner))
	//			WrongPassword if exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	
	//Restituisce il numero di utenti nella struttura
	//Metodo aggiuntivo non richiesto
	public int UsersSize() throws NullPointerException;
	//@REQUIRES:this!=null
	//@RETURNS:	#Users
	//THROWS:	NullPointerException if this==null
	
	/*Producers*/
	
	//Ottiene una copia del valore del dato nella collezione
	//se vengono rispettati i controlli di identità
	public E get(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			NotAuthorized, WrongPassword, DataNotFound, UserNotExists;
	//@REQUIRES:this!=null
	//			&& Owner!=null && passw!= null && data!=null
	//			&& exists i.(0<=i<n
	// 			   && exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypted(data, 'E'))
	// 				  && exists k.(0<=k<AuthorizedSize(User_i,e_ij)
	//			   		 && UAuthorized_ij_k.equals(Owner))))
	//			&& exists i.(0<=i<n && User_i.equals(Owner) && Pass_i.equals(passw))
	//@RETURNS:	a copy of data from Owner's collection
	//@THROWS:	NullPointerException	if this==null
	//			IllegalArgumentException if Owner==null || passw==null || data==null
	//			UserNotExists if !exists i.(0<=i<n && User_i.equals(Owner))
	//			WrongPassword if exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	//			NotAuthorized if Owner is not authorized to access to data
	// 			DataNotFound  if for all i.(0<=i<n
	// 							 ==>(!exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypted(data, 'E')))))
	
	/*Mutators*/
	
	//Crea l’identità un nuovo utente della collezione
	public void createUser(String Id, String passw)
				throws IllegalArgumentException, NullPointerException, UserAlreadyExists;
	//@REQUIRES:Id != null && passw != null
	// 			&& this != null
	// 			&& for all i.(0<=i<n ==> !User_i.equals(Id))
	//@MODIFIES:this
	//@EFFECTS:	this[post]=this[pre] U <<Id,passw>,{}>
	//@THROWS:	IllegalArgumentException if Id==null || passw==null
	// 			NullPointerException	 if this==null
	//			UserAlreadyExists		 if exist i.(0<=i<n && User_i.equals(Id))
	
	//Rimuove l’utente dalla collezione
	public void RemoveUser(String Id, String passw)
				throws NullPointerException, IllegalArgumentException,
					   UserNotExists, WrongPassword;
	//@REQUIRES:Id != null && passw != null
	//			&& this != null
	//			&& exist i.(0<=i<n && User_i.equals(Id) && Pass_i.equals(passw))
	//@MODIFIES:this
	//@EFFECTS:	removes user Id from collection, so also him data and authorizations, if has some
	//@THROWS:	NullPointerException if this == null
	//			IllegalArgumentException if Id == null || passw == null
	//			UserNotExists			 if for all i.(0<=i<n ==> !User_i.equals(Id))
	//			WrongPassword			 if exists  i.(0<=i<n && User_i == Id && Pass_i != passw)
	
	//Inserisce il valore del dato nella collezione
	//se vengono rispettati i controlli di identità
	public boolean put(String Owner, String passw, E data)
			throws NullPointerException, IllegalArgumentException,
			UserNotExists, WrongPassword;
	//@REQUIRES:this!=null
	//			&& Owner!= null && passw!=null && data!=null
	//			&&  exists i.(0<=i<n && User_i.equals(Owner) && Pass_i.equals(passw)
	//				&& for all j.(0<=j<getSize(User_i) ==> !e_ij.equals(data)))
	//@MODIFIES:Owner's dataset
	//@EFFECTS:	adds encrypted data in Owner's collection
	//@RETURNS:	true if the data has been added, false if not and no exceptions are raised
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null || passw==null || data==null
	//			UserNotExists if !exists i.(0<=i<n && User_i.equals(Owner))
	//			WrongPassword if  exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	
	
	//Rimuove il dato nella collezione
	//se vengono rispettati i controlli di identità
	public E remove(String Owner, String passw, E data)
			 throws NullPointerException, IllegalArgumentException,
					UserNotExists, WrongPassword, DataNotFound;
	//@REQUIRES:Owner!=null && passw!=null && data!=null && this!=null
	//			exists i.(0<=i<n
	// 			&& exists j.(0<=j<getSize(User_i)
	// 			   && User_i.equals(Owner) && Pass_i.equals(passw) && e_ij.equals(Encrypted(data, 'E'))))
	//@MODIFIES:Owner's dataset
	//@EFFECTS:	Removes data from owner's dataset
	//@RETURNS:	data removed
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null || data==null || data==null
	//			UserNotExists if !exists i.(0<=i<n && User_i.equals(Owner))
	//			WrongPassword if  exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	//			DataNotFound  if !exists i.(0<=i<n
	// 							 && !exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypted(data, 'E'))))
				
	//Crea una copia del dato nella collezione
	//se vengono rispettati i controlli di identità
	public void copy(String Owner, String passw, E data)
				throws NullPointerException, IllegalArgumentException,
				WrongPassword, UserNotExists, DataNotFound, NotAuthorized;
	//@REQUIRES:this!=null
	//			&& Owner!=null && passw!=null && data!=null
	//			&& exists i.(0<=i<n
	// 			   && exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypted(data, 'E'))
	// 				  && (exists k.(0<=k<AuthorizedSize(User_i,e_ij) && UAuthorized_ij_k.equals(Owner))
	// 					  || User_i.equals(Owner))))
	//			&& exists i.(0<=i<n && User_i.equals(Owner) && Pass_i.equals(passw))
	//@MODIFIES:Owner's dataset
	//@EFFECTS:	insert an encrypted copy of data in Owner's dataset, both he's the proprietary of data,
	//			or he is just an authorized user.
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null || passw==null || data==null
	//			WrongPassword if  exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	//			UserNotExists if !exists i.(0<=i<n User_i.equals(Owner))
	//			DataNotFound  if !exists i.(0<=i<n
	// 							 && !exists j.(0<=j<getSize(User_i) && e_ij.equals(Ecrypted(data, 'E'))))
	//			NotAuthorized if Owner isn't authorized to access to data
	
	//Condivide il dato nella collezione con un altro utente
	//se vengono rispettati i controlli di identità
	public void share(String Owner, String passw, String Other, E data)
				throws NullPointerException, IllegalArgumentException,
				WrongPassword, DataNotFound, UserNotExists, UserAlreadyExists;
	//@REQUIRES:this!=null
	//			&& Owner!=null && passw!=null && Other!=null && data!=null
	//			&& exists i.(0<=i<n
	// 			   && exists j.(0<=j<getSize(User_i)
	// 				  && exists k.(0<=k<n
	// 					 && User_i.equals(Owner) && Pass_i.equals(passw) && e_ij.equals(Encrypted(data, 'E'))
	//				 	 && User_k.equals(Other) && !User_k.equals(User_i))
	//			      && for all l.(0<=l<AuthorizedSize(User_i, e_ij)
	// 					 ==> !UAuthorized_ij_l.equals(Other))))
	//@MODIFIES:data's authorized list
	//@EFFECTS:	Inserts Other into data's authorized users list
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null || passw==null || Other==null || data==null
	//									 || Owner.equals(Other)
	//			WrongPassword if exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	//			UserAlreadyExists if Other is already authorized for acceding to data
	//			DataNotFound  if !exists i.(0<=i<n
	// 							 && !exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypted(data, 'E'))))
	//			UserNotExists if for all i.(0<=i<n ==> !User_i.equals(Owner))
	//						  || for all i.(0<=i<n ==> !User_i.equals(Other))
	
	//Modifica il contenuto di un dato, se sono rispettati i controlli d'identità
	//Metodo aggiuntivo non richiesto
	public void modify(String Owner, String passw, E OldData, E NewData)
				throws NullPointerException, IllegalArgumentException,
				WrongPassword, UserNotExists, DataNotFound;
	//@REQUIRES:this!=null && Owner!=null && passw!= null && OldData!=null && NewData!=null
	//			exists i.(0<=i<n
	// 			&& exists j.(0<=j<getSize(User_i)
	// 			   && User_i.equals(Owner) && Pass_i.equals(passw) && e_ij.equals(OldData)))
	//@MODIFIES:OldData
	//@EFFECTS:	replaces Encrypt(OldData, 'E') with Encrypt(NewData, 'E')
	//@THROWS:	NullPointerException if this==null
	//			IllegalArgumentException if Owner==null||passw==null||OldData==null||NewData==null
	//			WrongPassword if  exists i.(0<=i<n && User_i.equals(Owner) && !Pass_i.equals(passw))
	//			UserNotExists if !exists i.(0<=i<n User_i.equals(Owner))
	//			DataNotFound  if !exists i.(0<=i<n
	//							 && !exists j.(0<=j<getSize(User_i) && e_ij.equals(Encrypt(data,'E'))))
	//
	
	//Meccanismo di cifratura dati
	public E Encrypt(E data, char op)
			throws  IllegalArgumentException, NullPointerException;
	//@REQUIRES:this!=null && data != null && op == 'd' || op == 'D' op == 'e' op == 'E'
	//@RETURNS: encrypted value of data if op == 'E' || op == 'e'
	//			decrypted value of data if op == 'D' || op == 'd'
	//@THROWS:	IllegalArgumentException if data==null || op != {'d','D','e','E'}
	//			NullPointerException if this == null
	
}