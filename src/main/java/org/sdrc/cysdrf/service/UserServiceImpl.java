package org.sdrc.cysdrf.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.sdrc.cysdrf.domain.RoleFeaturePermissionScheme;
import org.sdrc.cysdrf.domain.UserDetail;
import org.sdrc.cysdrf.domain.UserLoginMeta;
import org.sdrc.cysdrf.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.cysdrf.model.TimePeriodModel;
import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.model.UserRoleSchemeModel;
import org.sdrc.cysdrf.reposiotry.RoleFeaturePermissionSchemeRepository;
import org.sdrc.cysdrf.reposiotry.UserDetailsRepository;
import org.sdrc.cysdrf.reposiotry.UserLoginMetaRepository;
import org.sdrc.cysdrf.reposiotry.UserRoleFeaturePermissionMappingRepository;
import org.sdrc.cysdrf.util.Constants;
import org.sdrc.cysdrf.util.DomainToModelConverter;
import org.sdrc.cysdrf.util.StateManager;
import org.sdrc.devinfo.domain.UtAreaEn;
import org.sdrc.devinfo.domain.UtTimeperiod;
import org.sdrc.devinfo.repository.AreaRepository;
import org.sdrc.devinfo.repository.TimeperiodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@Autowired
	private UserLoginMetaRepository userLoginMetaRepository;
	
	@Autowired
	private StateManager stateManager;
	
	@Autowired
	private UserRoleFeaturePermissionMappingRepository userRoleFeaturePermissionMappingRepository;
	
	@Autowired
	private RoleFeaturePermissionSchemeRepository roleFeaturePermissionSchemeRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private TimeperiodRepository timeperiodRepository;
	
	private SimpleDateFormat sdf1 = new SimpleDateFormat(ResourceBundle.getBundle("spring/app").getString("datetime.format"));
	private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ResourceBundle.getBundle("spring/app").getString("datetime.format"));
	
	//get model from a userdetail object
	@Override
	@Transactional(value="webTransactionManager",readOnly = true)
	public UserDetailModel setUserDetail(String emailId){
		UserDetail userDetail = userDetailsRepository.findByEmailId(emailId);
		UserDetailModel userDetailModel = null;
		if(null!=userDetail){
			userDetailModel = new UserDetailModel();
			userDetailModel.setUserId(userDetail.getUserDetailId());
			userDetailModel.setEmail(userDetail.getEmailId());
			userDetailModel.setUserName(userDetail.getUserName());
			userDetailModel.setLive(userDetail.isLive());
			userDetailModel.setContactNum(userDetail.getContactNo());
			userDetailModel.setPassword(userDetail.getPassword());
			userDetailModel.setUserRoleFeaturePermissionMappings(
					DomainToModelConverter.toUserRoleFeaturePermissionMappingModels(userDetail.getUserRoleFeaturePermissionMappings()));
		}
		return userDetailModel;
	}
	
	//save login meta of user log txn
	@Override
	@Transactional("webTransactionManager")
	public long saveUserLoginMeta(String ipAddress, Integer userId, String userAgent) {
		UserLoginMeta userLoginMeta = new UserLoginMeta();
		userLoginMeta.setUserIpAddress(ipAddress);
		userLoginMeta.setUserDetail(new UserDetail(userId));
		userLoginMeta.setLoggedInDateTime(new Timestamp(new Date().getTime()));
		userLoginMeta.setUserAgent(userAgent);
		return userLoginMetaRepository.save(userLoginMeta).getUserLogInMetaId();
	}
	
	//add a new user
	@Override
	@Transactional("webTransactionManager")
	public void addNewUser(String userName,String emailId, String contactNum, String password){
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		UserDetail userDetail = new UserDetail();
		userDetail.setUserName(userName);
		userDetail.setContactNo(contactNum);
		userDetail.setEmailId(emailId);
		userDetail.setPassword(password);
		userDetail.setLive(true);
		userDetail.setCreatedBy(userDetailModel.getUserName());
		userDetail.setCreatedDate(new Timestamp(new Date().getTime()));
		userDetailsRepository.save(userDetail);
	}
	
	//active / deactive user 
	@Override
	@Transactional("webTransactionManager")
	public boolean activeDeactiveUser(Integer userId, boolean isLive){
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		userDetailsRepository.setIsLive(isLive, userDetailModel.getUserName(), new Timestamp(new Date().getTime()), userId);
		if(isLive==false){
			userRoleFeaturePermissionMappingRepository.deleteByUserId(userId);
		}
		return true;
	}
	
	//get all user-role
	@Override
	public List<UserRoleSchemeModel> findAll(){
//		List<UserRoleFeaturePermissionMapping> mappings = userRoleFeaturePermissionMappingRepository.findAll();
		List<RoleFeaturePermissionScheme> schemes = roleFeaturePermissionSchemeRepository.findByMaxRoleFeaturePermissionSchemeId();
		List<UserRoleSchemeModel> userRoleSchemeModels = new ArrayList<>();
		Map<String,String> areaMap = getAreaListDetails();
		
		for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : schemes) {
			UserRoleSchemeModel userRoleSchemeModel = new UserRoleSchemeModel();
			userRoleSchemeModel.setAreaCode(roleFeaturePermissionScheme.getAreaCode());
			userRoleSchemeModel.setAreaName(roleFeaturePermissionScheme.getAreaCode().equals("IND021") ?
					"Odisha" : areaMap.get(roleFeaturePermissionScheme.getAreaCode()));
			userRoleSchemeModel.setContactNum(roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()!=null 
					&& roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().size()!=0 ? 
					roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().get(0).getUserDetail().getContactNo() : "");
			userRoleSchemeModel.setEmailId(roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()!=null 
					&& roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().size()!=0 ? 
					roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().get(0).getUserDetail().getEmailId() : "");
			userRoleSchemeModel.setUserRoleSchemeId(roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()!=null 
					&& roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().size()!=0 ? 
					roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().get(0).getUserRoleFeaturePermissionId() : 0);
			userRoleSchemeModel.setRoleSchemeId(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId());
			userRoleSchemeModel.setRoleName(roleFeaturePermissionScheme.getRole().getRoleName());
			userRoleSchemeModel.setUserId(roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()!=null 
					&& roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().size()!=0 ? 
					roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().get(0).getUserDetail().getUserDetailId() : 0);
			userRoleSchemeModel.setUserName(roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()!=null 
					&& roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().size()!=0 ? 
					roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().get(0).getUserDetail().getUserName() : "");
			userRoleSchemeModel.setAttached(roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()!=null 
					&& roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings().size()!=0 ? true : false);
			userRoleSchemeModel.setSchemeName(roleFeaturePermissionScheme.getSchemeName());
			userRoleSchemeModels.add(userRoleSchemeModel);
		}
		return userRoleSchemeModels;
	}
	
	//get all user
	@Override
	public List<UserDetailModel> findAllUser(){
		List<UserDetailModel> userDetailModels = new ArrayList<>();
		List<UserDetail> userDetails = userDetailsRepository.findAll();
		for (UserDetail userDetail : userDetails) {
			UserDetailModel userDetailModel = getModel(userDetail);
			userDetailModels.add(userDetailModel);
		}
		return userDetailModels;
	}
	
	@Override
	public List<UserDetailModel> getUsersNotAttachedToRole(){
		List<UserDetailModel> userDetailModels = new ArrayList<>();
		List<UserDetail> userDetails = userDetailsRepository.findUserDetailNotInUserRoleFeaturePermissionMapping();
		for (UserDetail userDetail : userDetails) {
			UserDetailModel userDetailModel = getModel(userDetail);
			userDetailModels.add(userDetailModel);
		}
		return userDetailModels;
	}
	
	//get UserDetailModel from UserDetail
	private UserDetailModel getModel(UserDetail userDetail){
		UserDetailModel userDetailModel = new UserDetailModel();
		userDetailModel.setContactNum(userDetail.getContactNo());
		userDetailModel.setCreatedBy(userDetail.getCreatedBy());
//		userDetailModel.setCreatedDate(userDetail.getCreatedDate());
		userDetailModel.setEmail(userDetail.getEmailId());
		userDetailModel.setLive(userDetail.isLive());
		userDetailModel.setUserId(userDetail.getUserDetailId());
		userDetailModel.setUserName(userDetail.getUserName());
		return userDetailModel;
	}
	
	//attach a role
	@Override
	@Transactional("webTransactionManager")
	public boolean attachRole(Integer roleFeaturePermissionSchemeId, String schemeName, Integer userId){
		UserDetailModel userDetailModel = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes = roleFeaturePermissionSchemeRepository.findBySchemeName(schemeName);
		for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemes) {
			UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping = new UserRoleFeaturePermissionMapping();
			userRoleFeaturePermissionMapping.setRoleFeaturePermissionScheme
					(roleFeaturePermissionSchemeRepository.findByRoleFeaturePermissionSchemeId
							(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId()));
			userRoleFeaturePermissionMapping.setUserDetail(userDetailsRepository.findByUserDetailId(userId));
			userRoleFeaturePermissionMapping.setUpdatedBy(userDetailModel.getUserName());
			userRoleFeaturePermissionMapping.setUpdatedDate(new Timestamp(new Date().getTime()));
			userRoleFeaturePermissionMappingRepository.save(userRoleFeaturePermissionMapping);
		}
		return true;
	}
	
	//detach a role from the user
	@Override
	@Transactional("webTransactionManager")
	public void detachRole(Integer userId){
		userRoleFeaturePermissionMappingRepository.deleteByUserId(userId);
	}
	
	//get areaId and Name from a list of area
	public Map<String,String> getAreaListDetails(){
		
		List<UtAreaEn> areaDetails= areaRepository.getAllAreaDetails();
		
		Map<String,String> areaMap = new HashMap<String,String>(areaDetails.size());
		
		for(UtAreaEn area : areaDetails){
			areaMap.put(area.getArea_ID(), area.getArea_Name());
		}
	
		
		return areaMap;
	}
	
	@Override
	@Transactional("webTransactionManager")
	public void updateLoggedOutStatus(long userLoginMetaId, Timestamp loggedOutDateTime){
		userLoginMetaRepository.updateStatus(loggedOutDateTime, userLoginMetaId);
	}
	
	//create the current time period in ut_timeperiod if not present
	@Override
	public void createCurrentTimeperiod() throws ParseException{
		LocalDateTime currentDate = LocalDateTime.now();
		List<UtTimeperiod> allTimeperiods = timeperiodRepository.getAllTimeperiod();
		UtTimeperiod lastTimePeriod = allTimeperiods.get(allTimeperiods.size()-1); //get the last row i.e the latest timeperiod
		String checkStartDate = sdf1.format(lastTimePeriod.getStartDate());
		String checkEndDate = sdf1.format(lastTimePeriod.getEndDate());
		
		LocalDateTime startDateTime = LocalDateTime.parse(checkStartDate, dateTimeFormatter);
		LocalDateTime endDateTime = LocalDateTime.parse(checkEndDate, dateTimeFormatter);
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		
		Date date = new Date();
		Date startDate = null;
		Date endDate = null;
		String currDate = "";
		
		currDate = formatter.format(date);
		
		
		startDate = formatter.parse(checkStartDate);
		endDate = formatter.parse(checkEndDate);
		date = formatter.parse(currDate);
			
		if(((currentDate.isAfter(startDateTime)
				&& currentDate.isBefore(endDateTime) )
				|| (date.compareTo(startDate)==0 || date.compareTo(endDate) ==0))==false){ // if the current time period is not available in the latest time period
			TimePeriodModel timePeriodModel = checkQuarter(currentDate.getMonthValue(), currentDate.getYear());
				saveTimeperiod(timePeriodModel.getTimePeriod(),  sdf1.parse(timePeriodModel.getStartDate()),
						 sdf1.parse(timePeriodModel.getEndDate()), timePeriodModel.getPeriodicity());
		}
		
	}
	//get the timeperiod specific formats according to month of the year
	private TimePeriodModel checkQuarter(int monthValue, int year){
		
		TimePeriodModel timePeriodModel = new TimePeriodModel();
		
		if(monthValue>=1 && monthValue<=3){
			timePeriodModel.setTimePeriod(year+".01-"+year+".03");
			timePeriodModel.setStartDate(year+"-01-01 00:00:00.000");
			timePeriodModel.setEndDate(year+"-03-31 00:00:00.000");
			timePeriodModel.setPeriodicity((year-1)+"-"+year);
		}else if(monthValue>=4 && monthValue<=6){
			timePeriodModel.setTimePeriod(year+".04-"+year+".06");
			timePeriodModel.setStartDate(year+"-04-01 00:00:00.000");
			timePeriodModel.setEndDate(year+"-06-30 00:00:00.000");
			timePeriodModel.setPeriodicity(year+"-"+(year+1));
		}else if(monthValue>=7 && monthValue<=9){
			timePeriodModel.setTimePeriod(year+".07-"+year+".09");
			timePeriodModel.setStartDate(year+"-07-01 00:00:00.000");
			timePeriodModel.setEndDate(year+"-09-30 00:00:00.000");
			timePeriodModel.setPeriodicity(year+"-"+(year+1));
		}else if(monthValue>=10 && monthValue<=12){
			timePeriodModel.setTimePeriod(year+".10-"+year+".12");
			timePeriodModel.setStartDate(year+"-10-01 00:00:00.000");
			timePeriodModel.setEndDate(year+"-12-31 00:00:00.000");
			timePeriodModel.setPeriodicity(year+"-"+(year+1));
		}
		
		return timePeriodModel;
	}
	
	//persist into db
	private void saveTimeperiod(String timePeriod, Date startDate, Date endDate, String periodicity) {
		UtTimeperiod utTimeperiod = new UtTimeperiod();
		utTimeperiod.setTimePeriod(timePeriod);
		utTimeperiod.setStartDate(startDate);
		utTimeperiod.setEndDate(endDate);
		utTimeperiod.setPeriodicity(periodicity);
		timeperiodRepository.save(utTimeperiod);
	}
}
