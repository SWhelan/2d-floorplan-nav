package containers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MustacheListItemTest {
	
	@Test
	public void testMakeList() {
		List<String> input = Arrays.asList("One", "Two", "Three");
		List<MustacheListItem> actual = MustacheListItem.makeList(input);
		assertEquals(input.get(0), actual.get(0).string);
		assertEquals(0, actual.get(0).index);
		assertEquals(true, actual.get(0).evenIndex);
		assertEquals(input.get(1), actual.get(1).string);
		assertEquals(1, actual.get(1).index);
		assertEquals(false, actual.get(1).evenIndex);
		assertEquals(input.get(2), actual.get(2).string);
		assertEquals(2, actual.get(2).index);
		assertEquals(true, actual.get(2).evenIndex);
	}
	
	@Test
	public void testMakeListEmpty() {
		List<String> input = Arrays.asList();
		List<MustacheListItem> actual = MustacheListItem.makeList(input);
		assertEquals(0, actual.size());
	}
}
