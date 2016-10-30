package containers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MustacheListItem {
	public String string;
	public int index;
	public boolean evenIndex;
	
	public MustacheListItem(String string, int index) {
		super();
		this.string = string;
		this.index = index;
		this.evenIndex = index % 2 == 0;
	}

	public static List<MustacheListItem> makeList(List<String> strings) {
		List<MustacheListItem> temp = new ArrayList<>();
		int index = 0;
		Iterator<String> itr = strings.iterator();
		while(itr.hasNext()) {
			temp.add(new MustacheListItem(itr.next(), index));
			index++;
		}
		return temp;
	}
}
