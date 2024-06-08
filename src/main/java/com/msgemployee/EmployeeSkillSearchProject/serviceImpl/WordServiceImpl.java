package com.msgemployee.EmployeeSkillSearchProject.serviceImpl;

import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableRowAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.msgemployee.EmployeeSkillSearchProject.entity.Education;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeProject;
import com.msgemployee.EmployeeSkillSearchProject.entity.TechnicalStack;
import com.msgemployee.EmployeeSkillSearchProject.repository.CertificateRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.CoreCompetenciesRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.EducationRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.EmployeeRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.ProjectRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.TechnicalStackRepository;
import com.msgemployee.EmployeeSkillSearchProject.service.WordGeneratorService;
import com.msgemployee.EmployeeSkillSearchProject.util.DocGeneratorUtil;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class WordServiceImpl implements WordGeneratorService{
	@Autowired
	private EmployeeRepository employeeRepo;
	@Autowired
	private ProjectRepository projectRepo;
	@Autowired
	private CoreCompetenciesRepository coreCompRepo;
	@Autowired
	private CertificateRepository certificateRepo;
	@Autowired
	private EducationRepository educationRepo;
	@Autowired
	private TechnicalStackRepository techStackRepo;
	@Autowired
	private DocGeneratorUtil wordUtil;
	
	
	@Override
	public void export(HttpServletResponse httpResponse, String employeeId) throws DocumentException, IOException {
		
		XWPFDocument wordDocument = new XWPFDocument();
		List<Object[]> employeeByEmpId = employeeRepo.findEmployeeByEmpId(employeeId);
		Object[] result = employeeByEmpId.get(0);
		
		wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.CENTER, 16, "8C0C3F","Employee Id : "+(String) result[0]);
		Font idFontSetting = wordUtil.FontSetting(12);
		wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.CENTER, 16, "8C0C3F","Name : "+(String) result[1]+" "+(String) result[2]);
		Font nameFontSetting = wordUtil.FontSetting(12);
		
		List<TechnicalStack> technicalStackByEmpId = techStackRepo.findTechnicalStackByEmpId(employeeId);
		
		addEmployeeInformation(wordDocument, employeeId, employeeByEmpId);
		addProjectExperience(wordDocument, employeeId);
		addSkills(wordDocument, employeeId, technicalStackByEmpId);
		addCoreCompetencies(wordDocument, employeeId);
		addCertificates(wordDocument, employeeId);
		addEducation(wordDocument, employeeId);
		addProgrammingSkills(wordDocument, employeeId, technicalStackByEmpId);
		addTechnologies(wordDocument, employeeId, technicalStackByEmpId);
		addTools(wordDocument, employeeId, technicalStackByEmpId);
		addLanguages(wordDocument, employeeId, technicalStackByEmpId);
		
		wordDocument.write(httpResponse.getOutputStream());
		wordDocument.close();
	}

	//01. Employee Information
	private void addEmployeeInformation(XWPFDocument wordDocument, String employeeId, List<Object[]> employeeByEmpId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "8C0C3F","Employee Data");
		
		fetchEmpDetails(wordDocument, employeeId, employeeByEmpId);
	}

	private String fetchEmpDetails(XWPFDocument wordDocument, String employeeId, List<Object[]> employeeByEmpId) {
		XWPFParagraph paragraph = wordDocument.createParagraph();
		
		StringBuilder empSb = new StringBuilder();
		if(!ObjectUtils.isEmpty(employeeByEmpId)) {
			for (Object[] employee : employeeByEmpId) {
				empSb.append("  First Name: ").append((String) employee[1]).append("\n");
				empSb.append("  Surname: ").append((String) employee[2]).append("\n");
				empSb.append("  Job Title: ").append((String) employee[3]).append("\n");
				empSb.append("  International Experience: ").append((String) employee[4]).append("\n");
				empSb.append("  Community Of Practice: ").append((String) employee[5]);
			}
		}else {
			empSb.append("Employee with ID ").append(employeeId).append(" not found");
		}
		XWPFRun runEmp = paragraph.createRun();
		runEmp.setFontSize(11);
		String[] empDetailsSplit = empSb.toString().split("\n");
		for (String string : empDetailsSplit) {
			runEmp.setText(string);
			runEmp.addBreak();
		}
		return empSb.toString();
	}

	//02. Project Experience
	private void addProjectExperience(XWPFDocument wordDocument, String employeeId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Project Experience");
		
		fetchProjectExperience(employeeId, wordDocument);
	}

	private XWPFTable fetchProjectExperience(String employeeId, XWPFDocument wordDocument) {
		XWPFTable projectTable = wordDocument.createTable();
		projectTable.setWidth("97%");
		projectTable.setCellMargins(100, 100, 100, 100);
		projectTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Company Name", "Project Start Date", "Project End Date", "Customer", "Project Role", "Location", "Description"};
		
		XWPFTableRow tableTitleRow = projectTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = projectTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		List<EmployeeProject> projectByEmpId = projectRepo.findProjectByEmpId(employeeId);
		if(!ObjectUtils.isEmpty(projectByEmpId)) {
			int count = 0;
			for (EmployeeProject employeeProject : projectByEmpId) {
				XWPFTableRow dataRow = projectTable.createRow();
				List<XWPFTableCell> tableCells = dataRow.getTableCells();
				tableCells.get(0).setText(String.valueOf(++count));
				tableCells.get(1).setText(employeeProject.getProjectName());
				tableCells.get(2).setText(employeeProject.getStartDate());
				tableCells.get(3).setText(employeeProject.getEndDate());
				tableCells.get(4).setText(employeeProject.getCustomer());
				tableCells.get(5).setText(employeeProject.getProjectRole());
				tableCells.get(6).setText(employeeProject.getLocation());
				
				String empDiscription = employeeProject.getProjectDiscription().length()>40?employeeProject.getProjectDiscription().substring(0, 40).concat("..."):employeeProject.getProjectDiscription();
				
				tableCells.get(7).setText(empDiscription);
			}
		}else {
			XWPFTableRow noDataRow = projectTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Project experience found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return projectTable;
	}

	//03. Skills
	private void addSkills(XWPFDocument wordDocument, String employeeId,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Skills");
		
		fetchSkills(employeeId, wordDocument, technicalStackByEmpId);
	}

	private XWPFTable fetchSkills(String employeeId, XWPFDocument wordDocument,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFTable skillTable = wordDocument.createTable();
		skillTable.setWidth("97%");
		skillTable.setCellMargins(100, 100, 100, 100);
		skillTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Skills"};
		
		XWPFTableRow tableTitleRow = skillTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = skillTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack tools : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(tools.getProgrammingSkill())) {
					XWPFTableRow dataRow = skillTable.createRow();
					List<XWPFTableCell> tableCells = dataRow.getTableCells();
					tableCells.get(0).setText(String.valueOf(++count));
					tableCells.get(1).setText(tools.getProgrammingSkill());
				}
			}
		}else {
			XWPFTableRow noDataRow = skillTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Skills found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return skillTable;
	}
	
	//04. Core Competencies
	private void addCoreCompetencies(XWPFDocument wordDocument, String employeeId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Core Competencies");
		
		fetchCoreCompetencies(employeeId, wordDocument);
	}

	private XWPFTable fetchCoreCompetencies(String employeeId, XWPFDocument wordDocument) {
		XWPFTable coreCompTable = wordDocument.createTable();
		coreCompTable.setWidth("97%");
		coreCompTable.setCellMargins(100, 100, 100, 100);
		coreCompTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Functional Skill", "Technical Skill"};
		
		XWPFTableRow tableTitleRow = coreCompTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = coreCompTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		List<Object[]> coreCompetenciesByEmpId = coreCompRepo.findCoreCompetenciesByEmpId(employeeId);
		if(!ObjectUtils.isEmpty(coreCompetenciesByEmpId)) {
			int count = 0;
			for (Object[] coreComp : coreCompetenciesByEmpId) {
				XWPFTableRow dataRow = coreCompTable.createRow();
				List<XWPFTableCell> tableCells = dataRow.getTableCells();
				tableCells.get(0).setText(String.valueOf(++count));
				tableCells.get(1).setText((String) coreComp[0]);
				tableCells.get(2).setText((String) coreComp[1]);
			}
		}else {
			XWPFTableRow noDataRow = coreCompTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Core Competencies found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return coreCompTable;
	}
	
	//05. Certificates
	private void addCertificates(XWPFDocument wordDocument, String employeeId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Certificates");
		
		fetchCertificates(employeeId, wordDocument);
	}

	private XWPFTable fetchCertificates(String employeeId, XWPFDocument wordDocument) {
		XWPFTable certificateTable = wordDocument.createTable();
		certificateTable.setWidth("97%");
		certificateTable.setCellMargins(100, 100, 100, 100);
		certificateTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Certificate Name"};
		
		XWPFTableRow tableTitleRow = certificateTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = certificateTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		List<Object[]> certificateByEmpId = certificateRepo.findCertificateByEmpId(employeeId);
		if(!ObjectUtils.isEmpty(certificateByEmpId)) {
			int count = 0;
			for (Object[] certificate : certificateByEmpId) {
				XWPFTableRow dataRow = certificateTable.createRow();
				List<XWPFTableCell> tableCells = dataRow.getTableCells();
				tableCells.get(0).setText(String.valueOf(++count));
				tableCells.get(1).setText((String) certificate[0]);
			}
		}else {
			XWPFTableRow noDataRow = certificateTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Certificates found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return certificateTable;
	}
	
	//06. Education
	private void addEducation(XWPFDocument wordDocument, String employeeId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Education");
		
		fetchEducation(employeeId, wordDocument);
	}

	private XWPFTable fetchEducation(String employeeId, XWPFDocument wordDocument) {
		XWPFTable educationTable = wordDocument.createTable();
		educationTable.setWidth("97%");
		educationTable.setCellMargins(100, 100, 100, 100);
		educationTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "From", "To", "Degree", "Field", "University"};
		
		XWPFTableRow tableTitleRow = educationTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = educationTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		List<Education> educationByEmpId = educationRepo.findEducationByEmpId(employeeId);
		if(!ObjectUtils.isEmpty(educationByEmpId)) {
			int count = 0;
			for (Education education : educationByEmpId) {
				XWPFTableRow dataRow = educationTable.createRow();
				List<XWPFTableCell> tableCells = dataRow.getTableCells();
				tableCells.get(0).setText(String.valueOf(++count));
				tableCells.get(1).setText(education.getEducationFrom());
				tableCells.get(2).setText(education.getEducationTo());
				tableCells.get(3).setText(education.getDegree());
				tableCells.get(4).setText(education.getField());
				tableCells.get(5).setText(education.getUniversity());
			}
		}else {
			XWPFTableRow noDataRow = educationTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Education found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return educationTable;
	}

	//07. Programming Skills
	private void addProgrammingSkills(XWPFDocument wordDocument, String employeeId,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Programming Skills");
		
		fetchProgrammingSkills(employeeId, wordDocument, technicalStackByEmpId);
	}

	private XWPFTable fetchProgrammingSkills(String employeeId, XWPFDocument wordDocument,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFTable progSkillTable = wordDocument.createTable();
		progSkillTable.setWidth("97%");
		progSkillTable.setCellMargins(100, 100, 100, 100);
		progSkillTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Programming Skill", "Proficiency Level"};
		
		XWPFTableRow tableTitleRow = progSkillTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = progSkillTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack progSkill : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(progSkill.getProgrammingSkill())) {
					XWPFTableRow dataRow = progSkillTable.createRow();
					List<XWPFTableCell> tableCells = dataRow.getTableCells();
					tableCells.get(0).setText(String.valueOf(++count));
					tableCells.get(1).setText(progSkill.getProgrammingSkill());
					tableCells.get(2).setText(progSkill.getProgSkillProficiencyLevel());
				}
			}
		}else {
			XWPFTableRow noDataRow = progSkillTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Programming Skills found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return progSkillTable;
	}

	//08. Technology
	private void addTechnologies(XWPFDocument wordDocument, String employeeId,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Technologies");
		
		fetchTechnologies(employeeId, wordDocument, technicalStackByEmpId);
	}

	private XWPFTable fetchTechnologies(String employeeId, XWPFDocument wordDocument,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFTable technologyTable = wordDocument.createTable();
		technologyTable.setWidth("97%");
		technologyTable.setCellMargins(100, 100, 100, 100);
		technologyTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Technology", "Proficiency Level"};
		
		XWPFTableRow tableTitleRow = technologyTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = technologyTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack technology : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(technology.getTechnology())) {
					XWPFTableRow dataRow = technologyTable.createRow();
					List<XWPFTableCell> tableCells = dataRow.getTableCells();
					tableCells.get(0).setText(String.valueOf(++count));
					tableCells.get(1).setText(technology.getTechnology());
					tableCells.get(2).setText(technology.getTechProficiencyLevel());
				}
			}
		}else {
			XWPFTableRow noDataRow = technologyTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Technologies found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return technologyTable;
	}

	//09. Tools
	private void addTools(XWPFDocument wordDocument, String employeeId,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Tools");
		
		fetchTools(employeeId, wordDocument, technicalStackByEmpId);
	}

	private XWPFTable fetchTools(String employeeId, XWPFDocument wordDocument,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFTable toolTable = wordDocument.createTable();
		toolTable.setWidth("97%");
		toolTable.setCellMargins(100, 100, 100, 100);
		toolTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Tools", "Proficiency Level"};
		
		XWPFTableRow tableTitleRow = toolTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = toolTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack tools : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(tools.getTools())) {
					XWPFTableRow dataRow = toolTable.createRow();
					List<XWPFTableCell> tableCells = dataRow.getTableCells();
					tableCells.get(0).setText(String.valueOf(++count));
					tableCells.get(1).setText(tools.getTools());
					tableCells.get(2).setText(tools.getToolProficiencyLevel());
				}
			}
		}else {
			XWPFTableRow noDataRow = toolTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Tools found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return toolTable;
	}

	//10. Languages
	private void addLanguages(XWPFDocument wordDocument, String employeeId,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFRun paragraphSetting = wordUtil.paragraphSetting(wordDocument, ParagraphAlignment.LEFT, 13, "90303F","Languages");
		
		fetchLanguages(employeeId, wordDocument, technicalStackByEmpId);
	}

	private XWPFTable fetchLanguages(String employeeId, XWPFDocument wordDocument,
			List<TechnicalStack> technicalStackByEmpId) {
		XWPFTable langTable = wordDocument.createTable();
		langTable.setWidth("97%");
		langTable.setCellMargins(100, 100, 100, 100);
		langTable.setTableAlignment(TableRowAlign.CENTER);
		
		String[] headers = {"Sl No", "Language", "Proficiency Level"};
		
		XWPFTableRow tableTitleRow = langTable.getRow(0);
		if(tableTitleRow == null) {
			tableTitleRow = langTable.createRow();
		}
		int headerSize = headers.length;
		for(int col = 0; col < headerSize; col++) {
			XWPFTableCell headerCell = tableTitleRow.getCell(col);
			if(headerCell == null) {
				headerCell = tableTitleRow.createCell();
			}
			headerCell.setText(headers[col]);
			headerCell.setColor("D3D3D3");
		}
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack language : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(language.getLanguage())) {
					XWPFTableRow dataRow = langTable.createRow();
					List<XWPFTableCell> tableCells = dataRow.getTableCells();
					tableCells.get(0).setText(String.valueOf(++count));
					tableCells.get(1).setText(language.getLanguage());
					tableCells.get(2).setText(language.getLangProficiencyLevel());
				}
			}
		}else {
			XWPFTableRow noDataRow = langTable.createRow();
			XWPFTableCell noDataCell = noDataRow.getCell(0);
			if(noDataCell == null)
				noDataCell = noDataRow.createCell();
			noDataCell.setText("No Languages found for Employee "+employeeId);
			noDataCell.setVerticalAlignment(XWPFVertAlign.CENTER);
		}
		
		return langTable;
	}

}
