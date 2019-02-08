package org.sdrc.cysdrf.service;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.sdrc.cysdrf.domain.ReportDetails;
import org.sdrc.cysdrf.domain.ReportTypeDetails;
import org.sdrc.cysdrf.domain.SectionDetails;
import org.sdrc.cysdrf.domain.TagDetails;
import org.sdrc.cysdrf.domain.UserDetail;
import org.sdrc.cysdrf.model.ReportDetailsModel;
import org.sdrc.cysdrf.model.ReportTypeDetailsModel;
import org.sdrc.cysdrf.model.SectionDetailsModel;
import org.sdrc.cysdrf.model.TagDetailsModel;
import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.reposiotry.CMSReportDetailsRepository;
import org.sdrc.cysdrf.reposiotry.CMSReportTypeDetailsRepository;
import org.sdrc.cysdrf.reposiotry.CMSSectionDetailsRepository;
import org.sdrc.cysdrf.reposiotry.CMSTagDetailsRepository;
import org.sdrc.cysdrf.util.Constants;
import org.sdrc.cysdrf.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Service
public class CMSServiceImpl implements CMSService {

	@Autowired
	CMSSectionDetailsRepository cmsSectionDetalsRepository;

	@Autowired
	CMSReportDetailsRepository cmsReportDetailsRepository;

	@Autowired
	CMSTagDetailsRepository cmsTagDetailsRepository;

	@Autowired
	private StateManager stateManager;

	@Autowired
	private ResourceBundleMessageSource cmsMessage;

	@Autowired
	private CMSReportTypeDetailsRepository cmsReportTypeDetailsRepository;

	@Override
	@Transactional
	public List<SectionDetailsModel> getAllSections() {
		List<SectionDetails> sectionDetails = cmsSectionDetalsRepository
				.findAllSections();

		List<SectionDetailsModel> sectionDetailsModels = new ArrayList<>();

		for (SectionDetails secionDetail : sectionDetails) {
			String tags = null;
			SectionDetailsModel sectionDetailsModel = new SectionDetailsModel();

			sectionDetailsModel.setSectionId(secionDetail.getSectionId());
			sectionDetailsModel.setSectionName(secionDetail.getSectionName());
			sectionDetailsModel.setParentId(secionDetail.getParentId());
			sectionDetailsModel.setType(secionDetail.getType());
			sectionDetailsModel.setDescription(secionDetail.getDescription());
			sectionDetailsModel.setAlive(secionDetail.isAlive());
			if (secionDetail.getTagDetails() != null) {
				for (TagDetails tag : secionDetail.getTagDetails()) {
					if (tags == null) {
						tags = tag.getTagName();
					} else {
						tags += "," + tag.getTagName();
					}

				}
			}
			sectionDetailsModel.setTagName(tags);
			sectionDetailsModels.add(sectionDetailsModel);
		}

		// TODO Auto-generated method stub
		return sectionDetailsModels;
	}

