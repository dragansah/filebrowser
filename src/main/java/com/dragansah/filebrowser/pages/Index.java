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
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

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

	private String rootFolderForLoggedInUser;

	@SuppressWarnings("unused")
	@Property
	@Path("../icons/folder.png")
	@Inject
	private Asset folderIcon;

	void setupRender()
	{
		rootFolderForLoggedInUser = "/students/dragan.sahpaski";
		rootDirectories = new ArrayList<File>();

		rootDirectories.add(new File(rootFolderForLoggedInUser, "private"));
		rootDirectories.add(new File(rootFolderForLoggedInUser, "finki"));
		rootDirectories.add(new File(rootFolderForLoggedInUser, "public"));
	}

	public String getLiClass()
	{
		if (selectedTopDirectory == null)
			return "";

		return topDirectory.getAbsolutePath().equals(selectedTopDirectory.getAbsolutePath()) ? "selected"
				: "";
	}

	void onSelectDirectory(File selectedTopDirectory) throws IOException
	{
		this.selectedTopDirectory = selectedTopDirectory;

		if (!selectedTopDirectory.exists())
			selectedTopDirectory.mkdir();

		currentDirectory = selectedTopDirectory;
	}
}
