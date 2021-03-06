package sg.edu.nus.comp.cs4218.impl.fileutils;

import java.io.File;
import java.nio.file.Paths;

import sg.edu.nus.comp.cs4218.common.Common;
import sg.edu.nus.comp.cs4218.fileutils.IDeleteTool;
import sg.edu.nus.comp.cs4218.impl.ATool;

public class DeleteTool extends ATool implements IDeleteTool {
	public DeleteTool(String[] arguments) {
		super(arguments);
		if (args == null || args.length == 0 || !args[0].equals("delete")) {
			setStatusCode(127);
		}
	}

	/**
	 * delete a file
	 * @param	toDelete	file to be deleted
	 * @return true if it deletes successfully
	 */
	@Override
	public boolean delete(File toDelete) {
		if(toDelete != null && toDelete.delete()){
			setStatusCode(0);
			return true;
		}else{
			setStatusCode(1);
			return false;
		}
	}
	
	/**
	 * The general go-to method for using the tool that calls
	 * the suitable submethods.
	 * @param workingDir current working directory
	 * @param stdin optional standard input from e.g. pipe tool
	 * @return output
	 */
	@Override
	public String execute(File workingDir, String stdin) {
		if (workingDir == null || args.length==1){
				setStatusCode(1);
		}else{
			StringBuilder returnMsg = new StringBuilder();
			int status = 0;
			for (int i=1; i<args.length; i++){
				String filePath = null;
				if (args[i]!=null && Paths.get(args[i]).isAbsolute()){
					filePath = args[i];
				}else{
					filePath = Common.concatenateDirectory(workingDir.getAbsolutePath(), args[i]);
				}
				if (!delete(new File(filePath))){
					status = 1;
					returnMsg.append("Error: Cannot delete " + args[i]);
				}
			}
			if (status == 1) {
				setStatusCode(1);
			}
			if (returnMsg.toString().length() > 0){
				return returnMsg.toString();
			}
		}
		return "";
	}
}
