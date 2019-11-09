import MyExceptions.WrongPassword;

public class User {
	
	private String ID;
	private String pass;
	
	//Constructor
	
	public User(String a, String b){
		//Essendo le stringhe oggetti non modificabili, si avrà un aliasing tra ID/pass
		//e le stringhe in input solo fino a quando queste non verranno modificate.
		//Quando questo avverrà, non si avrà più nessun tipo di collegamento tra i due oggetti,
		//e non ci sarà quindi modo di modificare l'utente
		ID = a;
		pass = b;
	}
	
	//Observers
	
	public String getID(){
		return ID;
	}
	
	//Effettua il controllo sull'identità.
	//In questo modo, non occorre definire un metodo che esponga la password
	public boolean check(String ID, String passw)
				   throws NullPointerException{
		if (ID==null) throw new NullPointerException("Id null al controllo d'identita'");
		if (passw==null) throw new NullPointerException("Password null al controllo d'identita'");
		
		//Se ID e Password corrispondono, return true
		if (this.pass.equals(passw) && this.ID.equals(ID))	return true;
		else												return false;
	}
	
	//Controlla che la password non sia null
	public boolean isNull(){
		if (pass==null) return true;
		else return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		//Necessario per utilizzare Contains() e IndexOf()
		
		if (obj.equals(ID)) return true;
		else return false;
	}
	
	@Override
	public int hashCode() {
		//Necessario per poter confrontare gli utenti per ID
		
		return ID.hashCode();
	};
}
