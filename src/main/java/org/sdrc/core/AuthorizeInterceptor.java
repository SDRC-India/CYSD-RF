package org.sdrc.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.cysdrf.model.FeaturePermissionMappingModel;
import org.sdrc.cysdrf.model.UserDetailModel;
import org.sdrc.cysdrf.util.Constants;
import org.sdrc.cysdrf.util.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {

	private final StateManager stateManager;
	private final ResourceBundleMessageSource messageSourceNotification;

	@Autowired
	public AuthorizeInterceptor(StateManager stateManager,
			ResourceBundleMessageSource messageSourceNotification) {
		this.stateManager = stateManager;
		this.messageSourceNotification = messageSourceNotification;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) {
		
		if (!(handler instanceof HandlerMethod))
			return true;

		Authorize authorize = ((HandlerMethod) handler)
				.getMethodAnnotation(Authorize.class);

		if (authorize == null)
			return true;

		UserDetailModel user = (UserDetailModel) stateManager.getValue(Constants.USER_PRINCIPAL);
		if (user == null)
			throw new AccessDeniedException(messageSourceNotification.getMessage(
					Constants.ACCESS_DENIED, null, null));
		
		String feature = authorize.feature();
		String permission = authorize.permission();
		
		if (user != null && user.getUserRoleFeaturePermissionMappings() != null) {
			for (int i = 0; i < user.getUserRoleFeaturePermissionMappings().size(); i++) {
				FeaturePermissionMappingModel fpMapping = user
						.getUserRoleFeaturePermissionMappings().get(i).getRoleFeaturePermissionSchemeModel().getFeaturePermissionMapping();
				if (feature.equals(fpMapping.getFeature().getFeatureName())
						&& permission.equals(fpMapping.getPermission()
								.getPermissionName())) {
					return true;
				}
			}
		}
		throw new AccessDeniedException(messageSourceNotification.getMessage(
				Constants.ACCESS_DENIED, null, null));
	}

}
