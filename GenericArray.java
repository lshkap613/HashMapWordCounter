

public class GenericArray<T> {
	private T[] genericArray;
	
	// Constructor
	@SuppressWarnings("unchecked")
	public GenericArray(int size) {
		this.genericArray = (T[])new Object[size];
	}
	
	// get method
	public T get(int index) {
		return (T) genericArray[index];
	}
	
	// set method
	public void set(int index, T value) {
		this.genericArray[index] = value;
	}
	
	// size() method returns current size of array
	public int size() {
		return this.genericArray.length;
	}
	// isEmpty method returns true if array
	// is empty, false if otherwise
	public boolean isEmpty() {
		return this.genericArray.length == 0;
	}
}
