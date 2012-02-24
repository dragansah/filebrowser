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

package com.dragansah.filebrowser.pages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;

import com.dragansah.filebrowser.Constants;
import com.dragansah.filebrowser.services.FileService;
import com.dragansah.filebrowser.sessionstate.UserInfo;

public class Index
{
	@Property
	@Persist
	private File currentDirectory;

	@Property
	private File topDirectory;

	@Property
	@Persist
	private File selectedTopDirectory;

	@Property
	private List<File> rootDirectories;

	@SuppressWarnings("unused")
	@Property
	@Path("../icons/folder.png")
	@Inject
	private Asset folderIcon;

	@SessionState
	private UserInfo userInfo;

	@PageActivationContext
	private String filePathForDownload;

	@Inject
	private FileService fileService;

	void setupRender() throws IOException, IllegalAccessException
	{
		File userFolder = new File(userInfo.getRootFolderForLoggedInUser());
		if (!userFolder.exists())
			userFolder.mkdir();

		rootDirectories = new ArrayList<File>();

		rootDirectories.add(new File(userFolder, Constants.PRIVATE_FOLDER_NAME));
		rootDirectories.add(new File(userFolder, Constants.FINKI_FOLDER_NAME));
		rootDirectories.add(new File(userFolder, Constants.PUBLIC_FOLDER_NAME));

		if (currentDirectory == null)
			selectDirectory(rootDirectories.get(0));
	}

	Object onActivate() throws IllegalAccessException
	{
		if (filePathForDownload != null)
			return fileService.createStreamResponseFromFile(fileService
					.createFileFromDownloadLink(filePathForDownload));

		return true;
	}

	public String getLiClass()
	{
		if (selectedTopDirectory == null)
			return "";

		return topDirectory.getAbsolutePath().equals(selectedTopDirectory.getAbsolutePath()) ? "selected"
				: "";
	}

	@OnEvent("selectDirectory")
	void selectDirectory(File selectedTopDirectory) throws IOException
	{
		this.selectedTopDirectory = selectedTopDirectory;

		if (!selectedTopDirectory.exists())
			selectedTopDirectory.mkdir();

		currentDirectory = selectedTopDirectory;
	}

	public boolean isShowLinkColumn()
	{
		String[] split = currentDirectory.getAbsolutePath().split("/");
		if (split.length < 4)
			return false;

		return split[3].equals(Constants.FINKI_FOLDER_NAME);
	}
}