	@Override
	public List<ReportDetailsModel> getAllReports() {
		List<ReportDetails> reportsDetail;
		Integer roleId = Integer.parseInt(cmsMessage.getMessage("roleId", null,
				null));
		if (roleId == ((UserDetailModel) stateManager
				.getValue(Constants.USER_PRINCIPAL))
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId()) {
			reportsDetail = cmsReportDetailsRepository
					.findAllReportByIsLiveTrue();
		} else {
			reportsDetail = cmsReportDetailsRepository
					.findAllNonConfidentialReportByIsLiveTrue();
		}
		List<TagDetails> tagDetails = cmsTagDetailsRepository.findAllTag();

		List<ReportDetailsModel> reportsDetailsModels = new ArrayList<ReportDetailsModel>();

		for (ReportDetails reportDetails : reportsDetail) {

			ReportDetailsModel reportDetailsModel = new ReportDetailsModel();
			String tagsName = null;
			reportDetailsModel.setReportName(reportDetails.getReportName());
			reportDetailsModel.setReportId(reportDetails.getReportId());
			reportDetailsModel.setAlive(reportDetails.isAlive());
			reportDetailsModel.setDescription(reportDetails.getDescription());
			reportDetailsModel.setFileType(reportDetails.getFileType());
			reportDetailsModel.setType(reportDetails.getType());
			if (reportDetails.getType().equalsIgnoreCase("Photographs")) {
				String path = reportDetails.getUrl();
				List<String> urls = new ArrayList<String>(Arrays.asList(path
						.split(",")));
				path = null;
				for (String url : urls) {
					String tempUrl = imageURLtoByte(url);
					if (path == null) {
						path = tempUrl;
					} else {
						path += "}" + tempUrl;
					}
				}
				reportDetailsModel.setUrl(path);
			} else {
				reportDetailsModel.setUrl(reportDetails.getUrl());
			}
			reportDetailsModel.setParentReportId(reportDetails
					.getParentReportId());
			if (reportDetails.getUpdatedDate() != null) {
				reportDetailsModel.setUpdatedBy(reportDetails.getUpdatedBy());
				reportDetailsModel.setUpdatedDate(reportDetails
						.getUpdatedDate().toString());
			}
			reportDetailsModel.setUploadedBy(reportDetails.getUploadedBy());
			reportDetailsModel.setUploadedDate(reportDetails.getUploadedDate()
					.toString());

			reportDetailsModel.setUserId(reportDetails.getUserDetail()
					.getUserDetailId());
			reportDetailsModel.setSectionId(reportDetails.getSectionDetails()
					.getParentId());
			reportDetailsModel.setThemeId(reportDetails.getSectionDetails()
					.getSectionId());
			reportDetailsModel.setConfidential(reportDetails.isConfidential());

			String tags = reportDetails.getTags();
			List<String> tagsId = new ArrayList<String>(Arrays.asList(tags
					.split(",")));
			for (TagDetails tagDetail : tagDetails) {
				if (tagsId.isEmpty() || tagsId == null) {
					break;
				} else {
					if (tagsId.contains(tagDetail.getTagId().toString())) {
						if (tagsName == null) {
							tagsName = tagDetail.getTagName();
						} else {
							tagsName += ", " + tagDetail.getTagName();
						}
						tagsId.remove(tagDetail.getTagId().toString());
					}

				}
			}
			reportDetailsModel.setTags(tagsName);

			reportsDetailsModels.add(reportDetailsModel);
		}
		return reportsDetailsModels;
	}

