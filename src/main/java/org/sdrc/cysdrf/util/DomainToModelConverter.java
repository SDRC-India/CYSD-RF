package org.sdrc.cysdrf.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sdrc.cysdrf.domain.Feature;
import org.sdrc.cysdrf.domain.FeaturePermissionMapping;
import org.sdrc.cysdrf.domain.Permission;
import org.sdrc.cysdrf.domain.Role;
import org.sdrc.cysdrf.domain.RoleFeaturePermissionScheme;
import org.sdrc.cysdrf.domain.UserDetail;
import org.sdrc.cysdrf.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.cysdrf.model.FeatureModel;
import org.sdrc.cysdrf.model.FeaturePermissionMappingModel;
import org.sdrc.cysdrf.model.PermissionModel;
import org.sdrc.cysdrf.model.RoleFeaturePermissionSchemeModel;
import org.sdrc.cysdrf.model.RoleModel;
import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.model.UserRoleFeaturePermissionMappingModel;
import org.springframework.stereotype.Component;

@Component
public class DomainToModelConverter {
	
	public static List<ValueObject> toUserRoleFeaturePermissionMappingValueObjs (
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings ){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
			ValueObject valueObject = new ValueObject(Integer.toString(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId()),
					userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme().getSchemeName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static List<UserDetailModel> toUserDetailModels(List<UserDetail> userDetails){
		List<UserDetailModel> userDetailModels= new ArrayList<>();
		for (UserDetail userDetail : userDetails) {
			UserDetailModel userDetailModel = new UserDetailModel();
			userDetailModel.setContactNum(userDetail.getContactNo());
			userDetailModel.setEmail(userDetail.getEmailId());
			userDetailModel.setLive(userDetail.isLive());
			userDetailModel.setPassword(userDetail.getPassword());
			userDetailModel.setUpdatedBy(userDetail.getUpdatedBy());
//			userDetailModel.setUpdatedDate(userDetail.getUpdatedDate());
			userDetailModel.setUserId(userDetail.getUserDetailId());
			userDetailModel.setUserRoleFeaturePermissionMappings(
					toUserRoleFeaturePermissionMappingModels(userDetail.getUserRoleFeaturePermissionMappings()));
			userDetailModel.setUserName(userDetail.getUserName());
			userDetailModels.add(userDetailModel);
		}
		return userDetailModels;
	}
	
	public static List<FeaturePermissionMappingModel> toFeaturePermissionMappings(List<FeaturePermissionMapping> featurePermissionMappings){
		List<FeaturePermissionMappingModel> featurePermissionMappingModels = new ArrayList<>();
		for (FeaturePermissionMapping featurePermissionMapping : featurePermissionMappings) {
			FeaturePermissionMappingModel featurePermissionMappingModel = new FeaturePermissionMappingModel();
			featurePermissionMappingModel.setFeaturePermissionId(featurePermissionMapping.getFeaturePermissionId());
			featurePermissionMappingModel.setUpdatedBy(featurePermissionMapping.getUpdatedBy());
//			featurePermissionMappingModel.setUpdatedDate(featurePermissionMapping.getUpdatedDate());
			featurePermissionMappingModel.setPermission(toPermissionModel(featurePermissionMapping.getPermission()));
			featurePermissionMappingModel.setFeature(toFeatureModels(Arrays.asList(featurePermissionMapping.getFeature())).get(0));
			featurePermissionMappingModel.setRoleFeaturePermissionSchemeModels(
					toRoleFeaturePermissionSchemeModelValueObjs(featurePermissionMapping.getRoleFeaturePermissionSchemes()));
			featurePermissionMappingModels.add(featurePermissionMappingModel);
		}
		return featurePermissionMappingModels;
	}
	
	public static List<ValueObject> toRoleFeaturePermissionSchemeModelValueObjs(List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemes) {
			ValueObject valueObject = new ValueObject(Integer.toString(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId()),
					roleFeaturePermissionScheme.getAreaCode());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static ValueObject toUserDetailModelValueObjs(UserDetail userDetail){
			ValueObject valueObject = new ValueObject(Integer.toString(userDetail.getUserDetailId()),
					userDetail.getUserName());
		return valueObject;
	}
	
	public static List<FeatureModel> toFeatureModels(List<Feature> features){
		List<FeatureModel> featureModels =new ArrayList<>();
		for (Feature feature : features) {
			FeatureModel featureModel = new FeatureModel();
			featureModel.setFeatureId(feature.getFeatureId());
			featureModel.setFeatureCode(feature.getFeatureCode());
			featureModel.setDescription(feature.getDescription());
			featureModel.setFeatureName(feature.getFeatureName());
			featureModel.setFeaturePermissionMappings(toFeaturePermissionMappingModelValueObjs(feature.getFeaturePermissionMappings()));
			featureModel.setUpdatedBy(feature.getUpdatedBy());
//			featureModel.setUpdatedDate(feature.getUpdatedDate());
			featureModels.add(featureModel);
		}
		return featureModels;
	}
	
	public static List<ValueObject> toFeaturePermissionMappingModelValueObjs(List<FeaturePermissionMapping> featurePermissionMappings){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (FeaturePermissionMapping featurePermissionMapping : featurePermissionMappings) {
			ValueObject valueObject = new ValueObject(Integer.toString(featurePermissionMapping.getFeaturePermissionId()),
					featurePermissionMapping.getPermission().getPermissionName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static PermissionModel toPermissionModel(Permission permission){
		PermissionModel permissionModel = new PermissionModel();
		permissionModel.setPermissionId(permission.getPermissionId());
		permissionModel.setPermissionCode(permission.getPermissionCode());
		permissionModel.setDescription(permission.getDescription());
		permissionModel.setUpdatedBy(permission.getUpdatedBy());
		permissionModel.setPermissionName(permission.getPermissionName());
//		permissionModel.setUpdatedDate(permission.getUpdatedDate());
		permissionModel.setFeaturePermissionMappings(toFeaturePermissionMappingModelValueObjs(permission.getFeaturePermissionMappings()));
		
		return permissionModel;
	}
	
	public static RoleModel toRoleModel(Role role){
		RoleModel roleModel = new RoleModel();
		roleModel.setDescription(role.getDescription());
		roleModel.setRoleCode(role.getRoleCode());
		roleModel.setRoleFeaturePermissionSchemes(toRoleFeaturePermissionSchemeModelValueObjs(role.getRoleFeaturePermissionSchemes()));
		roleModel.setRoleId(role.getRoleId());
		roleModel.setRoleName(role.getRoleName());
		roleModel.setUpdatedBy(role.getUpdatedBy());
//		roleModel.setUpdatedDate(role.getUpdatedDate());
		return roleModel;
	}
	
	
	public static List<RoleFeaturePermissionSchemeModel> toRoleFeaturePermissionSchemeModels(
			List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes){
		List<RoleFeaturePermissionSchemeModel> roleFeaturePermissionSchemeModels = new ArrayList<>();
		for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemes) {
			RoleFeaturePermissionSchemeModel roleFeaturePermissionSchemeModel = new RoleFeaturePermissionSchemeModel();
			roleFeaturePermissionSchemeModel.setAreaCode(roleFeaturePermissionScheme.getAreaCode());
			roleFeaturePermissionSchemeModel.setFeaturePermissionMapping(
					toFeaturePermissionMappings(Arrays.asList(roleFeaturePermissionScheme.getFeaturePermissionMapping())).get(0));
			roleFeaturePermissionSchemeModel.setRoleFeaturePermissionSchemeId(roleFeaturePermissionScheme.getRoleFeaturePermissionSchemeId());
			roleFeaturePermissionSchemeModel.setSchemeName(roleFeaturePermissionScheme.getSchemeName());
			roleFeaturePermissionSchemeModel.setUpdatedBy(roleFeaturePermissionScheme.getUpdatedBy());
//			roleFeaturePermissionSchemeModel.setUpdatedDate(roleFeaturePermissionScheme.getUpdatedDate());
			roleFeaturePermissionSchemeModel.setRole(toRoleModel(roleFeaturePermissionScheme.getRole()));
			roleFeaturePermissionSchemeModel.setUserRoleFeaturePermissionMappings(toUserRoleFeaturePermissionMappingModelValueObjs(
					roleFeaturePermissionScheme.getUserRoleFeaturePermissionMappings()));
			roleFeaturePermissionSchemeModels.add(roleFeaturePermissionSchemeModel);
		}
		return roleFeaturePermissionSchemeModels;
	}
	
	public static List<ValueObject> toUserRoleFeaturePermissionMappingModelValueObjs(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings){
		List<ValueObject> valueObjects = new ArrayList<>();
		for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
			ValueObject valueObject = new ValueObject(Integer.toString(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId()),
					userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme().getRole().getRoleName());
			valueObjects.add(valueObject);
		}
		return valueObjects;
	}
	
	public static UserRoleFeaturePermissionMappingModel toUserRoleFeaturePermissionMappingModel(
			UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping){
		UserRoleFeaturePermissionMappingModel userRoleFeaturePermissionMappingModel = new UserRoleFeaturePermissionMappingModel();
		userRoleFeaturePermissionMappingModel.setRoleFeaturePermissionSchemeModel(toRoleFeaturePermissionSchemeModels(
				Arrays.asList(userRoleFeaturePermissionMapping.getRoleFeaturePermissionScheme())).get(0));
		userRoleFeaturePermissionMappingModel.setUpdatedBy(userRoleFeaturePermissionMapping.getUpdatedBy());
//		userRoleFeaturePermissionMappingModel.setUpdatedDate(userRoleFeaturePermissionMapping.getUpdatedDate());
		userRoleFeaturePermissionMappingModel.setUserDetailModel(toUserDetailModelValueObjs(
				userRoleFeaturePermissionMapping.getUserDetail()));
		userRoleFeaturePermissionMappingModel.setUserRoleFeaturePermissionId(userRoleFeaturePermissionMapping.getUserRoleFeaturePermissionId());
		
		return userRoleFeaturePermissionMappingModel;
	}

	public static List<UserRoleFeaturePermissionMappingModel> toUserRoleFeaturePermissionMappingModels(
			List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings) {
		List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappingModels = new ArrayList<>();
		for (UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping : userRoleFeaturePermissionMappings) {
			UserRoleFeaturePermissionMappingModel userRoleFeaturePermissionMappingModel = toUserRoleFeaturePermissionMappingModel(
					userRoleFeaturePermissionMapping);
			userRoleFeaturePermissionMappingModels.add(userRoleFeaturePermissionMappingModel);
		}
		return userRoleFeaturePermissionMappingModels;
	}
			
}
