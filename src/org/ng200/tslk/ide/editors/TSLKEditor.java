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

import org.eclipse.ui.editors.text.TextEditor;

public class TSLKEditor extends TextEditor {

	private ColorManager colorManager;

	public TSLKEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new TSLKConfiguration(colorManager));
		setDocumentProvider(new TSLKDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
