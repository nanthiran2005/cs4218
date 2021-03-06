package sg.edu.nus.comp.cs4218.impl.extended2;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.extended2.ICommTool;
/**
 * BUG_ID: 29
 * Fix location 1: Line 44, CommTool.java
 * Fix location 2: Line 90, CommTool.Java
 * Fix location 3: Line 143, CommTool.Java
 * There are a lot of changes in CommTool.Java.
 */
public class TestBug29CommTool {
	private static ICommTool commTool; 
	private static File file1, file5;
	private static File workingDir = new File(System.getProperty("user.dir"));

	@BeforeClass 
	public static void executeThisBeforeClass() throws IOException{
		//testFile 1 will be the file in sorted order
		file1 = File.createTempFile("commfile", "sorted", workingDir);
		Common.writeFile(
				file1, 
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"ddd");
		
		file5 = File.createTempFile("commfile", "empty", workingDir);
		Common.writeFile(file5, "");
	}

	@AfterClass 
	public static void executeThisAfterClass(){
		file1.delete();
		file5.delete();
	}

	@Before
	public void before() throws IOException{
		commTool = new CommTool(new String[]{"comm"});
	}

	@After
	public void after(){
		commTool = null;
	}
	
	/**
	 * Compare two files with second file empty
	 */
	@Test
	public void compareFilesSecondFileEmpty() {
		String result = commTool.compareFiles(
				file1.getAbsolutePath(), 
				file5.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+
				"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+
				"ddd"+System.lineSeparator(),
				result);
	}
	
	/**
	 * Compare two files with first file empty
	 */
	@Test
	public void compareFilesFirstFileEmpty() {
		String result = commTool.compareFiles(
				file5.getAbsolutePath(), 
				file1.getAbsolutePath());
		assertEquals(
				"\taaa"+System.lineSeparator()+
				"\tbbb"+System.lineSeparator()+
				"\tccc"+System.lineSeparator()+
				"\tddd"+System.lineSeparator(),
				result);
	}

}
