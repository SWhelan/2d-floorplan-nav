package service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import containers.Directions;
import containers.Directions.Coordinate;

public class MatlabServiceTest {
	private MatlabService matlabService;
	private MatlabService.Hook testHook;
	
	private static final List<String> filenames = Arrays.asList("file1", "file2");
	
	@Before
	public void init() {
		matlabService = new MatlabService();
		testHook = matlabService.new Hook(); 
	}
	
	@Test
	public void testGenerateRunCommand() {
		String expected = "run('src\\matlab\\DotGrid(3, 2, 1, 3, 4, 2)');";
		String actual = testHook.generateRunCommand(3, 2, "file1", 3, 4, "file2", filenames);
		assertEquals(expected, actual);
	}

	@Test
	public void testMakeFileNamesFile() {
		testHook.makeFilenamesFile(filenames);
		List<String> expected = Arrays.asList("..\\main\\resources\\static\\tmp\\file1", "..\\main\\resources\\static\\tmp\\file2");
		List<String> actual = Util.readFile("src/matlab/filenames.txt");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeExcludePointsFile() {
		List<Coordinate> excludePoints = Arrays.asList(new Coordinate(1, 4, 5), new Coordinate(1, 3, 4));
		testHook.makeExcludePointsFile(excludePoints);
		List<String> expected = Arrays.asList("2,4,5","2,3,4");
		List<String> actual = Util.readFile("src/matlab/exclude_points.txt");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testExecuteCommand() {
		String[] command = {"ls"};
		String expected = "pom.xml\nsrc\ntarget\n";
		String actual = testHook.executeCommand(command);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testMakeMatlabCommand() {
		Directions directions = new Directions();
		directions.setA(new Coordinate(0, 5, 6), "file1");
		directions.setB(new Coordinate(1, 3, 6), "file1");
		directions.setOriginalFileNames(Arrays.asList("file1.png", "file2.png"));
		String[] expected = {"\"E:\\matlab2016\\bin\\matlab.exe\"", "-nodisplay", "-nosplash", "-nodesktop", "-wait", "-r", "run('src\\matlab\\DotGrid(5, 6, 1, 3, 6, 1)');"};
		String[] actual = testHook.makeMatlabComand(directions);
		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
		assertEquals(expected[2], actual[2]);
		assertEquals(expected[3], actual[3]);
		assertEquals(expected[4], actual[4]);
		assertEquals(expected[5], actual[5]);
		assertEquals(expected[6], actual[6]);
	}
	
	@Test
	public void testSavePathToDirections() {
		Util.writeFile("src/matlab/path.txt", "4,5,1\\n5,26,1");
		Directions directions = new Directions();
		directions.setOriginalFileNames(Arrays.asList("file1.png", "file2.png"));
		testHook.savePathToDirections(directions);
		List<String> expected = Arrays.asList("tmp/file1.png", "tmp/file2.png");
		List<String> actual = directions.getAfterPathFileNames();
		assertEquals(expected, actual);		
	}
}

