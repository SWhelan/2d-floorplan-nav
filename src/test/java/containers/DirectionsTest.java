package containers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import containers.Directions.Coordinate;

public class DirectionsTest {
	private Directions directions;
	private Directions.Hook testHook;
	
	private static List<String> steps;
	private static List<Coordinate> coords;
	private static List<String> pretty;
	
	@Before
	public void setUp() {
		directions = new Directions();
		testHook = directions.new Hook();
		steps = new ArrayList<>(Arrays.asList("1,2,1", "3,3,1", "4,5,1", "5,6,1"));
		coords = new ArrayList<>(Arrays.asList(new Coordinate(1, 1, 2), new Coordinate(1, 3, 3), new Coordinate(1, 4, 5), new Coordinate(1, 5, 6)));
		pretty = new ArrayList<>(Arrays.asList("Assuming that north is up", "Start", "Go southeast 1 unit", "Go southeast 1 unit", "End"));
	}
	
	@Test
	public void testPreprocessForResponseTest() {
		directions.setSteps(steps);
		directions.preprocessForResponse();
		List<String> prettySteps = directions.getPrettySteps();
		assertEquals(5, prettySteps.size());
		assertEquals("Go southeast 1 unit", prettySteps.get(2));
	}
	
	@Test
	public void testGenerateCoordsTest() {
		List<Coordinate> actual = testHook.generateCoords(steps);
		assertTrue(coords.get(0).equals(actual.get(0)));
		assertTrue(coords.get(1).equals(actual.get(1)));
		assertTrue(coords.get(2).equals(actual.get(2)));
		assertTrue(coords.get(3).equals(actual.get(3)));
	}
	
	@Test
	public void testGeneratePrettyStepsTest() {
		List<String> steps = testHook.generatePrettySteps(coords);
		assertEquals(pretty.get(0), steps.get(0));
		assertEquals(pretty.get(1), steps.get(1));
		assertEquals(pretty.get(2), steps.get(2));
		assertEquals(pretty.get(3), steps.get(3));
		assertEquals(pretty.get(4), steps.get(4));
	}
	
	@Test
	public void testGeneratePrettyStepTest() {
		Coordinate coord1 = new Coordinate(1, 0, 0);
		Coordinate coord2 = new Coordinate(1, -1, 0);
		assertEquals("Go west 1 unit", testHook.generatePrettyStep(coord1, coord2));
	}
}
