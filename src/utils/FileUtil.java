package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	public static void listToFile(List<String> list, String filePath) throws IOException{
		listToFile(list, filePath, System.getProperty("file.encoding"));
	}
	public static void listToFile(List<String> list, String filePath, String encoding) throws IOException{
		//FileWriter fstream = new FileWriter(filePath);
		createFolderIfNotExist(filePath);
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), encoding));
		for(int i=0;i<list.size();i++){
			try {
				out.write(list.get(i));
			} catch (Exception e) {
				e.printStackTrace();
			} 
			out.write(System.getProperty("line.separator"));
		}
		out.close();
	}	
	
	public static List<String> fileToList(String filePath) throws IOException {
		return fileToList(filePath, System.getProperty("file.encoding"));
	}
	
	public static List<String> fileToList(String filePath, String encoding) throws IOException {
		List<String> list = new ArrayList<String>();
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), encoding ));
		String line = null;
		while ((line = input.readLine()) != null) {
			if (!"".equals(line.trim())) {
				list.add(line.trim());
			}
		}
		return list;

	}

	public static void copyFile(String sourceFilePath, String destFilePath) throws IOException {
		File sourceFile=new File(sourceFilePath);
		File destFile= new File(destFilePath);
		
		if(!sourceFile.exists()){
			return;
		}
		
		String destPath = destFile.getParent();
		File destFolder = new File(destPath);
		if(!destFolder.exists()){
			destFolder.mkdirs();
		}
		
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		long srcLastModified = sourceFile.lastModified();
		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
		destFile.setLastModified(srcLastModified);
	}

	
	public static String fileToString(String filePath) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}
		return stringBuilder.toString();

	}
	
	public static void createFolderIfNotExist(String filePath) throws IOException {
		File destFile= new File(filePath);
		String destPath = destFile.getParent();
		File destFolder = new File(destPath);
		if(!destFolder.exists()){
			destFolder.mkdirs();
		}
	}
}
