package sg.edu.nus.comp.cs4218.impl.extended1;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.impl.Shell;
import sg.edu.nus.comp.cs4218.impl.extended1.PipingTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
import sg.edu.nus.comp.cs4218.impl.fileutils.EchoTool;
import sg.edu.nus.comp.cs4218.impl.extended1.GrepTool;

public class TestPipeingTool {

	private static Properties prop;
	private File testfile;
	static String pipeTestFile ="pipetestFile";
	String[] empty = new String[] {"cat", pipeTestFile};
	ITool cat = new CatTool(empty);
	ITool[] set = new ITool[2];
	PipingTool pipe;
	public TestPipeingTool(){
		
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		prop = new Properties();
		try {
			prop.load(new FileInputStream("parsing.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		File testfile = new File(pipeTestFile);
		if (!testfile.isFile()) {
		PrintWriter writer = new PrintWriter(pipeTestFile, "UTF-8");
		writer.println(prop.getProperty("cat"));
		writer.close();
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.delete(Paths.get(pipeTestFile));
	}
	
	@Before
	public void setup(){
	}
	
	/**
	 * Test echo|cat 
	 */
	@Test
	public void canPipeTwoITools(){
		String[] empty = new String[] {"cat", "-"};
		ITool cat = new CatTool(empty);
		ITool[] set = new ITool[2];
		String[] a = new String[] {"echo", "pipe.properties"};
		ITool echo = new EchoTool(a);
		set[1]=cat;
		set[0]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.pipe(set[0], set[1]);
		assertEquals("Pipeing failed", "pipe.properties"+System.lineSeparator(), actual);
		assertEquals("Statuscode", 0, pipe.getStatusCode());
	}
	
	/**
	 * Test pipe with input 
	 */
	@Test
	public void canPipeOneElement(){
		String[] empty = new String[] {"cat", "-"};
		ITool cat = new CatTool(empty);
		ITool[] set = new ITool[1];
		set[0]=cat;
		pipe = new PipingTool(set);
		String result= "Hallo world";
		String actual = pipe.pipe(result, set[0]);
		assertEquals("Pipeing failed", result, actual);	
		assertEquals("Statuscode", 0, pipe.getStatusCode());
	}
	/**
	 * test pipe with two inputs 
	 */
	@Test
	public void excecuteWithTwoTools(){
		String[] empty = new String[] {"cat", "-"};
		ITool cat = new CatTool(empty);
		ITool[] set = new ITool[2];
		String[] a = new String[] {"echo", "pipe.properties"};
		ITool echo = new EchoTool(a);
		set[1]=cat;
		set[0]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.execute(pipe.workingDir, null);
		assertEquals("Pipeing failed", "pipe.properties"+System.lineSeparator(), actual);
		assertEquals("Statuscode", 0, pipe.getStatusCode());
	}
	/**
	 * Test echo|cat|cat 
	 */
	@Test
	public void excecuteWithThreeTools(){
		String[] empty = new String[] {"cat", "-"};
		ITool cat = new CatTool(empty);
		ITool[] set = new ITool[3];
		String[] a = new String[] {"echo", "pipe.properties"};
		ITool echo = new EchoTool(a);
		set[2]=cat;
		set[1]=cat;
		set[0]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.execute(pipe.workingDir, null);
		assertEquals("Pipeing failed", "pipe.properties"+System.lineSeparator(), actual);
		assertEquals("Statuscode", 0, pipe.getStatusCode());
	}
	
	/**
	 * Test how it works to pass statuscodes 
	 */
	@Test 
	public void passingOfsatusCode(){
		String[] empty = new String[] {"cat", "-"};
		ITool cat = new CatTool(empty);
		ITool[] set = new ITool[3];
		String[] a = new String[] {"echo", null};
		ITool echo = new EchoTool(a);
		set[2]=cat;
		set[1]=cat;
		set[0]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.execute(pipe.workingDir, null);
		assertEquals("Pipeing failed", "", actual);
		assertTrue("Statuscode", 0 !=pipe.getStatusCode());
	}
	/**
	 * Test that the pipingtool need more then 1 tool.
	 */
	@Test 
	public void failOnOneITools(){
		String[] a = new String[] {"echo", null};
		ITool echo = new EchoTool(a);
		ITool[] set = new ITool[1];
		set[0]=echo;
		pipe = new PipingTool(set);
		String actual = pipe.execute(pipe.workingDir, null);
		assertEquals("Pipeing failed", "", actual);
		assertTrue("Statuscode", 0 !=pipe.getStatusCode());
	}
	
	/**
	 * Test what happens if null in Tools
	 */
	@Test 
	public void nullInITools(){
		ITool[] set = new ITool[2];
		set[0]=null;
		set[1]=null;
		pipe = new PipingTool(set);
		String actual = pipe.execute(pipe.workingDir, null);
		assertEquals("Pipeing failed", "", actual);
		assertTrue("Statuscode", 0 !=pipe.getStatusCode());
	}
		
}
