package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class UploadFileServiceTest {
	private UploadFileService service;
	private UploadFileService.Hook testHook;
	
	@Before
	public void setUp() {
		service = new UploadFileService();
		testHook = service.new Hook();
	}
	
	@Test
	public void testEndOfInputStream() {
		assertTrue(testHook.endOfInputstream(-1));
		assertFalse(testHook.endOfInputstream(0));
		assertFalse(testHook.endOfInputstream(1));
	}
	
	@Test
	public void testGetFileBytes() throws IOException {
		byte[] expected = "testing stuff-1".getBytes();
		InputStream stream = new ByteArrayInputStream(expected);
		byte[] actual = testHook.getFileBytes(stream);
		assertEquals(expected.length, actual.length);
	}

}
