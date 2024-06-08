package com.msgemployee.EmployeeSkillSearchProject.util;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;

@Component
public class DocGeneratorUtil {

	public Font fontSetting(String fontStyle, float fontSize, int fontColorR, int fontColorG, int fontColorB) {
		Font font = FontFactory.getFont(fontStyle);
		font.setSize(fontSize);
		font.setColor(fontColorR, fontColorG, fontColorB);
		return font;
	}

	public Font headerSetting(String headerName, Document document) throws DocumentException {
		Font subTitleFont = fontSetting(FontFactory.HELVETICA_BOLD, 12, 144, 12, 63);
		
		Paragraph employeeTitle = new Paragraph(headerName, subTitleFont);
		employeeTitle.setAlignment(Paragraph.ALIGN_LEFT);
		
		document.add(employeeTitle);
		
		Font empParagraphFont = fontSetting(FontFactory.HELVETICA, 10, 60, 60, 60);
		return empParagraphFont;
	}

	public XWPFRun paragraphSetting(XWPFDocument wordDocument, ParagraphAlignment align, int paraFontSize, String color, String text) {
		XWPFParagraph paragraph = wordDocument.createParagraph();
		paragraph.setAlignment(align);
		XWPFRun runPara = paragraph.createRun();
		runPara.setFontSize(paraFontSize);
		runPara.setBold(true);
		runPara.setColor(color);
		runPara.setText(text);
		return runPara;
	}
	
	public Font FontSetting(float fontSize) {
		Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
		fontParagraph.setSize(fontSize);
		return fontParagraph;
	}

}
