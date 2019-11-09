public class MyCloneable implements Cloneable{
	
	//Definizione metodo clone.
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
	//Metodi di cifratura e decifrazione.
	//Ridefinire in base all'algoritmo e al tipo di dato.
	public MyCloneable encrypt(){return this;}
	public MyCloneable decrypt(){return this;}
}
