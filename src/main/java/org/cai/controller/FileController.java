package org.cai.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.cai.property.FileStorageProperties;
import org.cai.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FileStorageProperties fileStorageProperties;

	@GetMapping("/downloadFile/{taskid:.*}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String taskid, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(taskid);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

	@GetMapping("/viewoutput/{taskid:.*}")	
	public void getTaskoutput(@PathVariable String taskid, HttpServletResponse response) throws IOException {
		String directory = Paths.get(fileStorageProperties.getTaskdir()).resolve(taskid).toString();
		InputStream inputStream = new FileInputStream(new File(directory, "out.txt"));
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
	}
}
