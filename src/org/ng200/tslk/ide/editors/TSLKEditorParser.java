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
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.eclipse.core.runtime.CoreException;
import org.ng200.tslk.lang.TSLKGrammarLexer;
import org.ng200.tslk.lang.TSLKGrammarParser;

public class TSLKEditorParser {

	private static class ErrorListener extends BaseErrorListener {

		private TSLKEditorParser tslkEditorParser;

		public ErrorListener(TSLKEditorParser tslkEditorParser) {
			this.tslkEditorParser = tslkEditorParser;
		}

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer,
				Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			try {
				tslkEditorParser.getMarkingErrorHandler().putError(line,
						charPositionInLine, msg,
						((CommonToken) offendingSymbol).getStartIndex(),
						((CommonToken) offendingSymbol).getStopIndex());
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}
	}

	private ErrorMarker markingErrorHandler;

	public void doParse(String text) {
		TSLKGrammarLexer lexer = new TSLKGrammarLexer(
				new ANTLRInputStream(text));
		TSLKGrammarParser parser = new TSLKGrammarParser(new CommonTokenStream(
				lexer));
		parser.addErrorListener(new ErrorListener(this));
		parser.body();
	}

	public ErrorMarker getMarkingErrorHandler() {
		return markingErrorHandler;
	}

	public void setErrorHandler(ErrorMarker markingErrorHandler) {
		this.markingErrorHandler = markingErrorHandler;
	}
}
