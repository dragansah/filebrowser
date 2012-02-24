package com.dragansah.filebrowser.sessionstate;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

public class UserInfo
{
	private final String username;

	public UserInfo(@Inject RequestGlobals requestGlobals)
	{
		if (requestGlobals != null)
			if (requestGlobals.getHTTPServletRequest().getRemoteUser() != null)
				username = requestGlobals.getHTTPServletRequest().getRemoteUser();
	}

	public String getUsername()
	{
		return username;
	}
}
