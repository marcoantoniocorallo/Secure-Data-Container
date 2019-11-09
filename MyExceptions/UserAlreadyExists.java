package MyExceptions;

public class UserAlreadyExists extends Exception{
	//Exception throws when try to add an user that's already in the Data container.
	
	public UserAlreadyExists(){super();}
	public UserAlreadyExists(String s){super(s);}
}
