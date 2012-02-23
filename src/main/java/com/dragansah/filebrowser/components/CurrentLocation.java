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

package com.dragansah.filebrowser.components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class CurrentLocation
{
	@Parameter
	private File topDirectory;

	@Parameter
	private File currentDirectory;

	@Property
	private List<File> directories;

	@SuppressWarnings("unused")
	@Property
	private File directory;

	void setupRender()
	{
		directories = new ArrayList<File>();

		File parent = currentDirectory;
		directories.add(parent);
		while ((parent = parent.getParentFile()) != null
				&& !parent.getAbsolutePath().equals(topDirectory.getParent()))
			directories.add(0, parent);
	}

	public void onChangeDirectory(File directory)
	{
		currentDirectory = directory;
	}
}
