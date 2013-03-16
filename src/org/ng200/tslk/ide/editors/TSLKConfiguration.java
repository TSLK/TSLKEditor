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

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class TSLKConfiguration extends SourceViewerConfiguration {
	private TSLKDoubleClickStrategy doubleClickStrategy;
	private TSLKScanner scanner;
	private ColorManager colorManager;

	public TSLKConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return TSLKPartitionScanner.PARTITION_TYPES;
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new TSLKDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected TSLKScanner getTSLKScanner() {
		if (scanner == null) {
			scanner = new TSLKScanner(colorManager);
			scanner.setDefaultReturnToken(new Token(new TextAttribute(
					colorManager.getColor(ColorManager.DEFAULT))));
		}
		return scanner;
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getTSLKScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer string = new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager.getColor(ColorManager.STRING)));
		reconciler.setDamager(string, TSLKPartitionScanner.TSLK_STRING);
		reconciler.setRepairer(string, TSLKPartitionScanner.TSLK_STRING);

		NonRuleBasedDamagerRepairer multiLineComment = new NonRuleBasedDamagerRepairer(
				new TextAttribute(
						colorManager.getColor(ColorManager.MULTI_LINE_COMMENT)));
		reconciler.setDamager(multiLineComment,
				TSLKPartitionScanner.TSLK_ML_COMMENT);
		reconciler.setRepairer(multiLineComment,
				TSLKPartitionScanner.TSLK_ML_COMMENT);

		NonRuleBasedDamagerRepairer singleLineComment = new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager
						.getColor(ColorManager.SINGLE_LINE_COMMENT)));
		reconciler.setDamager(singleLineComment,
				TSLKPartitionScanner.TSLK_SL_COMMENT);
		reconciler.setRepairer(singleLineComment,
				TSLKPartitionScanner.TSLK_SL_COMMENT);

		return reconciler;
	}

}
