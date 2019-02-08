package org.sdrc.cysdrf.reposiotry;

import java.sql.Timestamp;

import org.sdrc.cysdrf.domain.UserLoginMeta;

public interface UserLoginMetaRepository {

	UserLoginMeta save(UserLoginMeta userLoginMeta);
	
	void updateStatus(Timestamp loggedOutDateTime, long userLogInMetaId);
}
