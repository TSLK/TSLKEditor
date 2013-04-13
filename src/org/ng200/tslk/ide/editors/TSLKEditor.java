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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.TextEditor;

public class TSLKEditor extends TextEditor {

	private ColorManager colorManager;

	private IEditorInput input;

	public TSLKEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new TSLKConfiguration(colorManager));
		setDocumentProvider(new TSLKDocumentProvider());
	}

	@Override
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	@Override
	protected void doSetInput(IEditorInput newInput) throws CoreException {
		super.doSetInput(newInput);
		this.input = newInput;
		validateAndMark();
	}

	@Override
	protected void editorSaved() {
		super.editorSaved();
		validateAndMark();

	}

	public IEditorInput getInput() {
		return input;
	}

	protected IDocument getInputDocument() {
		IDocument document = getDocumentProvider().getDocument(input);
		return document;
	}

	protected IFile getInputFile() {
		IFileEditorInput ife = (IFileEditorInput) input;
		IFile file = ife.getFile();
		return file;
	}

	protected void validateAndMark() {
		try {
			IDocument document = getInputDocument();
			String text = document.get();
			ErrorMarker markingErrorHandler = new ErrorMarker(getInputFile(),
					document);
			markingErrorHandler.removeExistingMarkers();
			TSLKEditorParser parser = new TSLKEditorParser();
			parser.setErrorHandler(markingErrorHandler);
			parser.doParse(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
