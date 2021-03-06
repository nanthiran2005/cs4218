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

public class COMMToolTest {
	private static ICommTool commTool; 
	private static File file1, file2, file3, file4, file5;
	private static File workingDir = new File(System.getProperty("user.dir"));

	@BeforeClass 
	public static void executeThisBeforeClass() throws IOException{
		//testFile 1 will be the file in sorted order
		file1 = File.createTempFile("commfile", "sorted", workingDir);
		Common.writeFile(
				file1, 
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"ddd");
		
		//testFile 2 will be the file in sorted order
		file2 = File.createTempFile("commfile", "sorted", workingDir);
		Common.writeFile(
				file2, 
				"aaf"+System.lineSeparator()+"abb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"fff");

		//testFile 3 will be the file in unsorted order 
		file3 = File.createTempFile("commfile", "unsorted", workingDir);
		Common.writeFile(
				file3, 
				"zzz"+System.lineSeparator()+"ccc"+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"bbb");
		
		//testFile 4 will be the file in unsorted order 
		file4 = File.createTempFile("commfile", "unsorted", workingDir);
		Common.writeFile(
				file4, 
				"aaa"+System.lineSeparator()+"ecc"+System.lineSeparator()+
				"eaa"+System.lineSeparator()+"zbb");
		
		file5 = File.createTempFile("commfile", "empty", workingDir);
		Common.writeFile(file5, "");
	}

	@AfterClass 
	public static void executeThisAfterClass(){
		file1.delete();
		file2.delete();
		file3.delete();
		file4.delete();
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

	//test compareFiles method, with sorted file
	@Test
	public void compareFilesSortedFile() {
		String result = commTool.compareFiles(
				file1.getAbsolutePath(), 
				file2.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"\taaf"+System.lineSeparator()+
				"\tabb"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"\t\tccc"+System.lineSeparator()+"ddd"+System.lineSeparator()+
				"\tfff"+System.lineSeparator(),
				result);
	}

	/**
	 * test compareFiles method, with first file unsorted
	 */
	@Test
	public void compareFilesUnSortedFile1() {
		String result = commTool.compareFiles(
				file3.getAbsolutePath(), 
				file1.getAbsolutePath());
		assertEquals(
				"comm: File 1 is not in sorted order "+System.lineSeparator()+
				"\taaa"+System.lineSeparator()+"\tbbb"+System.lineSeparator()+
				"\tccc"+System.lineSeparator()+"\tddd"+System.lineSeparator()+
				"zzz"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator(),
				result);
	}
	
	/**
	 * test compareFiles method, with second file unsorted
	 */
	@Test
	public void compareFilesUnSortedFile2() {
		String result = commTool.compareFiles(
				file1.getAbsolutePath(), 
				file3.getAbsolutePath());
		assertEquals(
				"comm: File 2 is not in sorted order " + System.lineSeparator()+
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"ddd"+System.lineSeparator()+
				"\tzzz"+System.lineSeparator()+
				"\tccc"+System.lineSeparator()+
				"\taaa"+System.lineSeparator()+"\tbbb"+System.lineSeparator(),
				result);
	}
	
	/**
	 * test compareFiles method, with non-existing file
	 * @throws IOException
	 */
	@Test
	public void compareNonExistingFiles() throws IOException {
		File nonExist = File.createTempFile("commNonExist", "");

		nonExist.delete();
		assertEquals(false, nonExist.exists());
		String result = commTool.compareFiles(
				file1.getAbsolutePath(), 
				nonExist.getAbsolutePath());
		assertEquals("", result);
		assertEquals(3, commTool.getStatusCode());
	}
		
	/**
	 * test compareFilesCheckSortStatus method, with sorted files
	 * @throws IOException
	 */
	@Test
	public void compareFilesCheckSortStatusSortedFile() throws IOException { 
		String result = commTool.compareFilesCheckSortStatus(
				file1.getAbsolutePath(), 
				file2.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"\taaf"+
				System.lineSeparator()+"\tabb"+System.lineSeparator()+"bbb"+
				System.lineSeparator()+"\t\tccc"+System.lineSeparator()+"ddd"+
				System.lineSeparator()+"\tfff"+System.lineSeparator(),
				result);
	}

	/**
	 * test compareFilesCheckSortStatus method, with second file unsorted
	 * @throws IOException
	 */
	@Test
	public void compareFilesCheckSortStatusOneNotSorted() throws IOException { 
		String result = commTool.compareFilesCheckSortStatus(
				file1.getAbsolutePath(), 
				file3.getAbsolutePath());
		assertEquals(
				"comm: File 2 is not in sorted order "+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"ddd"+System.lineSeparator(),
				result);
	}

	/**
	 * test compareFilesCheckSortStatus method, neither files sorted
	 * @throws IOException
	 */
	@Test
	public void compareFilesCheckSortStatusBothNotSorted() throws IOException { 
		String result = commTool.compareFilesCheckSortStatus(
				file3.getAbsolutePath(), 
				file4.getAbsolutePath());
		assertEquals(
				"comm: File 1 is not in sorted order "+System.lineSeparator()+
				"\taaa"+System.lineSeparator()+
				"comm: File 2 is not in sorted order "+System.lineSeparator(),
				result);
	}
	
	/**
	 * test compareFilesCheckSortStatus method, with non-existing file
	 * @throws IOException
	 */
	@Test
	public void compareFilesCheckSortStatus() throws IOException {
		File nonExist = File.createTempFile("commNonExist", "");
		
		nonExist.delete();
		assertEquals(false, nonExist.exists());
		String result = commTool.compareFilesCheckSortStatus(
				nonExist.getAbsolutePath(),
				file1.getAbsolutePath() 
				);
		assertEquals("", result);
		assertEquals(3, commTool.getStatusCode());
	}
	
	/**
	 * test compareFilesDoNotCheckSortStatus method, with sorted
	 * @throws IOException
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusSortedFile() throws IOException { 

		String result = commTool.compareFilesDoNotCheckSortStatus(
				file1.getAbsolutePath(), 
				file2.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"\taaf"+System.lineSeparator()+
				"\tabb"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"\t\tccc"+System.lineSeparator()+"ddd"+System.lineSeparator()+
				"\tfff"+System.lineSeparator(),
				result);
	}

	/**
	 * test compareFilesDoNotCheckSortStatus method, with second file unsorted
	 * @throws IOException
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusNotSortedFile() throws IOException { 
		String result = commTool.compareFilesDoNotCheckSortStatus(
				file1.getAbsolutePath(), 
				file3.getAbsolutePath());
		assertEquals(
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"ddd"+System.lineSeparator()+
				"\tzzz"+System.lineSeparator()+"\tccc"+System.lineSeparator()+
				"\taaa"+System.lineSeparator()+"\tbbb"+System.lineSeparator(),
				result
				);

	}
	
	/**
	 * test compareFilesDoNotCheckSortStatus method, neither sorted
	 * @throws IOException
	 */
	@Test
	public void compareFilesDoNotCheckSortStatusNeitherSortedFile() throws IOException { 
		String result = commTool.compareFilesDoNotCheckSortStatus(
				file3.getAbsolutePath(), 
				file4.getAbsolutePath());
		assertEquals(
				"\taaa"+System.lineSeparator()+"\tecc"+System.lineSeparator()+
				"\teaa"+System.lineSeparator()+"\tzbb"+System.lineSeparator()+
				"zzz"+System.lineSeparator()+"ccc"+System.lineSeparator()+
				"aaa"+System.lineSeparator()+"bbb"+System.lineSeparator(),
				result);
	}

	/**
	 *  test getHelp
	 */
	@Test
	public void testGetHelp(){
		String commHelp="comm : Compares two sorted files line by line. "
				+ "With no options, produce three-column output. "
				+ "Column one contains lines unique to FILE1, "
				+ "column two contains lines unique to FILE2, "
				+ "and column three contains lines common to both files.\n"
				+ "Command Format - comm [OPTIONS] FILE1 FILE2 \n"
				+ "FILE1 - Name of the file 1\n"
				+ "FILE2 - Name of the file 2\n"
				+ "-c : check that the input is correctly sorted, even if all input lines are pairable \n"
				+ "-d : do not check that the input is correctly sorted\n"
				+ "-help : Brief information about supported options\n";
		assertEquals(commHelp, commTool.getHelp());
	}
	
	/**
	 * test execute method, with zero argument
	 */
	@Test
	public void testExecuteWihtZeroArg(){
		commTool = new CommTool(new String[]{});
		assertEquals("", commTool.execute(workingDir, ""));
		assertNotEquals(0, commTool.getStatusCode());
	}

	/**
	 * test execute method, with one argument "comm"
	 */
	@Test
	public void testExecuteWihtOneArgComm(){
		commTool = new CommTool(new String[]{"comm"});
		assertEquals("", commTool.execute(workingDir, ""));
		assertEquals(2, commTool.getStatusCode());
	}
		
	/**
	 * test execute method, with one argument not "comm"
	 */
	@Test
	public void testExecuteWihtOneArgNotComm(){
		commTool = new CommTool(new String[]{"COM"});
		assertEquals("", commTool.execute(workingDir, ""));
		assertNotEquals(0, commTool.getStatusCode());
	}

	/**
	 * test execute method, with "help"
	 */
	@Test
	public void testExecuteWihtTwoArgsHelp(){
		commTool = new CommTool(new String[]{"comm","-help" });
		String commHelp="comm : Compares two sorted files line by line. "
				+ "With no options, produce three-column output. "
				+ "Column one contains lines unique to FILE1, "
				+ "column two contains lines unique to FILE2, "
				+ "and column three contains lines common to both files.\n"
				+ "Command Format - comm [OPTIONS] FILE1 FILE2 \n"
				+ "FILE1 - Name of the file 1\n"
				+ "FILE2 - Name of the file 2\n"
				+ "-c : check that the input is correctly sorted, even if all input lines are pairable \n"
				+ "-d : do not check that the input is correctly sorted\n"
				+ "-help : Brief information about supported options\n";
		assertEquals(commHelp, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}

	/**
	 * test execute method, with not "help"
	 */
	@Test
	public void testExecuteWihtTwoArgsNotHelp(){
		commTool = new CommTool(new String[]{"comm","-notHelp" });
		String commHelp="comm : Compares two sorted files line by line. "
				+ "With no options, produce three-column output. "
				+ "Column one contains lines unique to FILE1, "
				+ "column two contains lines unique to FILE2, "
				+ "and column three contains lines common to both files.\n"
				+ "Command Format - comm [OPTIONS] FILE1 FILE2 \n"
				+ "FILE1 - Name of the file 1\n"
				+ "FILE2 - Name of the file 2\n"
				+ "-c : check that the input is correctly sorted, even if all input lines are pairable \n"
				+ "-d : do not check that the input is correctly sorted\n"
				+ "-help : Brief information about supported options\n";
		assertTrue(!commHelp.equals(commTool.execute(workingDir, "")));
		assertEquals(98, commTool.getStatusCode());
	}

	/**
	 * test execute method, with three arguments {"comm", "file1_path", "file2_path"}
	 */
	@Test
	public void testExecuteWihtThreeArgs(){
		commTool = new CommTool(new String[]{"comm", file1.getName(), file2.getName()});
		String result = "aaa"+System.lineSeparator()+"\taaf"+
				System.lineSeparator()+"\tabb"+System.lineSeparator()+"bbb"+
				System.lineSeparator()+"\t\tccc"+System.lineSeparator()+"ddd"+
				System.lineSeparator()+"\tfff"+System.lineSeparator();
		assertTrue(file1.exists());
		assertTrue(file2.exists());

		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}

	/**
	 * test execute method, with three arguments {"comm", "non_exist_file1_path", "non_exist_file2_path"}
	 * @throws IOException
	 */
	@Test
	public void testExecuteWihtThreeWrongArgs() throws IOException{
		File nonExist1 = File.createTempFile("comm", "nonExist");
		File nonExist2 = File.createTempFile("comm", "nonExist");
		commTool = new CommTool(new String[]{"comm", nonExist1.getAbsolutePath(), nonExist2.getAbsolutePath()});
		
		nonExist1.delete();
		nonExist2.delete();
		assertFalse(nonExist1.exists());
		assertFalse(nonExist2.exists());
		
		assertEquals("", commTool.execute(workingDir, ""));
		assertEquals(3, commTool.getStatusCode());
	}
	
	/**
	 * test execute method, with four arguments {"comm", "-c", "file2_path", "file4_path"}
	 */
	@Test
	public void testExecuteWithCheckSorted(){
		commTool = new CommTool(new String[]{"comm", "-c", file2.getName(), file4.getName()});
		String result = "\taaa"+System.lineSeparator()+
				"comm: File 2 is not in sorted order "+System.lineSeparator()+
				"aaf"+System.lineSeparator()+
				"abb"+System.lineSeparator()+"ccc"+System.lineSeparator()+
				"fff"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}
	
	/**
	 * test execute method, with four arguments {"comm", "-d", "file2_path", "file3_path"}
	 */
	@Test
	public void testExecuteWihtDoNotCheckSorted(){
		commTool = new CommTool(new String[]{"comm", "-d", file2.getName(), file3.getName()});
		
		String result = "aaf"+System.lineSeparator()+"abb"+System.lineSeparator()+
				"ccc"+System.lineSeparator()+"fff"+System.lineSeparator()+
				"\tzzz"+System.lineSeparator()+"\tccc"+System.lineSeparator()+
				"\taaa"+System.lineSeparator()+"\tbbb"+System.lineSeparator();
		assertEquals(result, commTool.execute(workingDir, ""));
		assertEquals(0, commTool.getStatusCode());
	}

	/**
	 * test execute method, with four arguments {"comm", "-d", "file2_path", "file3_path"}
	 */
	@Test
	public void testExecuteWihtWrongOption(){
		commTool = new CommTool(new String[]{"comm", "-f", "file2_path", "file4_path"});
		assertEquals("", commTool.execute(workingDir, ""));
		assertEquals(98, commTool.getStatusCode());
	}
	 
	/**
	 * test execute method, with five arguments
	 */
	@Test
	public void testExecuteWihtFiveArgs(){
		commTool = new CommTool(new String[]{"comm", "-f", "c", "file2_path", "file4_path"});
		assertEquals("", commTool.execute(workingDir, ""));
		assertEquals(2, commTool.getStatusCode());
	}
}
