package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import containers.Directions.Coordinate;

public class UtilTest {
	
	@Test
	public void testGetMatlabIndex() {
		List<String> list = Arrays.asList("one", "two");
		assertEquals(1, Util.getMatlabIndex("one", list));	
		assertEquals(2, Util.getMatlabIndex("two", list));
	}
	
	@Test
	public void testGetJavaIndex() {
		List<String> list = Arrays.asList("one", "two");
		assertEquals(0, Util.getJavaIndex("one", list));	
		assertEquals(1, Util.getJavaIndex("two", list));
	}
	
	@Test
	public void testWriteFile() throws IOException {
		String path = "test.txt";
		String expected = "Some data";
		Util.writeFile(path, expected);
		List<String> actual = Files.readAllLines(Paths.get(path));
		assertEquals(expected, actual.get(0));
		Files.delete(Paths.get(path));
	}
	
	@Test
	public void testReadFile() throws IOException {
		String path = "test.txt";
		String expected = "Some data";
		Files.write(Paths.get(path), expected.getBytes());
		List<String> actual = Util.readFile(path);
		assertEquals(expected, actual.get(0));
		Files.delete(Paths.get(path));
	}
	
	@Test
	public void testGetFileNameOnly() {
		assertEquals("test.png", Util.getFileNameOnly("dir1/dir2/test.png"));
		assertEquals("test.png", Util.getFileNameOnly("dir1/test.png"));
		assertEquals("test.png", Util.getFileNameOnly("test.png"));
		assertEquals("", Util.getFileNameOnly(""));
	}
	
	@Test
	public void testRemoveFileExtension() {
		assertEquals("dir1/dir2/test", Util.removeFileExtension("dir1/dir2/test.png"));
		assertEquals("dir1/test", Util.removeFileExtension("dir1/test.png"));
		assertEquals("test", Util.removeFileExtension("test.png"));
		assertEquals("", Util.removeFileExtension(""));
	}
	
	@Test
	public void testDeleteOldPathImages() {
		Util.writeFile("src/main/resources/static/tmp/file1_path.jpg", "file1");
		Util.writeFile("src/main/resources/static/tmp/file2_path.jpg", "file2");
		assertTrue(Files.exists(Paths.get("src/main/resources/static/tmp/file1_path.jpg")));
		assertTrue(Files.exists(Paths.get("src/main/resources/static/tmp/file2_path.jpg")));
		List<String> originalFileNames = Arrays.asList("file1.jpg", "file2.jpg");
		Util.deleteOldPathImages(originalFileNames);
		assertFalse(Files.exists(Paths.get("src/main/resources/static/tmp/file1_path.jpg")));
		assertFalse(Files.exists(Paths.get("src/main/resources/static/tmp/file2_path.jpg")));
	}
	
	@Test
	public void testGetPathImageFileName() {
		assertEquals("file_path.jpg", Util.getPathImageFileName("file.jpg"));
	}
	
	@Test
	public void testMakeCoord() {
		final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
		ObjectNode node = nodeFactory.objectNode();
		node.put("filename", "file.png");
		node.put("xcoord", 5);
		node.put("ycoord", 7);
		List<String> possibleFileNames = Arrays.asList("random.png", "random2.png", "random3.png", "file.png");
		Coordinate actual = Util.makeCoord(node, possibleFileNames);
		assertEquals(5, actual.getX());
		assertEquals(7, actual.getY());
		assertEquals(3, actual.getIndex());
	}
	
	
	@Test
	public void testGetFileNames() {
		String filenames = "file1.png, file2.png";
		List<String> expected = Arrays.asList("file1.png", "file2.png");
		assertEquals(expected, Util.getFileNames(filenames));
	}
	
	@Test
	public void testgetFileNameString() {
		String[] original = {"file1.png", "file2.png"};
		String expected = "file1.png,file2.png";
		String actual = Util.getFileNamesString(original);
		assertEquals(expected, actual);
	}

}
