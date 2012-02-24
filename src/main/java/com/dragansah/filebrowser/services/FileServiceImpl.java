package com.dragansah.filebrowser.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.Response;

import com.dragansah.filebrowser.Constants;
import com.dragansah.filebrowser.pages.Index;
import com.dragansah.filebrowser.sessionstate.UserInfo;

public class FileServiceImpl implements FileService
{
	@Inject
	private ApplicationStateManager asm;

	/**
	 * Protect the given {@link File} from security attacks on the client side by stripping the root
	 * folder path of the user folder (in the file system) from {@link File#getAbsolutePath()}.
	 * <p>
	 * The {@link ApplicationStateManager} is needed for obtaining the {@link UserInfo}
	 * {@link SessionState} object.
	 * 
	 * @param asm
	 * 
	 * @param value
	 *            The file that needs to be encoded i.e. stripped.
	 * 
	 * @return The stripped {@link File#getAbsolutePath()}
	 * 
	 * @see FileBrowserModule#reconstructProtectedFileFromPath(ApplicationStateManager, String)
	 */
	public String protectFile(File value)
	{
		String rootFolder = getUserInfo().getRootFolderForLoggedInUser();
		return value.getAbsolutePath().replace(rootFolder, "");
	}

	/**
	 * Reconstruct the protected {@link File} by adding the root folder path of the user folder (in
	 * the file system) to the path obtained from the client side.
	 * <p>
	 * The {@link ApplicationStateManager} is needed for obtaining the {@link UserInfo}
	 * {@link SessionState} object.
	 * 
	 * @param asm
	 * 
	 * @param path
	 *            The file path obtained from the client side
	 * 
	 * @return The reconstructed {@link File}
	 * 
	 * @see FileBrowserModule#protectFile(ApplicationStateManager, File)
	 */
	public File reconstructProtectedFileFromPath(String path)
	{
		String rootFolder = getUserInfo().getRootFolderForLoggedInUser();
		return new File(rootFolder + path);
	}

	private UserInfo getUserInfo()
	{
		return asm.get(UserInfo.class);
	}

	@Inject
	private PageRenderLinkSource linkSource;

	@Override
	public Link createDownloadLinkFromFile(File file)
	{
		return linkSource.createPageRenderLinkWithContext(Index.class,
				String.format("/%s%s", getUserInfo().getUsername(), protectFile(file)));
	}

	@Override
	public File createFileFromDownloadLink(String filePath) throws IllegalAccessException
	{
		String[] split = filePath.split("/");

		if (!split[2].equals(Constants.FINKI_FOLDER_NAME))
			throw new IllegalAccessException(
					"You don't have the permissions to view the given folder.");

		return new File(Constants.ROOT_USERS_FOLDER + filePath);
	}

	@Override
	public Object createStreamResponseFromFile(final File file)
	{
		return new StreamResponse()
		{
			@Override
			public void prepareResponse(Response response)
			{
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + file.getName() + "\"");
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
}
