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

package com.dragansah.filebrowser.domain;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.tapestry5.beaneditor.NonVisual;

public class FileModel
{
	private final boolean folder;
	private final String absolutePath;
	private final String fileName;
	private final Long fileSizeBytes;
	private final Date lastModified;
	private final boolean pointerToPrevDirectory;

	private FileModel(String absolutePath, String fileName, Long fileSizeBytes, boolean folder,
			Date lastModified)
	{
		this.fileName = fileName;
		this.fileSizeBytes = fileSizeBytes;
		this.lastModified = lastModified;
		this.folder = folder;
		this.absolutePath = absolutePath;
		this.pointerToPrevDirectory = false;
	}

	private FileModel(String absolutePath, String fileName, Long fileSizeBytes, boolean folder,
			Date lastModified, boolean pointerToPrevDirectory)
	{
		this.fileName = fileName;
		this.fileSizeBytes = fileSizeBytes;
		this.lastModified = lastModified;
		this.folder = folder;
		this.absolutePath = absolutePath;
		this.pointerToPrevDirectory = pointerToPrevDirectory;
	}

	@NonVisual
	public String getAbsolutePath()
	{
		return absolutePath;
	}

	public String getFileName()
	{
		return fileName;
	}

	public Long getFileSizeBytes()
	{
		return fileSizeBytes;
	}

	public Date getLastModified()
	{
		return lastModified;
	}

	@NonVisual
	public boolean isFolder()
	{
		return folder;
	}

	public String getExtension()
	{
		String[] split = fileName.split("\\.");

		if (split.length <= 1)
			return null;

		return split[split.length - 1];

	}

	@NonVisual
	public boolean isPointerToPrevDirectory()
	{
		return pointerToPrevDirectory;
	}

	@NonVisual
	public String getFileSizePrettyPrint()
	{
		if (fileSizeBytes == null)
			return "";

		if (fileSizeBytes < 1024)
			return fileSizeBytes + " B";

		if (fileSizeBytes < 1024 * 1024)
			return new DecimalFormat("###.#").format(((double) fileSizeBytes / 1024)) + " KB";

		return new DecimalFormat("######.#").format(((double) fileSizeBytes / 1024 / 1024)) + " MB";
	}

	public static FileModel fromFile(File f)
	{
		return new FileModel(f.getAbsolutePath(), f.getName(), f.length(), f.isDirectory(), null);
	}

	public static FileModel getPreviousDirectory(FileModel f)
	{
		return getPreviousDirectory(new File(f.getAbsolutePath()));
	}

	public static FileModel getPreviousDirectory(File f)
	{
		if (f.getParentFile() == null)
			return null;

		return new FileModel(f.getParentFile().getAbsolutePath(), "..", null, true, null, true);
	}
}
