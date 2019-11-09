import java.awt.*;

public class MyComplexType extends MyType{
	private int codeint;
	private char codech;
	private Point p;
	
	public MyComplexType(int a, char b, int c, String s){
		super(s);
		codeint=a;
		codech=b;
		p= new Point(c,c);
	}
	
	@Override
	public String toString() {
		return ((super.toString()+codeint+codech+ (int)p.getX() + (int) p.getY()));
	}
	
	@Override
	public MyCloneable decrypt() {
		return this;
	}
	
	@Override
	public MyCloneable encrypt() {
		return this;
	}
}
