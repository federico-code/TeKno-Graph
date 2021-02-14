package tekno;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
	

	private List<String> files_strings = new ArrayList<String>();
	private String folder;
	public FileHelper(String folder_string) {
		this.folder = folder_string;
		this.createFileList(folder_string);
	}

	private void createFileList(String folder_string) {
		File folder = new File(folder_string);
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	try {
	        		createFileList(fileEntry.getCanonicalPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else {
	        	this.files_strings.add(folder+"/"+fileEntry.getName());
	        }
	    }
	}
	
	
	
	
	public List<String> listSourceFiles (){
		return this.files_strings;
	}
	
}
