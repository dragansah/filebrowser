package com.dragansah.filebrowser.services;

import java.io.File;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.services.ApplicationStateManager;

import com.dragansah.filebrowser.sessionstate.UserInfo;

public interface FileService
{
	/**
	 * Protect the given {@link File} from security attacks on the client side by stripping the root
	 * folder path of the user folder (in the file system) from {@link File#getAbsolutePath()}.
	 * <p>
	 * Uses the {@link UserInfo} {@link SessionState} object.
	 * 
	 * @param value
	 *            The file that needs to be encoded i.e. stripped.
	 * 
	 * @return The stripped {@link File#getAbsolutePath()}
	 * 
	 * @see FileBrowserModule#reconstructProtectedFileFromPath(ApplicationStateManager, String)
	 */
	String protectFile(File value);

	/**
	 * Reconstruct the protected {@link File} by adding the root folder path of the user folder (in
	 * the file system) to the path obtained from the client side.
	 * <p>
	 * Uses the {@link UserInfo} {@link SessionState} object.
	 * 
	 * @param path
	 *            The file path obtained from the client side
	 * 
	 * @return The reconstructed {@link File}
	 * 
	 * @see FileBrowserModule#protectFile(ApplicationStateManager, File)
	 */
	File reconstructProtectedFileFromPath(String path);

	Link createDownloadLinkFromFile(File file);

	File createFileFromDownloadLink(String filePath) throws IllegalAccessException;

	Object createStreamResponseFromFile(final File file);
}
