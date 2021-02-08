package io.com.vaccine.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.com.vaccine.configuration.AppConfiguration;

@Service
public class FileService {

	@Autowired
	AppConfiguration appConfiguration;

	Tika tika=new Tika();
	
	public String saveProfileImage(String base64Image) throws IOException {
		String imageName=UUID.randomUUID().toString().replaceAll("-", "");
		
		byte[] decodedBytes=Base64.getDecoder().decode(base64Image);
		File target=new File(appConfiguration.getFullProfileImagePath()+"/"+imageName);
		FileUtils.writeByteArrayToFile(target, decodedBytes);
		
		return imageName;
	}
	
	public String detectType(byte[] filterArr) {
		return tika.detect(filterArr);
	}

	public void deleteProfileImage(String image) {
		
		try {
			Files.deleteIfExists(Paths.get(appConfiguration.getFullProfileImagePath()+"/"+image));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
