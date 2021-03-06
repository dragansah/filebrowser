// Copyright 2012 Dragan Sahpaski
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.dragansah.filebrowser.sessionstate;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import com.dragansah.filebrowser.Constants;

public class UserInfo
{
	private final String username;

	public UserInfo(@Inject RequestGlobals requestGlobals)
	{
		username = requestGlobals.getRequest().getHeader("CAS-User");
	}

	public String getUsername()
	{
		return username;
	}

	public String getRootFolderForLoggedInUser()
	{
		return Constants.ROOT_USERS_FOLDER + "/" + username;
	}
}
