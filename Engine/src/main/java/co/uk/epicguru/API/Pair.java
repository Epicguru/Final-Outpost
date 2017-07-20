package co.uk.epicguru.API;

public class Pair<A, B> {

	private A a;
	private B b;
	
	public Pair() { }
	
	public Pair(A a, B b){
		this.set(a, b);
	}
	
	public Pair<A, B> setA(A a){
		this.a = a;
		return this;
	}
	
	public Pair<A, B> setB(B b){
		this.b = b;
		return this;
	}
	
	public A getA(){
		return this.a;
	}
	
	public B getB(){
		return this.b;
	}
	
	public void set(Pair<A, B> other){
		this.setA(other.getA());
		this.setB(other.getB());
	}
	
	public void set(A a, B b){
		this.setA(a);
		this.setB(b);
	}
}
