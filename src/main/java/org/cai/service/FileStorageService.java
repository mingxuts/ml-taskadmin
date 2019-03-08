package org.cai.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.cai.exception.FileStorageException;
import org.cai.exception.FileWritingException;
import org.cai.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


@Service
public class FileStorageService {

	private final Path fileStorageLocation;
	
    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getTaskdir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
    public String saveText(String directory, String filename, String content) throws RuntimeException {
    	createPath(directory);
    	String dir = directory;
    	String sFilename = dir + File.separator + filename;
    	Path p = fileStorageLocation.resolve(sFilename);
    	try {
			Files.write(p, content.getBytes());
			System.out.println(p.toString());
			return p.toString();
		} catch (Exception ex) {
			String m = "writing into path: " + p + " failed";
			throw new FileWritingException(m, ex);
		}
    }
    
    private void createPath(String directory) {
    	String dir = directory;
    	
    	Path p = fileStorageLocation.resolve(dir);
    	
    	File d = p.toFile();
    	if (!d.exists()){
    	
	        try {
	            Files.createDirectories(p);
	        } catch (Exception ex) {
	            throw new FileStorageException("Could not create the directory where the task files will be stored.", ex);
	        }    	
    	}
    }

	public Resource loadFileAsResource(String taskid) {
		// TODO Auto-generated method stub
		return null;
	}
}
