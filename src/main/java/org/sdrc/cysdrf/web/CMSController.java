package org.sdrc.cysdrf.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.core.Authorize;
import org.sdrc.cysdrf.model.ReportDetailsModel;
import org.sdrc.cysdrf.model.ReportTypeDetailsModel;
import org.sdrc.cysdrf.model.SectionDetailsModel;
import org.sdrc.cysdrf.model.TagDetailsModel;
import org.sdrc.cysdrf.service.CMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CMSController {

	@Autowired
	CMSService cmsService;

	@Autowired
	private ResourceBundleMessageSource cmsMessage;

	@RequestMapping("/getAllSection")
	@ResponseBody
	public List<SectionDetailsModel> getAllSection() {

		return cmsService.getAllSections();
	}

	@Authorize(feature = "view-reports", permission = "View")
	@RequestMapping("/getAllReports")
	@ResponseBody
	public List<ReportDetailsModel> getAllDocuments() {
		return cmsService.getAllReports();
	}

	@Authorize(feature = "manage-section", permission = "Edit")
	@RequestMapping(value = "/editSection", method = RequestMethod.POST)
	public String editSection(RedirectAttributes redirectAttributes,
			@RequestParam("sectionName") String sectionName,
			@RequestParam("sectionId") Integer sectionId,
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam("description") String description,
			@RequestParam("parentId") Integer parentId) {
		List<String> tagsName;
		if (tags == null || tags.trim().equalsIgnoreCase("")) {
			tagsName = new ArrayList<>();
		} else {
			tagsName = new ArrayList<String>(Arrays.asList(tags.split(",")));
		}

		boolean check;

		check = cmsService.editSection(sectionName.trim(), sectionId, tagsName,
				description.trim(), parentId);

		List<String> addMessage = new ArrayList<>();
		redirectAttributes.addFlashAttribute("formError", addMessage);

		if (check) {
			redirectAttributes.addFlashAttribute("className", cmsMessage
					.getMessage("bootstrap.alert.success", null, null));
			if (parentId != -1)
				addMessage.add(cmsMessage.getMessage("theme.update.success",
						null, null));
			else

				addMessage.add(cmsMessage.getMessage("Section.update.success",
						null, null));
		} else {
			redirectAttributes.addFlashAttribute("className", cmsMessage
					.getMessage("bootstrap.alert.warning", null, null));
			if (parentId != -1)

				addMessage.add(cmsMessage.getMessage("theme.update.failure",
						null, null));
			else

				addMessage.add(cmsMessage.getMessage("Section.update.failure",
						null, null));
		}
		return "redirect:cms/manage-section";
	}

	@Authorize(feature = "manage-section", permission = "Edit")
	@RequestMapping(value = "/newSection", method = RequestMethod.POST)
	public String newSection(RedirectAttributes redirectAttributes,
			@RequestParam("sectionName") String sectionName,
			@RequestParam("parentId") Integer parentId,
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam("description") String description)

	{

		boolean check;

		List<String> tagsName;
		if (tags == null || tags.trim().equalsIgnoreCase("")) {
			tagsName = new ArrayList<>();
		} else {
			tagsName = new ArrayList<String>(Arrays.asList(tags.split(",")));
		}

		check = cmsService.newSection(sectionName.trim(), parentId, tagsName,
				description.trim());
		List<String> addMessage = new ArrayList<>();
		redirectAttributes.addFlashAttribute("formError", addMessage);

		if (check) {
			redirectAttributes.addFlashAttribute("className", cmsMessage
					.getMessage("bootstrap.alert.success", null, null));
			if (parentId != -1)
				addMessage.add(cmsMessage.getMessage("theme.add.success", null,
						null));
			else

				addMessage.add(cmsMessage.getMessage("Section.add.success",
						null, null));
		} else {
			redirectAttributes.addFlashAttribute("className", cmsMessage
					.getMessage("bootstrap.alert.warning", null, null));
			if (parentId != -1)

				addMessage.add(cmsMessage.getMessage("theme.add.failure", null,
						null));
			else

				addMessage.add(cmsMessage.getMessage("Section.add.failure",
						null, null));
		}

		return "redirect:cms/manage-section";
	}

	@Authorize(feature = "view-reports", permission = "Edit")
	@RequestMapping(value = "/editReport", method = RequestMethod.POST)
	public String editReport(RedirectAttributes redirectAttributes,
			@RequestParam(value = "reportId") Integer reportId,
			@RequestParam(value = "reportName") String reportName,
			@RequestParam(value = "description") String description,
			@RequestParam(value = "tags", required = false) String tags) {
		cmsService.updateRecord(reportId, reportName.trim(), description.trim(), tags);
		List<String> addMessage = new ArrayList<>();
		addMessage.add(cmsMessage.getMessage("Report.update.success", null,
				null));
		redirectAttributes.addFlashAttribute("formError", addMessage);
		redirectAttributes.addFlashAttribute("className",
				cmsMessage.getMessage("bootstrap.alert.success", null, null));
		return "redirect:cms/view-reports";
	}

	@Authorize(feature = "upload-report", permission = "Edit")
	@RequestMapping(value = "/saveReport", method = RequestMethod.POST)
	public String saveReport(
			HttpServletRequest request,
			RedirectAttributes redirectAttributes,
			@RequestParam("reportTitle") String reportTitle,
			@RequestParam("desc") String desc,
			@RequestParam("parentId") Integer parentId,
			@RequestParam("reportType") String reportType,
			@RequestParam(value = "url", required = false) String url,
			@RequestParam("sectionId") Integer sectionId,
			@RequestParam(value = "tags", required = false) String tags,
			@RequestParam(value = "file", required = false) MultipartFile[] files,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		cmsService.saveReport(reportTitle.trim(), desc.trim(), parentId, reportType, url,
				sectionId, tags, files, file);

		List<String> addMessage = new ArrayList<>();
		addMessage.add(cmsMessage.getMessage("Report.add.success", null, null));
		redirectAttributes.addFlashAttribute("formError", addMessage);
		redirectAttributes.addFlashAttribute("className",
				cmsMessage.getMessage("bootstrap.alert.success", null, null));
		return "redirect:cms/upload-report";
	}

	@RequestMapping("/getAllTags")
	@ResponseBody
	public List<TagDetailsModel> getAllTags() {
		return cmsService.getAllTags();
	}

	@RequestMapping("/getAllReportType")
	@ResponseBody
	public List<ReportTypeDetailsModel> getAllReportType() {
		return cmsService.getAllReportType();
	}

	@Authorize(feature = "view-reports", permission = "View")
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void sendFile(@RequestParam("reportId") int reportId,
			HttpServletResponse response) throws IOException {
		File file = null;

		try {
			String filePath = cmsService.getReportDocumentById(reportId);
			file = new File(filePath);
			InputStream inputStream;
			inputStream = new FileInputStream(file);
			response.setContentType("application/octet-stream");

			response.setHeader("Content-Disposition",
					"fileName=" + file.getName());
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} catch (FileNotFoundException e) {
			response.sendError(404, "File Deleted Or Modified");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			response.sendError(404, "File Deleted Or Modified");
			e.printStackTrace();
		} finally {
			try {
				response.getOutputStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Authorize(feature = "view-reports", permission = "Edit")
	@RequestMapping(value = "/deleteRecord", method = RequestMethod.GET)
	public String deleteReport(RedirectAttributes redirectAttributes,
			@RequestParam("reportId") Integer reportId) {

		boolean check = cmsService.deleteReport(reportId);

		List<String> addMessage = new ArrayList<>();
		if (check) {
			addMessage.add(cmsMessage.getMessage("Report.delete.success", null,
					null));
			redirectAttributes.addFlashAttribute("formError", addMessage);
			redirectAttributes.addFlashAttribute("className", cmsMessage
					.getMessage("bootstrap.alert.success", null, null));
		}

		else {
			addMessage.add(cmsMessage.getMessage("Report.delete.failure", null,
					null));
			redirectAttributes.addFlashAttribute("formError", addMessage);
			redirectAttributes.addFlashAttribute("className", cmsMessage
					.getMessage("bootstrap.alert.warning", null, null));

		}
		return "redirect:cms/view-reports";
	}

	@Authorize(feature = "cms", permission = "View")
	@RequestMapping("/cms")
	public String cmsPage() {
		return "cms";
	}

	@Authorize(feature = "upload-report", permission = "View")
	@RequestMapping("cms/upload-report")
	public String uploadPage() {
		return "cms/upload-report";
	}

	@Authorize(feature = "manage-section", permission = "View")
	@RequestMapping("cms/manage-section")
	public String sectionPage() {
		return "cms/manage-section";
	}

	@Authorize(feature = "view-reports", permission = "View")
	@RequestMapping("cms/view-reports")
	public String reportPage() {
		return "cms/view-reports";
	}

}
