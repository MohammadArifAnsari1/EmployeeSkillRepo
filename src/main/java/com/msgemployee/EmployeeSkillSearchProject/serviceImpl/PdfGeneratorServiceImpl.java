package com.msgemployee.EmployeeSkillSearchProject.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.msgemployee.EmployeeSkillSearchProject.entity.Education;
import com.msgemployee.EmployeeSkillSearchProject.entity.EmployeeProject;
import com.msgemployee.EmployeeSkillSearchProject.entity.TechnicalStack;
import com.msgemployee.EmployeeSkillSearchProject.repository.CertificateRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.CoreCompetenciesRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.EducationRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.EmployeeRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.ProjectRepository;
import com.msgemployee.EmployeeSkillSearchProject.repository.TechnicalStackRepository;
import com.msgemployee.EmployeeSkillSearchProject.service.PdfGeneratorService;
import com.msgemployee.EmployeeSkillSearchProject.util.DocGeneratorUtil;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfGeneratorServiceImpl implements PdfGeneratorService{
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
	private DocGeneratorUtil pdfUtil;

	@Override
	public void export(HttpServletResponse httpResponse, String employeeId) throws DocumentException, IOException {
		List<Object[]> employeeByEmpId = employeeRepo.findEmployeeByEmpId(employeeId);
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, httpResponse.getOutputStream());
		document.open();
		
		Font headerFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 14, 144, 12, 63);
		
		for (Object[] result : employeeByEmpId) {
			Paragraph idParagraph = new Paragraph("Employee Id : "+ (String) result[0], headerFont);
			idParagraph.setAlignment(Paragraph.ALIGN_CENTER);
			
			Paragraph nameParagraph = new Paragraph("Name : "+(String) result[1]+ " "+(String) result[2]+"\n", headerFont);
			nameParagraph.setAlignment(Paragraph.ALIGN_CENTER);
			
			document.add(idParagraph);
			document.add(nameParagraph);
			
			List<TechnicalStack> technicalStackByEmpId = techStackRepo.findTechnicalStackByEmpId(employeeId);

			addEmployeeInformation(document, employeeId);
			addEmployeeProjectExp(document, employeeId);
			addEmpSkills(document, employeeId, technicalStackByEmpId);
			addCoreCompetencies(document, employeeId);
			addEmpCertificate(document, employeeId);
			addEmpEducation(document, employeeId);
			addEmpProgSkill(document, employeeId, technicalStackByEmpId);
			addEmpTechnology(document, employeeId, technicalStackByEmpId);
			addEmpTools(document, employeeId, technicalStackByEmpId);
			addEmpLanguage(document, employeeId, technicalStackByEmpId);
			
			document.close();
		}
	}

	private void addEmployeeInformation(Document document, String employeeId) throws DocumentException {
		Font empParagraphFont = pdfUtil.headerSetting("Employee Data: ",document);
		Paragraph employeeDetails = fetchEmpDetails(empParagraphFont, employeeId);
		
		document.add(employeeDetails);
	}

	private Paragraph fetchEmpDetails(Font empParagraphFont, String employeeId) {
		List<Object[]> employeeByEmpId = employeeRepo.findEmployeeByEmpId(employeeId);
		StringBuilder empSb = new StringBuilder();
		if(!ObjectUtils.isEmpty(employeeByEmpId)) {
			for (Object[] employee : employeeByEmpId) {
				empSb.append("     First Name: ").append((String) employee[1]).append("\n");
				empSb.append("     Surname: ").append((String) employee[2]).append("\n");
				empSb.append("     Job Title: ").append((String) employee[3]).append("\n");
				empSb.append("     International Experience: ").append((String) employee[4]).append("\n");
				empSb.append("     Community Of Practice: ").append((String) employee[5]).append("\n\n");
			}
		}else {
			empSb.append("Employee with ID ").append(employeeId).append(" not found");
		}
		Paragraph employeeData = new Paragraph(empSb.toString(), empParagraphFont);
//		employeeData.setSpacingBefore(4f);
		
		return employeeData;
	}

	private void addEmployeeProjectExp(Document document, String employeeId) throws DocumentException {
		Font empProjectParagraphFont = pdfUtil.headerSetting("Project Experience: ",document);
		PdfPTable projectExpTable = fetchProjectExperienceDetails(employeeId, empProjectParagraphFont);

		document.add(projectExpTable);
	}

	private PdfPTable fetchProjectExperienceDetails(String employeeId, Font empProjectParagraphFont) throws DocumentException {
		PdfPTable projectExpTable = new PdfPTable(8);
		float[] columnWidths = {1f, 3f, 3f, 3f, 3f, 4f, 3f, 4f };
		projectExpTable.setWidths(columnWidths);
		projectExpTable.setSpacingBefore(5f);
		projectExpTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		projectExpTable.setWidthPercentage(95);
		
		Font empProjectTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", empProjectTableHeaderFont));
		PdfPCell companyNameHeaderCell = new PdfPCell(new Paragraph("Company Name", empProjectTableHeaderFont));
		PdfPCell startProjectHeaderCell = new PdfPCell(new Paragraph("Start Of Project", empProjectTableHeaderFont));
		PdfPCell endProjectHeaderCell = new PdfPCell(new Paragraph("End Of Project", empProjectTableHeaderFont));
		PdfPCell customerHeaderCell = new PdfPCell(new Paragraph("Customer", empProjectTableHeaderFont));
		PdfPCell roleHeaderCell = new PdfPCell(new Paragraph("Role", empProjectTableHeaderFont));
		PdfPCell locationHeaderCell = new PdfPCell(new Paragraph("Location", empProjectTableHeaderFont));
		PdfPCell discriptionHeaderCell = new PdfPCell(new Paragraph("Discription", empProjectTableHeaderFont));
		
		Stream.of(slNoHeaderCell, companyNameHeaderCell, startProjectHeaderCell, endProjectHeaderCell, customerHeaderCell, 
				roleHeaderCell, locationHeaderCell, discriptionHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			projectExpTable.addCell(cell);
		});
		
		List<EmployeeProject> projectByEmpId = projectRepo.findProjectByEmpId(employeeId);
		if(!ObjectUtils.isEmpty(projectByEmpId)) {
			int count = 0;
			for (EmployeeProject employeeProject : projectByEmpId) {
				PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), empProjectParagraphFont));
				PdfPCell companyNameDataCell = new PdfPCell(new Paragraph(employeeProject.getProjectName(), empProjectParagraphFont));
				PdfPCell startDateDataCell = new PdfPCell(new Paragraph(employeeProject.getStartDate(), empProjectParagraphFont));
				PdfPCell endDateDataCell = new PdfPCell(new Paragraph(employeeProject.getEndDate(), empProjectParagraphFont));
				PdfPCell customerDataCell = new PdfPCell(new Paragraph(employeeProject.getCustomer(), empProjectParagraphFont));
				PdfPCell roleDataCell = new PdfPCell(new Paragraph(employeeProject.getProjectRole(), empProjectParagraphFont));
				PdfPCell locationDataCell = new PdfPCell(new Paragraph(employeeProject.getLocation(), empProjectParagraphFont));
				String empDiscription = employeeProject.getProjectDiscription().length()>40?employeeProject.getProjectDiscription().substring(0, 40).concat("..."):employeeProject.getProjectDiscription();
				PdfPCell discriptionDataCell = new PdfPCell(new Paragraph(empDiscription, empProjectParagraphFont));
				
				Stream.of(slNoDataCell, companyNameDataCell, startDateDataCell, endDateDataCell, customerDataCell, 
						roleDataCell, locationDataCell, discriptionDataCell).forEach(cell -> {
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					projectExpTable.addCell(cell);
				});
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Project Experience found for Employee "+employeeId, empProjectParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			projectExpTable.addCell(noDataCell);
		}
		return projectExpTable;
	}

	private void addEmpSkills(Document document, String employeeId, List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		Font skillParagraphFont = pdfUtil.headerSetting("Skills: ", document);
		PdfPTable skillTable = fetchEmpSkills(employeeId, skillParagraphFont, technicalStackByEmpId);

		document.add(skillTable);
	}

	private PdfPTable fetchEmpSkills(String employeeId, Font skillParagraphFont,
			List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		PdfPTable empSkillsTable = new PdfPTable(2);
		float[] columnWidths = {1f, 10f };
		empSkillsTable.setWidths(columnWidths);
		empSkillsTable.setSpacingBefore(5f);
		empSkillsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		empSkillsTable.setWidthPercentage(95);
		
		Font toolsTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", toolsTableHeaderFont));
		PdfPCell skillHeaderCell = new PdfPCell(new Paragraph("Skills", toolsTableHeaderFont));
		
		Stream.of(slNoHeaderCell, skillHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			empSkillsTable.addCell(cell);
		});
		
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack result : technicalStackByEmpId) {
				//Taking Employee Skill info from Programming Skill(TechnicalStack table) column
				if (!ObjectUtils.isEmpty(result.getProgrammingSkill())) {
					PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), skillParagraphFont));
					PdfPCell skillDataCell = new PdfPCell(
							new Paragraph(result.getProgrammingSkill(), skillParagraphFont));

					Stream.of(slNoDataCell, skillDataCell).forEach(cell -> {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						empSkillsTable.addCell(cell);
					});
				}
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Skills found for Employee "+employeeId, skillParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			empSkillsTable.addCell(noDataCell);
		}
		
		return empSkillsTable;
	}

	private void addCoreCompetencies(Document document, String employeeId) throws DocumentException {
		Font coreCompParagraphFont = pdfUtil.headerSetting("Core Competencies: ", document);
		PdfPTable coreCompTable = fetchCoreCompetencies(employeeId, coreCompParagraphFont);

		document.add(coreCompTable);
	}

	private PdfPTable fetchCoreCompetencies(String employeeId, Font coreCompParagraphFont) throws DocumentException {
		PdfPTable coreCompTable = new PdfPTable(3);
		float[] columnWidths = {1f, 5f, 5f };
		coreCompTable.setWidths(columnWidths);
		coreCompTable.setSpacingBefore(5f);
		coreCompTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		coreCompTable.setWidthPercentage(95);
		
		Font coreCompTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", coreCompTableHeaderFont));
		PdfPCell functionalSkillHeaderCell = new PdfPCell(new Paragraph("Functional Skill", coreCompTableHeaderFont));
		PdfPCell technicalSkillHeaderCell = new PdfPCell(new Paragraph("Technical Skill", coreCompTableHeaderFont));
		
		Stream.of(slNoHeaderCell, functionalSkillHeaderCell, technicalSkillHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			coreCompTable.addCell(cell);
		});
		
		List<Object[]> coreCompetenciesByEmpId = coreCompRepo.findCoreCompetenciesByEmpId(employeeId);
		
		if(!ObjectUtils.isEmpty(coreCompetenciesByEmpId)) {
			int count = 0;
			for (Object[] result : coreCompetenciesByEmpId) {
				PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), coreCompParagraphFont));
				PdfPCell functSkillDataCell = new PdfPCell(new Paragraph((String) result[0], coreCompParagraphFont));
				PdfPCell techSkillDataCell = new PdfPCell(new Paragraph((String) result[1], coreCompParagraphFont));
				
				Stream.of(slNoDataCell, functSkillDataCell, techSkillDataCell).forEach(cell -> {
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					coreCompTable.addCell(cell);
				});
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Core Competencies found for Employee "+employeeId, coreCompParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			coreCompTable.addCell(noDataCell);
		}
		
		return coreCompTable;
	}

	private void addEmpCertificate(Document document, String employeeId) throws DocumentException {
		Font certificateParagraphFont = pdfUtil.headerSetting("Certificates: ", document);
		PdfPTable certificateTable = fetchEmpCertificates(employeeId, certificateParagraphFont);

		document.add(certificateTable);
	}

	private PdfPTable fetchEmpCertificates(String employeeId, Font certificateParagraphFont) throws DocumentException {
		PdfPTable certificateTable = new PdfPTable(2);
		float[] columnWidths = {1f, 10f };
		certificateTable.setWidths(columnWidths);
		certificateTable.setSpacingBefore(5f);
		certificateTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		certificateTable.setWidthPercentage(95);
		
		Font certificateTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", certificateTableHeaderFont));
		PdfPCell certificateNameHeaderCell = new PdfPCell(new Paragraph("Certificate Name", certificateTableHeaderFont));
		
		Stream.of(slNoHeaderCell, certificateNameHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			certificateTable.addCell(cell);
		});
		
		List<Object[]> certificateByEmpId = certificateRepo.findCertificateByEmpId(employeeId);
		
		if(!ObjectUtils.isEmpty(certificateByEmpId)) {
			int count = 0;
			for (Object[] result : certificateByEmpId) {
				PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), certificateParagraphFont));
				PdfPCell certificateNameDataCell = new PdfPCell(new Paragraph((String) result[0], certificateParagraphFont));
				
				Stream.of(slNoDataCell, certificateNameDataCell).forEach(cell -> {
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					certificateTable.addCell(cell);
				});
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Certificates found for Employee "+employeeId, certificateParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			certificateTable.addCell(noDataCell);
		}
		
		return certificateTable;
	}

	private void addEmpEducation(Document document, String employeeId) throws DocumentException {
		Font educationParagraphFont = pdfUtil.headerSetting("Education: ", document);
		PdfPTable educationTable = fetchEmpEducation(employeeId, educationParagraphFont);

		document.add(educationTable);
	}

	private PdfPTable fetchEmpEducation(String employeeId, Font educationParagraphFont) throws DocumentException {
		PdfPTable empEducationTable = new PdfPTable(6);
		float[] columnWidths = {1f, 3f, 3f, 3f, 3f, 3f };
		empEducationTable.setWidths(columnWidths);
		empEducationTable.setSpacingBefore(5f);
		empEducationTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		empEducationTable.setWidthPercentage(95);
		
		Font educationTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", educationTableHeaderFont));
		PdfPCell fromDateHeaderCell = new PdfPCell(new Paragraph("From", educationTableHeaderFont));
		PdfPCell toDateHeaderCell = new PdfPCell(new Paragraph("To", educationTableHeaderFont));
		PdfPCell degreeHeaderCell = new PdfPCell(new Paragraph("Degree", educationTableHeaderFont));
		PdfPCell fieldHeaderCell = new PdfPCell(new Paragraph("Field", educationTableHeaderFont));
		PdfPCell universityHeaderCell = new PdfPCell(new Paragraph("University", educationTableHeaderFont));
		
		Stream.of(slNoHeaderCell, fromDateHeaderCell, toDateHeaderCell, degreeHeaderCell, fieldHeaderCell, universityHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			empEducationTable.addCell(cell);
		});
		
		List<Education> empEducationByEmpId = educationRepo.findEducationByEmpId(employeeId);
		
		if(!ObjectUtils.isEmpty(empEducationByEmpId)) {
			int count = 0;
			for (Education result : empEducationByEmpId) {
				PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), educationParagraphFont));
				PdfPCell fromDateDataCell = new PdfPCell(new Paragraph(result.getEducationFrom(), educationParagraphFont));
				PdfPCell toDateDataCell = new PdfPCell(new Paragraph(result.getEducationTo(), educationParagraphFont));
				PdfPCell degreeDataCell = new PdfPCell(new Paragraph(result.getDegree(), educationParagraphFont));
				PdfPCell fieldDataCell = new PdfPCell(new Paragraph(result.getField(), educationParagraphFont));
				PdfPCell universityDataCell = new PdfPCell(new Paragraph(result.getUniversity(), educationParagraphFont));
				
				Stream.of(slNoDataCell, fromDateDataCell, toDateDataCell, degreeDataCell, fieldDataCell, universityDataCell).forEach(cell -> {
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					empEducationTable.addCell(cell);
				});
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Education found for Employee "+employeeId, educationParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			empEducationTable.addCell(noDataCell);
		}
		
		return empEducationTable;
	}

	private void addEmpProgSkill(Document document, String employeeId, List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		Font progSkillParagraphFont = pdfUtil.headerSetting("Programming Skills: ", document);
		PdfPTable progSkillTable = fetchEmpProgSkill(employeeId, progSkillParagraphFont, technicalStackByEmpId);

		document.add(progSkillTable);
	}

	private PdfPTable fetchEmpProgSkill(String employeeId, Font progSkillParagraphFont,
			List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		PdfPTable empProgSkillTable = new PdfPTable(3);
		float[] columnWidths = {1f, 5f, 5f };
		empProgSkillTable.setWidths(columnWidths);
		empProgSkillTable.setSpacingBefore(5f);
		empProgSkillTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		empProgSkillTable.setWidthPercentage(95);
		
		Font educationTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", educationTableHeaderFont));
		PdfPCell progSkillHeaderCell = new PdfPCell(new Paragraph("Programming Skill", educationTableHeaderFont));
		PdfPCell proficiencyLevelHeaderCell = new PdfPCell(new Paragraph("Proficiency Level", educationTableHeaderFont));
		
		Stream.of(slNoHeaderCell, progSkillHeaderCell, proficiencyLevelHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			empProgSkillTable.addCell(cell);
		});
		
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack result : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(result.getProgrammingSkill())
						&& !ObjectUtils.isEmpty(result.getProgSkillProficiencyLevel())) {
					PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), progSkillParagraphFont));
					PdfPCell progSkillDataCell = new PdfPCell(
							new Paragraph(result.getProgrammingSkill(), progSkillParagraphFont));
					PdfPCell proficiencyLevelDataCell = new PdfPCell(
							new Paragraph(result.getProgSkillProficiencyLevel(), progSkillParagraphFont));

					Stream.of(slNoDataCell, progSkillDataCell, proficiencyLevelDataCell).forEach(cell -> {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						empProgSkillTable.addCell(cell);
					});
				}
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Programming Skill found for Employee "+employeeId, progSkillParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			empProgSkillTable.addCell(noDataCell);
		}
		
		return empProgSkillTable;
	}

	private void addEmpTechnology(Document document, String employeeId, List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		Font technologyParagraphFont = pdfUtil.headerSetting("Technologies: ", document);
		PdfPTable technologyTable = fetchEmpTechnology(employeeId, technologyParagraphFont, technicalStackByEmpId);

		document.add(technologyTable);
	}

	private PdfPTable fetchEmpTechnology(String employeeId, Font technologyParagraphFont,
			List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		PdfPTable empTechnologyTable = new PdfPTable(3);
		float[] columnWidths = {1f, 5f, 5f };
		empTechnologyTable.setWidths(columnWidths);
		empTechnologyTable.setSpacingBefore(5f);
		empTechnologyTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		empTechnologyTable.setWidthPercentage(95);
		
		Font technologyTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", technologyTableHeaderFont));
		PdfPCell technologyHeaderCell = new PdfPCell(new Paragraph("Technology", technologyTableHeaderFont));
		PdfPCell proficiencyLevelHeaderCell = new PdfPCell(new Paragraph("Proficiency Level", technologyTableHeaderFont));
		
		Stream.of(slNoHeaderCell, technologyHeaderCell, proficiencyLevelHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			empTechnologyTable.addCell(cell);
		});
		
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack result : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(result.getTechnology())
						&& !ObjectUtils.isEmpty(result.getTechProficiencyLevel())) {
					PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), technologyParagraphFont));
					PdfPCell technologyDataCell = new PdfPCell(
							new Paragraph(result.getTechnology(), technologyParagraphFont));
					PdfPCell proficiencyLevelDataCell = new PdfPCell(
							new Paragraph(result.getTechProficiencyLevel(), technologyParagraphFont));

					Stream.of(slNoDataCell, technologyDataCell, proficiencyLevelDataCell).forEach(cell -> {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						empTechnologyTable.addCell(cell);
					});
				}
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Technologies found for Employee "+employeeId, technologyParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			empTechnologyTable.addCell(noDataCell);
		}
		
		return empTechnologyTable;
	}
	
	private void addEmpTools(Document document, String employeeId, List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		Font toolsParagraphFont = pdfUtil.headerSetting("Tools: ", document);
		PdfPTable toolsTable = fetchEmpTools(employeeId, toolsParagraphFont, technicalStackByEmpId);

		document.add(toolsTable);
	}

	private PdfPTable fetchEmpTools(String employeeId, Font toolsParagraphFont,
			List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		PdfPTable empToolsTable = new PdfPTable(3);
		float[] columnWidths = {1f, 5f, 5f };
		empToolsTable.setWidths(columnWidths);
		empToolsTable.setSpacingBefore(5f);
		empToolsTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		empToolsTable.setWidthPercentage(95);
		
		Font toolsTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", toolsTableHeaderFont));
		PdfPCell toolsHeaderCell = new PdfPCell(new Paragraph("Tools", toolsTableHeaderFont));
		PdfPCell proficiencyLevelHeaderCell = new PdfPCell(new Paragraph("Proficiency Level", toolsTableHeaderFont));
		
		Stream.of(slNoHeaderCell, toolsHeaderCell, proficiencyLevelHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			empToolsTable.addCell(cell);
		});
		
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack result : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(result.getTools())
						&& !ObjectUtils.isEmpty(result.getToolProficiencyLevel())) {
					PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), toolsParagraphFont));
					PdfPCell toolsDataCell = new PdfPCell(
							new Paragraph(result.getTools(), toolsParagraphFont));
					PdfPCell proficiencyLevelDataCell = new PdfPCell(
							new Paragraph(result.getToolProficiencyLevel(), toolsParagraphFont));

					Stream.of(slNoDataCell, toolsDataCell, proficiencyLevelDataCell).forEach(cell -> {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						empToolsTable.addCell(cell);
					});
				}
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Tools found for Employee "+employeeId, toolsParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			empToolsTable.addCell(noDataCell);
		}
		
		return empToolsTable;
	}

	private void addEmpLanguage(Document document, String employeeId, List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		Font languageParagraphFont = pdfUtil.headerSetting("Language: ", document);
		PdfPTable languageTable = fetchEmpLanguage(employeeId, languageParagraphFont, technicalStackByEmpId);

		document.add(languageTable);
	}

	private PdfPTable fetchEmpLanguage(String employeeId, Font languageParagraphFont,
			List<TechnicalStack> technicalStackByEmpId) throws DocumentException {
		PdfPTable empEducationTable = new PdfPTable(3);
		float[] columnWidths = {1f, 5f, 5f };
		empEducationTable.setWidths(columnWidths);
		empEducationTable.setSpacingBefore(5f);
		empEducationTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		empEducationTable.setWidthPercentage(95);
		
		Font educationTableHeaderFont = pdfUtil.fontSetting(FontFactory.HELVETICA_BOLD, 10, 60, 60, 60);
		PdfPCell slNoHeaderCell = new PdfPCell(new Paragraph("Sl No", educationTableHeaderFont));
		PdfPCell langNameHeaderCell = new PdfPCell(new Paragraph("Langauge", educationTableHeaderFont));
		PdfPCell proficiencyLevelHeaderCell = new PdfPCell(new Paragraph("Proficiency Level", educationTableHeaderFont));
		
		Stream.of(slNoHeaderCell, langNameHeaderCell, proficiencyLevelHeaderCell).forEach(cell -> {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setGrayFill(0.9f);
			
			empEducationTable.addCell(cell);
		});
		
		if(!ObjectUtils.isEmpty(technicalStackByEmpId)) {
			int count = 0;
			for (TechnicalStack result : technicalStackByEmpId) {
				if (!ObjectUtils.isEmpty(result.getLanguage())
						&& !ObjectUtils.isEmpty(result.getLangProficiencyLevel())) {
					PdfPCell slNoDataCell = new PdfPCell(new Paragraph(String.valueOf(++count), languageParagraphFont));
					PdfPCell langNameDataCell = new PdfPCell(
							new Paragraph(result.getLanguage(), languageParagraphFont));
					PdfPCell proficiencyLevelDataCell = new PdfPCell(
							new Paragraph(result.getLangProficiencyLevel(), languageParagraphFont));

					Stream.of(slNoDataCell, langNameDataCell, proficiencyLevelDataCell).forEach(cell -> {
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						empEducationTable.addCell(cell);
					});
				}
			}
		}else {
			PdfPCell noDataCell = new PdfPCell(new Paragraph("No Education found for Employee "+employeeId, languageParagraphFont));
			noDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
			noDataCell.setBorder(Rectangle.NO_BORDER);
			noDataCell.setColspan(3);
			
			empEducationTable.addCell(noDataCell);
		}
		
		return empEducationTable;
	}

}
