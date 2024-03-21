
class MapEntry<K, V>{
	protected K key;
	protected V value;
	protected MapEntry<K, V> next;
	
	public MapEntry(K key, V value) {
		this.key = key;
		this.value = value;
		this.next = null;
	}
	
	public K getKey() {return key;};
	public V getValue() {return value;};
	public MapEntry<K, V> getNext() {return next;}
	
	public void setValue(V value) {this.value = value;}
	public void setNext(MapEntry<K, V> next) {this.next = next;}
	
	@Override
	public String toString() {
		return "Key: " + key + " Value: " + value;
	}
}
