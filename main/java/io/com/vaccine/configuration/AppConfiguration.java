package io.com.vaccine.configuration;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class AppConfiguration {

	String uploadPath="images";
	
	String profileImageFolder="profile";
	
	String attachments="attachments";
	
	public String getFullProfileImagePath() {
		return this.uploadPath+"/"+this.profileImageFolder;
	}
	
	public String getFullAttachmentPath() {
		return this.uploadPath+"/"+this.attachments;
	}
}
