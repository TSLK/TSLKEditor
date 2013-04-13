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

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.RGB;
import org.ng200.tslk.lang.TSLKGrammarLexer;

public class TSLKRepairer implements IPresentationDamager,
		IPresentationRepairer {

	private ColorManager colorManager;

	protected IDocument document;

	public TSLKRepairer(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public void createPresentation(TextPresentation presentation,
			ITypedRegion region) {
		// Use tokens provided by the lexer to highlight keywords, etc...
		// Seems fast enough to skip Eclipse partitioning. Infact, the Eclipse
		// partitioner seems to slow everything down...
		TSLKGrammarLexer lexer = new TSLKGrammarLexer(new ANTLRInputStream(
				document.get()));
		Token t = null;
		while ((t = lexer.nextToken()).getType() != Token.EOF) {
			if (t.getStartIndex() > region.getOffset() + region.getLength())
				break;
			int start = t.getStartIndex();
			int end = t.getStopIndex();
			RGB foreground = null;
			RGB background = null;
			int style = SWT.NORMAL;
			switch (t.getType()) { // TODO: Make keywords customisable
			case TSLKGrammarLexer.WHILE:
			case TSLKGrammarLexer.FOR:
			case TSLKGrammarLexer.FUNC:
			case TSLKGrammarLexer.IF:
			case TSLKGrammarLexer.THEN:
			case TSLKGrammarLexer.DO:
			case TSLKGrammarLexer.END:
				foreground = ColorManager.KEYWORD;
				style = SWT.BOLD;
				break;
			case TSLKGrammarLexer.STRING:
				foreground = ColorManager.STRING;
				break;
			case TSLKGrammarLexer.SLCOMMENT:
				foreground = ColorManager.SINGLE_LINE_COMMENT;
				break;
			case TSLKGrammarLexer.MLCOMMENT:
				foreground = ColorManager.MULTI_LINE_COMMENT;
				break;
			default:
				foreground = ColorManager.DEFAULT;
				break;
			}
			presentation.addStyleRange(new StyleRange(start, end - start + 1,
					colorManager.getColor(foreground), colorManager
							.getColor(background), style));
		}
	}

	protected int endOfLineOf(int offset) throws BadLocationException {

		IRegion info = document.getLineInformationOfOffset(offset);
		if (offset <= info.getOffset() + info.getLength())
			return info.getOffset() + info.getLength();

		int line = document.getLineOfOffset(offset);
		try {
			info = document.getLineInformation(line + 1);
			return info.getOffset() + info.getLength();
		} catch (BadLocationException x) {
			return document.getLength();
		}
	}

	@Override
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent event,
			boolean documentPartitioningChanged) {
		if (!documentPartitioningChanged) {
			try {

				IRegion info = document.getLineInformationOfOffset(event
						.getOffset());
				int start = Math.max(partition.getOffset(), info.getOffset());

				int end = event.getOffset()
						+ (event.getText() == null ? event.getLength() : event
								.getText().length());

				if (info.getOffset() <= end
						&& end <= info.getOffset() + info.getLength()) {
					// optimize the case of the same line
					end = info.getOffset() + info.getLength();
				} else
					end = endOfLineOf(end);

				end = Math.min(partition.getOffset() + partition.getLength(),
						end);
				return new Region(start, end - start);

			} catch (BadLocationException x) {
			}
		}

		return partition;
	}

	@Override
	public void setDocument(IDocument document) {
		this.document = document;
	}

}
