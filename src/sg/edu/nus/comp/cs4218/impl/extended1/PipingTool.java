package sg.edu.nus.comp.cs4218.impl.extended1;

import java.io.File;

import sg.edu.nus.comp.cs4218.ITool;
import sg.edu.nus.comp.cs4218.extended1.IPipingTool;

public class PipingTool implements IPipingTool {
	private static final int ZERO = 0;
	private static final int ONE = 1;
	private static final int TWO = 2;
	ITool[] arguments;
	File workingDir;
	int statuscode=ZERO;
	public PipingTool(ITool[] arguments){
		this.arguments = arguments;
		workingDir=getWorkingdir();
	}

	@Override
	public String execute(File workingDir, String stdin) {
		String result = pipe(arguments[ZERO], arguments[ONE]);

		for(int loopValue=TWO; loopValue<arguments.length && statuscode==ZERO; loopValue++){
			result = pipe(result, arguments[loopValue]);
		}
		return result;
	}

	@Override
	public int getStatusCode() {
		return statuscode;

	}
	@Override
	public String pipe(ITool from, ITool to) {
		String result = from.execute(workingDir, null);
		int statusCodeFrom = from.getStatusCode();
		int statusCodeTo=ZERO;
		System.out.println(result);
		if(statusCodeFrom!=ZERO){
			result ="";
			setStatusCode(statusCodeFrom);
			statusCodeTo=statusCodeFrom;
		} else if(statusCodeFrom==ZERO) {
			result = to.execute(workingDir, result);
			statusCodeTo = to.getStatusCode();
		}
		if(statusCodeTo!=ZERO){
			result="";
			setStatusCode(statusCodeTo);
		}
		return result;
	}


	@Override
	public String pipe(String stdout, ITool to) {
		String result =to.execute(workingDir, stdout);
		int statusCodeTo = to.getStatusCode();
		if(statusCodeTo!=ZERO){
			result="";
			setStatusCode(statusCodeTo);
		}
		return result;
	}
	
	private void setStatusCode(int value){
		statuscode =value;
	}

	private File getWorkingdir(){
		String workdir = System.getProperty( "user.dir" );
		return  new File(workdir);
	}

	@Override
	public boolean equals(Object object){
		PipingTool pipe =(PipingTool) object;
		if(arguments.length!=pipe.arguments.length){
			return false;
		}
		for (int i = 0; i < arguments.length; i++) {
			if(!arguments[i].equals(pipe.arguments[i])){
				return false;
			}
		}
		return true;
		
	}

}
