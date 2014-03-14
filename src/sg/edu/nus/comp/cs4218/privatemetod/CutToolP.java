package sg.edu.nus.comp.cs4218.privatemetod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

import sg.edu.nus.comp.cs4218.extended2.ICutTool;
import sg.edu.nus.comp.cs4218.impl.ATool;
import sg.edu.nus.comp.cs4218.impl.fileutils.CatTool;
/*
 * 
 * Args[0] = cut;
 * Args[1] = -c;
 * Args[2] = List;
 * Args[3] = file
 * 
 * Args[0] = cut;
 * Args[1] = -c;
 * Args[2] = List;
 * Args[3] = -d;
 * Args[4] = delim;
 * Args[5] = file;
 *
 */
public class CutToolP extends ATool implements ICutTool {
    public CutToolP(String[] args) {
		super(args);
		if (!args[0].equals("cut")) {
			setStatusCode(127);
		}
	}

	@Override
	public String execute(File workingDir, String stdin) {
		final String DELIM = "-d";
		final String DASH = "-";
		final String LIST = "-c";
		boolean haveDelim = false;
		String input;
		String returnvalue = "" ;
		CatTool catTool = new CatTool(new String[]{"cat"});
		int filePosistion = 3;
		if(args.length<4){
			setStatusCode(127);
			return "";
		}
		if(args.length == 6){
			if(args[3].equalsIgnoreCase(DELIM)){
				filePosistion = 5;
				haveDelim = true;
			}
			else { 
				setStatusCode(127);
			}
		}
		if(args[filePosistion].equalsIgnoreCase(DASH)){
			input = stdin;
		} else {
			input =catTool.getStringForFile(new File (args[filePosistion]));
		}
		if(!args[1].equalsIgnoreCase(LIST)){
			setStatusCode(127);
		}
		
		if(haveDelim){
			returnvalue = cutSpecifiedCharactersUseDelimiter(args[2], args[4], input);
		}
		else {
			returnvalue = cutSpecfiedCharacters(args[2], input);
		}
		
		return returnvalue;
	}



	@Override
	public String cutSpecfiedCharacters(String list, String input) {
		if(list == null || input ==null){
			setStatusCode(210);
			return "";
		}
		StringBuilder endResult = new StringBuilder();
		boolean[] possitions = listToPossitions(list, input.length());
		for(int i=1; i<possitions.length; i++){
			if(possitions[i]){
				endResult.append(input.charAt(i-1));
			}
		}
		return endResult.toString();
	}
	
	@Override
	public String cutSpecifiedCharactersUseDelimiter(String list, String delim,
			String input) {
		if(list == null || input ==null || delim==null){
			setStatusCode(210);
			return "";
		}
		String splitdelim = java.util.regex.Pattern.quote(delim); 
		StringBuilder endResult = new StringBuilder();
		String[] fieldArray = input.split(splitdelim);
		boolean[] possitions = listToPossitions(list, fieldArray.length);
		for(int i=1; i<possitions.length; i++){
			if(possitions[i]){
				endResult.append(fieldArray[i-1]);
				endResult.append(delim);
			}
		}
		if(endResult.length()>0){
		endResult.replace(endResult.length()-delim.length(), endResult.length(), "");
		}
		return endResult.toString();
	}

	
	public boolean[] listToPossitions(String list, int inputLenght){
		LinkedList<Integer> allBlockNumbers = new LinkedList<Integer>();
		boolean[] output = new boolean[inputLenght+1];
		String[] partsOfLineToPrint = list.split(",");
		for(String currentPart  : partsOfLineToPrint){
			allBlockNumbers.addAll(parseString(currentPart));
		}
		int blockToKeep;
		while(!allBlockNumbers.isEmpty()){
			blockToKeep = allBlockNumbers.remove();
			if(blockToKeep<output.length){
				output[blockToKeep]= true;
			}
		}
		if(getStatusCode() == 0){
			return output;
		}
		return new boolean[inputLenght+1];
	}
    /**
     * Parameter a string on fromation [d]|[d1-d2] where d is a int
     * output a linkedlist with Integers d or d1-d2
     */
	public LinkedList<Integer> parseString(String currentPart) {
		String[] partsOfString= currentPart.split("-");
		Integer firstPart;
		LinkedList<Integer> returnList = new LinkedList<Integer>();
		firstPart = parseSingelNumber(partsOfString[0]);
		if (firstPart == -1) {
			returnList.clear();
			return returnList;
		}
		if(partsOfString.length==1){
			returnList.add(firstPart);
		}
		else if(partsOfString.length==2){
			Integer secondPart=parseSingelNumber(partsOfString[1]);
			if (secondPart == -1) {
				returnList.clear();
				return returnList;
			}
			while(firstPart<=secondPart){
				returnList.add(firstPart);
				firstPart++;
			}
		}
		else {
			setStatusCode(67);
		}
		return returnList;
	}
	/**
	 * Check if the string can be parsed to a int > -1 
	 * @param number
	 * @return number as int 
	 * @return -1 if number is not an int 
	 */
	
	public Integer parseSingelNumber(String number) {
		if(number.length() == 0){
			setStatusCode(67);
			return -1;
		}
		char tmp;
		for(int increment=0; increment<number.length(); increment++){
			tmp = number.charAt(increment);
			if(!Character.isDigit(tmp)){
				setStatusCode(67);
				return -1;
			}
		}
		return Integer.parseInt(number);
	}


	/**
	 * Return the helpmessage 
	 */
	@Override
	public String getHelp() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return prop.getProperty("cutHelp");
	}

}