	@Override
	@Transactional
	public boolean editSection(String sectionName, Integer sectionId,
			List<String> tags, String description, Integer parentId) {

		try {
			if (cmsSectionDetalsRepository
					.getSectionDetailByParentIdAndSectionName(parentId,
							sectionName) == null
					|| cmsSectionDetalsRepository
							.getSectionDetailByParentIdAndSectionName(parentId,
									sectionName).getSectionId() == sectionId) {
				cmsSectionDetalsRepository.updateSectionById(sectionName,
						sectionId, description);
				if (!tags.isEmpty()) {
					saveTags(tags, sectionId);
				}
				return true;
			}

			else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		// TODO Auto-generated method stub
	}

	// this method will return all the tags id for the respective tags added by user while uploading documnent.
	@Transactional
	private String saveTags(List<String> tagsName, Integer sectionId) {

		String tagsId = null;
		List<TagDetails> tagsDetail = new ArrayList<TagDetails>();
		List<TagDetails> tagDetails = cmsTagDetailsRepository
				.findAllTagBySectionID(sectionId);
		//to remove the duplicate tags
		tagsName=tagsName.stream().distinct().collect(Collectors.toList());
		
		//to get the ids of already present tag in database and concat it seperated by ','
		for (TagDetails tagName : tagDetails) {
			if (!tagsName.isEmpty()) {
				if (tagsName.contains(tagName.getTagName())) {
					if (tagsId == null) {
						tagsId = tagName.getTagId().toString();

					} else {
						tagsId += ",";
						tagsId = tagsId + tagName.getTagId().toString();
					}
					tagsName.remove(tagName.getTagName());
				}
			} else {
				break;
			}
		}
		// saving new tags
		for (String tagname : tagsName) {
			TagDetails tagDetail = new TagDetails();
			tagDetail.setTagName(tagname.trim());
			tagDetail.setSectionDetails(new SectionDetails(sectionId));
			tagDetail.setAlive(true);
			tagsDetail.add(tagDetail);
		}

		List<TagDetails> savedTags = cmsTagDetailsRepository.save(tagsDetail);

		//adding ids of newly generated tags to tgaId 
		for (TagDetails tag : savedTags) {
			if (tagsId == null) {
				tagsId = tag.getTagId().toString();

			} else {
				tagsId += ",";
				tagsId = tagsId + tag.getTagId().toString();
			}
		}
		if (tagsId != null)
			return tagsId;
		else
			return "";
	}

	@Override
	@Transactional
	public boolean newSection(String sectionName, Integer parentId,
			List<String> tags, String description) {

		try {
			if (cmsSectionDetalsRepository
					.getSectionDetailByParentIdAndSectionName(parentId,
							sectionName) == null) {
				SectionDetails sectionDetails = new SectionDetails();

				sectionDetails.setSectionName(sectionName);
				sectionDetails.setAlive(true);
				sectionDetails.setDescription(description);
				if (parentId != -1) {
					sectionDetails.setParentId(parentId);
					sectionDetails.setType("Thematics");
				} else {
					sectionDetails.setParentId(-1);
					sectionDetails.setType("Section");
				}
				SectionDetails section = cmsSectionDetalsRepository
						.save(sectionDetails);
				if (!tags.isEmpty()) {
					saveTags(tags, section.getSectionId());
				}

				return true;
				// TODO Auto-generated method stub
			} else {
				return false;

			}
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	@Transactional
	public void updateRecord(Integer reportId, String reportName,
			String description, String tags) {
		ReportDetails oldReportDetails = cmsReportDetailsRepository
				.findReportDetailsById(reportId);
		ReportDetails reportDetails = new ReportDetails();
		reportDetails.setAlive(true);
		reportDetails.setFileType(oldReportDetails.getFileType());
		reportDetails.setParentReportId(oldReportDetails.getReportId());
		reportDetails.setSectionDetails(oldReportDetails.getSectionDetails());
		reportDetails.setType(oldReportDetails.getType());
		reportDetails.setUpdatedDate(new Timestamp(new Date().getTime()));
		reportDetails.setUploadedBy(oldReportDetails.getUploadedBy());
		reportDetails.setUploadedDate(oldReportDetails.getUploadedDate());
		reportDetails.setUrl(oldReportDetails.getUrl());
		reportDetails.setUserDetail(oldReportDetails.getUserDetail());
		reportDetails.setUpdatedBy(((UserDetailModel) stateManager
				.getValue(Constants.USER_PRINCIPAL)).getUserName());
		reportDetails.setReportName(reportName);
		reportDetails.setDescription(description);

		List<String> tagNames;
		if (tags == null || tags.trim().equalsIgnoreCase("")) {
			tagNames = new ArrayList<String>();
		} else {
			tagNames = new ArrayList<String>(Arrays.asList(tags.split(",")));
		}
		String tagId = saveTags(tagNames, oldReportDetails.getSectionDetails()
				.getSectionId());

		reportDetails.setTags(tagId);

		oldReportDetails.setAlive(false);
		cmsReportDetailsRepository.save(oldReportDetails);
		cmsReportDetailsRepository.save(reportDetails);
	}

	@Transactional
	@Override
	public String saveReport(String reportTitle, String desc,
			Integer parentReportId, String reportType, String url,
			Integer sectionId, String tags, MultipartFile[] files,
			MultipartFile singlefile) {

		Integer roleId = Integer.parseInt(cmsMessage.getMessage("roleId", null,
				null));
		// TODO Auto-generated method stub

		// Saving files to the server
		String fileName = null;
		String msg = "";
		String path = "C://CYSD";
		File filePath = new File(path);
		if (!filePath.exists())
			filePath.mkdir();
		if (files != null && files.length > 0) {
			url = null;

			for (int i = 0; i < files.length; i++) {
				try {
					fileName = files[i].getOriginalFilename();
					byte[] bytes = files[i].getBytes();

					File directory;
					if (reportType.equalsIgnoreCase("Photographs"))
						directory = new File(path + "/reports/gallery");
					else
						directory = new File(path + "/reports/documents");

					if (!directory.exists())
						directory.mkdirs();
					File file = new File(directory.getAbsolutePath()
							+ File.separator + fileName);

					BufferedOutputStream buffStream = new BufferedOutputStream(
							new FileOutputStream(file));
					if (reportType.equalsIgnoreCase("Photographs")) {
						float imageQuality = 0.5f;

						// Create the buffered image
						BufferedImage bufferedImage = ImageIO.read(files[i]
								.getInputStream());

						// Get image writers
						Iterator<ImageWriter> imageWriters = ImageIO
								.getImageWritersByFormatName("jpg");

						ImageWriter imageWriter = (ImageWriter) imageWriters
								.next();
						ImageOutputStream imageOutputStream = ImageIO
								.createImageOutputStream(buffStream);
						imageWriter.setOutput(imageOutputStream);

						ImageWriteParam imageWriteParam = imageWriter
								.getDefaultWriteParam();

						// Set the compress quality metrics
						imageWriteParam
								.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
						imageWriteParam.setCompressionQuality(imageQuality);
						imageWriter.write(null, new IIOImage(bufferedImage,
								null, null), imageWriteParam);

					}
					if (url == null || url.isEmpty()) {
						url = directory.getAbsolutePath() + File.separator
								+ fileName;
					} else {
						url += "," + directory.getAbsolutePath()
								+ File.separator + fileName;
					}
					if (!reportType.equalsIgnoreCase("Photographs"))
						buffStream.write(bytes);
					buffStream.close();
					msg += "You have successfully uploaded " + fileName
							+ "<br/>";
				} catch (Exception e) {
					return "You failed to upload " + fileName + ": "
							+ e.getMessage() + "<br/>";
				}
			}
		}

		else if (singlefile != null) {
			try {
				fileName = singlefile.getOriginalFilename();
				byte[] bytes = singlefile.getBytes();

				File directory = new File(path + "/reports");
				if (!directory.exists())
					directory.mkdirs();
				File file = new File(directory.getAbsolutePath()
						+ File.separator + fileName);
				BufferedOutputStream buffStream = new BufferedOutputStream(
						new FileOutputStream(file));

				url = directory.getAbsolutePath() + File.separator + fileName;
				buffStream.write(bytes);
				buffStream.close();
				msg += "You have successfully uploaded " + fileName + "<br/>";
			} catch (Exception e) {
				return "You failed to upload " + fileName + ": "
						+ e.getMessage() + "<br/>";
			}
		}

		// saving tags to the database
		List<String> tagNames;
		String tagIds;
		if (tags == null || tags.trim().equalsIgnoreCase("")) {
			tagNames = new ArrayList<String>();
			tagIds = "noTag";
		} else {
			tagNames = new ArrayList<String>(Arrays.asList(tags.split(",")));
			tagIds = saveTags(tagNames, sectionId);
		}

		// saving report in the database
		ReportDetails reportDetails = new ReportDetails();
		reportDetails.setReportName(reportTitle);
		reportDetails.setDescription(desc);
		reportDetails.setType(reportType);
		reportDetails.setUrl(url);
		reportDetails.setSectionDetails(new SectionDetails(sectionId));
		reportDetails.setTags(tagIds);
		reportDetails.setAlive(true);
		reportDetails.setParentReportId(parentReportId);
		reportDetails.setUserDetail(new UserDetail(
				((UserDetailModel) stateManager
						.getValue(Constants.USER_PRINCIPAL)).getUserId()));
		reportDetails.setUploadedBy(((UserDetailModel) stateManager
				.getValue(Constants.USER_PRINCIPAL)).getUserName());
		reportDetails.setUploadedDate(new Timestamp(new Date().getTime()));
		if (roleId == ((UserDetailModel) stateManager
				.getValue(Constants.USER_PRINCIPAL))
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId()) {
			reportDetails.setConfidential(true);
		} else {
			reportDetails.setConfidential(false);
		}

		cmsReportDetailsRepository.save(reportDetails);
		return msg;
	}

	@Override
	@Transactional
	public List<TagDetailsModel> getAllTags() {

		List<TagDetails> tagDetails = cmsTagDetailsRepository.findAllTag();

		List<TagDetailsModel> tagDetailsModels = new ArrayList<TagDetailsModel>();
		for (TagDetails tag : tagDetails) {
			TagDetailsModel tagDetailsModel = new TagDetailsModel();

			tagDetailsModel.setTagName(tag.getTagName());
			tagDetailsModel.setTagId(tag.getTagId());

			tagDetailsModels.add(tagDetailsModel);

		}
		// TODO Auto-generated method stub
		return tagDetailsModels;
	}

	@Override
	public List<ReportTypeDetailsModel> getAllReportType() 
	{
		Integer roleId = Integer.parseInt(cmsMessage.getMessage("roleId", null,
				null));
		Integer reportTypeId=Integer.parseInt(cmsMessage.getMessage("reportTypeId", null,
				null));
		List<ReportTypeDetails> reportTypeDetails;
		if (roleId == ((UserDetailModel) stateManager
				.getValue(Constants.USER_PRINCIPAL))
				.getUserRoleFeaturePermissionMappings().get(0)
				.getRoleFeaturePermissionSchemeModel().getRole().getRoleId()) 
		{
		
			reportTypeDetails= cmsReportTypeDetailsRepository
					.findAllReportType();
		}
		else
		{
			reportTypeDetails= cmsReportTypeDetailsRepository
					.findAllReportTypeForNonConfendtialType(reportTypeId);
		}
		List<ReportTypeDetailsModel> reportTypeDetailsModels = new ArrayList<ReportTypeDetailsModel>();
		for (ReportTypeDetails reportType : reportTypeDetails) {
			ReportTypeDetailsModel detailsModel = new ReportTypeDetailsModel();
			detailsModel.setReportTypeId(reportType.getReportTypeId());
			detailsModel.setReportTypeName(reportType.getReportTypeName());
			detailsModel.setDescription(reportType.getDescription());
			detailsModel.setFileType(reportType.getFileType());

			reportTypeDetailsModels.add(detailsModel);
		}

		// TODO Auto-generated method stub
		return reportTypeDetailsModels;
	}

	@Override
	public String getReportDocumentById(Integer reportId) {
		ReportDetails reportDetails = cmsReportDetailsRepository
				.findReportDetailsById(reportId);
		// TODO Auto-generated method stub
		return reportDetails.getUrl();
	}

	@Override
	@Transactional
	public boolean deleteReport(Integer reportId) {

		// TODO Auto-generated method stub
		try {
			cmsReportDetailsRepository.deleteRecord(reportId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private String imageURLtoByte(String url) {
		try {
			BufferedImage image = ImageIO.read(new File(url));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			/*byte[] res = baos.toByteArray();*/
			String encodedImage = "data:image/png;base64,"
					+ Base64.encode(baos.toByteArray());

			return encodedImage;
		} catch (Exception e) {
			return "../resources/images/image-notFound.png";
		}
	}
}
