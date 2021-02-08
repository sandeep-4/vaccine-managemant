package io.com.vaccine.utils;

public interface EmailUtil {

	public void sendEmail(String toAddress,String subject,String body);
	public void sendEmailPdf(String toAddress,String filePath);
}
