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
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.upload.services.UploadedFile;

import com.dragansah.filebrowser.domain.FileModel;

@Import(stylesheet = "file-browser.css")
public class FileBrowser
{

	@Property
	@Parameter(required = true, allowNull = false)
	private File currentDirectory;

	@Property
	@Parameter(required = true, allowNull = false)
	private File topDirectory;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectComponent
	private Zone mainZone;

	@Property
	private boolean creatingDirectory;

	@Property
	private int gridRowIndex;

	@SuppressWarnings("unused")
	@Path("../icons/delete.png")
	@Inject
	@Property
	private Asset deleteIcon;

	@Property
	private String newDirectoryName;

	Object onCreateDirectory()
	{
		creatingDirectory = true;

		return mainZone.getBody();
	}

	public boolean isNewDirectory()
	{
		return creatingDirectory == true && gridRowIndex == 1;
	}

	private boolean saveNewDirectory;

	void onSelectedFromSaveNewDirectory()
	{
		saveNewDirectory = true;
	}

	void onSuccessFromNewDirectoryForm() throws IOException
	{
		if (!saveNewDirectory)
			return;

		new File(currentDirectory, newDirectoryName).mkdir();
	}

	@SuppressWarnings("unused")
	@Property
	private FileModel file;

	public List<FileModel> getFiles()
	{
		List<FileModel> files = new ArrayList<FileModel>();

		File[] subDirs = currentDirectory.listFiles((FileFilter) FileFilterUtils
				.directoryFileFilter());

		if (!topDirectory.getPath().equals(currentDirectory.getPath()))
			files.add(FileModel.getPreviousDirectory(currentDirectory));

		for (File f : subDirs)
			files.add(FileModel.fromFile(f));

		for (File f : FileUtils.listFiles(currentDirectory, null, false))
		{
			files.add(FileModel.fromFile(f));
			// FileSystemView view = FileSystemView.getFileSystemView();
		}

		return files;
	}

	Object onBrowseFolder(FileModel file)
	{
		currentDirectory = new File(file.getAbsolutePath());
		return mainZone.getBody();
	}

	Object onDownloadFile(final FileModel file)
	{
		return new StreamResponse()
		{

			@Override
			public void prepareResponse(Response response)
			{
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + file.getFileName() + "\"");
			}

			@Override
			public InputStream getStream() throws IOException
			{
				return FileUtils.openInputStream(new File(file.getAbsolutePath()));
			}

			@Override
			public String getContentType()
			{
				return new MimetypesFileTypeMap().getContentType(new File(file.getAbsolutePath()));
			}
		};
	}

	void onAjaxFileUploadFromUploadFile(UploadedFile uploadedFile)
	{
		uploadedFile.write(new File(currentDirectory, uploadedFile.getFileName()));

		ajaxResponseRenderer.addRender("mainZone", mainZone);
	}

	@SuppressWarnings("unused")
	@Property
	private File fileForDelete;

	@Inject
	private Block deleteBlock;

	Object onActionFromDeleteFileLink(File file)
	{
		fileForDelete = file;

		return deleteBlock;
	}

	private boolean deleteFile;

	void onSelectedFromDeleteFile()
	{
		deleteFile = true;
	}

	void onSuccessFromDeleteFileForm(File fileForDelete)
	{
		if (!deleteFile)
			return;

		FileUtils.deleteQuietly(fileForDelete);
	}

}
