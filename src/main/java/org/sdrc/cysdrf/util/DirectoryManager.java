package org.sdrc.cysdrf.util;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class DirectoryManager {

	public void createDirectory(){
		String username = System.getProperty("user.name");
		System.out.println(username);
//		String filePath = "C:\\Users\\"+username+"\\AppData\\cysdrfNewDirectory";
		String filePath = "D:/cysd/template";
		String uploadFilePath = "D:/cysd/uploadedFiles";
		
		File file = new File(filePath);
		File file2 = new File(uploadFilePath);
		if(!file.exists()){
			if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
		}
		if(!file2.exists()){
			if (file2.mkdir()) {
                System.out.println("Directory2 is created!");
            } else {
                System.out.println("Failed to create directory2!");
            }
		}
		
	}
}
