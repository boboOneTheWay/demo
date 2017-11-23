package thinkinjava.collections.setTest;

import java.util.HashSet;
import java.util.Set;

public class LinkedHashSetTest<E>
extends HashSet<E>
implements Set<E>, Cloneable, java.io.Serializable {

	public LinkedHashSetTest(int initialCapacity, float loadFactor) {
        //super(initialCapacity, loadFactor, true);
    }
	
	public LinkedHashSetTest(int initialCapacity) {
        //super(initialCapacity, .75f, true);
    }
	
	 public LinkedHashSetTest() {
	        //super(16, .75f, true);
	    }

	private static final long serialVersionUID = 1L;

}
