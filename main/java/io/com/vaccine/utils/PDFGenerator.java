package io.com.vaccine.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import io.com.vaccine.model.Vaccine;

@Component
public class PDFGenerator {

	public void generateItetenary(Vaccine vaccine,String filePath) {
		
		try {
			Document document=new Document();
			document.open();
			
			document.add(generateTable(vaccine));
			
			
			document.close();
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	private PdfPTable generateTable(Vaccine vaccine) {
		PdfPTable table=new PdfPTable(2);
		PdfPCell cell;
		cell=new PdfPCell(new Phrase("Vaccine : "));
		cell.setColspan(2);
		table.addCell(cell);
		
		cell=new PdfPCell(new Phrase("Vaccine Information"));
		cell.setColspan(2);
		table.addCell(cell);
		
		table.addCell("Vaccine Id");
		table.addCell(vaccine.getVacId());
		
		table.addCell("Vaccine Name");
		table.addCell(vaccine.getName());
		
		table.addCell("Vaccine Type");
		table.addCell(vaccine.getType());
		
		table.addCell("Description");
		table.addCell(vaccine.getDescription());
		
		table.addCell("Vaccine By");
		table.addCell(vaccine.getDoctor());
		
		table.addCell("Aviliable");
		table.addCell(vaccine.getAvialiablity().toString());
		
		
		
		
		
		return table;
	}
}
