package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Listener {
	
	public static final String logPath = "/home/rylai/Development/logs/";

	public static void main(String[] args) throws Exception {
		
		List<String> successMessages = new ArrayList<String>();
		successMessages.add("heyo");

		String []command = new String[] {"java", "-jar", "executable.jar"};
		ProcessBuilder processbuilder = new ProcessBuilder(command);
		processbuilder.directory(new File("/home/rylai/Development/"));
		
		 
        Process process = processbuilder.start();
        
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReaderForInput = new BufferedReader(inputStreamReader);
        
        
        InputStream errorStream = process.getErrorStream();
        InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReaderForError = new BufferedReader(errorStreamReader);
        
        
        String lineForInput = "";
        String lineForError = "";
        
        System.out.printf("Output of running %s is:\n", Arrays.toString(command));
        
        String fileName = UUID.randomUUID().toString();
        
		File fout = new File(logPath+fileName+".txt");
        
    	FileOutputStream fos = new FileOutputStream(fout);
     
    	BufferedWriter bufferWritter = new BufferedWriter(new OutputStreamWriter(fos));
        
        boolean hasSuccessMessage = false;
        boolean errorExists = false;
        
        while ((lineForInput = bufferedReaderForInput.readLine()) != null 
        		|| (lineForError = bufferedReaderForError.readLine()) != null) {
            
        	if(lineForInput != null && !lineForInput.isEmpty()) {
        		bufferWritter.write("lineForInput -> " +lineForInput);
        		bufferWritter.newLine();
        		
        		if(successMessages.contains(lineForError)) {
        			hasSuccessMessage = true;
        		}
        	}
        	
        	if(lineForError != null && !lineForError.isEmpty()) {
        		bufferWritter.write("lineForError -> " +lineForError);
        		bufferWritter.newLine();
        		
        		errorExists = true;
        	}

        	bufferWritter.flush();
        }
        
        bufferWritter.close();
        
        //Wait to get exit value (0 successful, 1 fail)
        try {
            int exitValue = process.waitFor();
            System.out.println("\n\nExit Value is " + exitValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        if(hasSuccessMessage && !errorExists) {
        	System.out.println("Everything is ok...");
        } else {
        	System.out.println("Something is wrong...");
        	throw new Exception("Test Exception!");
        }

	}

}
