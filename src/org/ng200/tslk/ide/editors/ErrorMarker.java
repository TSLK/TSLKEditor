/*******************************************************************************
 * Copyright (c) 2013 Nick Guletskii.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Nick Guletskii - initial API and implementation
 ******************************************************************************/
package org.ng200.tslk.ide.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;

public class ErrorMarker {

	public static final String ERROR_MARKER_ID = "TSLKEditor.syntaxerror";

	private IFile file;

	public ErrorMarker(IFile file, IDocument document) {
		super();
		this.file = file;
	}

	public void putError(int line, int charPositionInLine, String msg,
			int charStart, int charEnd) throws CoreException {
		IMarker marker = file.createMarker(ERROR_MARKER_ID);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
		marker.setAttribute(IMarker.MESSAGE, msg);
		marker.setAttribute(IMarker.CHAR_START, charStart);
		marker.setAttribute(IMarker.CHAR_END, charEnd + 1);
		marker.setAttribute(IMarker.LOCATION, file.getFullPath().toString());
	}

	public void removeExistingMarkers() {
		try {
			file.deleteMarkers(ERROR_MARKER_ID, true, IResource.DEPTH_ZERO);
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

}
