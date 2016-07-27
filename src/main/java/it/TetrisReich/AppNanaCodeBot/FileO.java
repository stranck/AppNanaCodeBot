package it.TetrisReich.AppNanaCodeBot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class FileO {
	public static void newFile(String path){
    	File file = new File(path);
        try{
          file.createNewFile();
        }
        catch(IOException ioe){
        	System.out.println("Error while creating a new empty file :" + ioe);
        }
    }
	public static String line(String path, int n) throws FileNotFoundException, IOException{
		int i = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	i++;
		    	if(i==n) return line;
		    }
		}
		return null;
	}
	public static String reader(String path) throws IOException{
        FileReader fileReader;
        fileReader = new FileReader(path);
		BufferedReader bufferedReader = new BufferedReader( fileReader );
         
        String line = bufferedReader.readLine();
        bufferedReader.close();
        return line;
    }
	public static void writer(String text, String path){
    	try {
			File file = new File(path);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(text);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public static void delater(String path){
		try {
		    Files.delete(Paths.get(path));
		} catch (NoSuchFileException x) {
		    System.err.format("%s: no such" + " file or directory%n", path);
		} catch (DirectoryNotEmptyException x) {
		    System.err.format("%s not empty%n", path);
		} catch (IOException x) {
		    System.err.println(x);
		}
	}
	public static int addWrite(String path, String text) throws IOException{
		int i = 0;
		File fout = new File("temp");
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		    String line;
		    while ((line = br.readLine()) != null) {
		    	i++;
		    	bw.write(line);
		    	bw.newLine();
		    }
		    bw.write(text);
		    i++;
			bw.close();
		}
		delater(path);
		fout.renameTo(new File(path));
		return i;
	}
	public static int countLines(String path) throws IOException{
		return countLines1(path) + 1;
	}
	public static int countLines1(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	public static boolean isEmpty(String path) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		if(br.readLine() == null) {br.close();return false;}
		br.close();
		return true;
	}
	public static void removeWrite(String path) throws IOException{
		File fout = new File("temp");
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		    String line;
		    boolean first = true;
		    while ((line = br.readLine()) != null) {
		    	if(first) first = false; else {bw.write(line);
		    	bw.newLine();}
		    }
			bw.close();
		}
		delater(path);
		fout.renameTo(new File(path));
	}
}
