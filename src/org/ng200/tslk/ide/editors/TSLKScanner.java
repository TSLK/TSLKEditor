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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class TSLKScanner extends RuleBasedScanner {
	public static String[] keywords = { "while", "for", "if", "else", "then",
			"do", "end", "function", "table", "break", "continue", "local",
			"true", "false" };

	public TSLKScanner(ColorManager provider) {

		IToken keyword = new Token(new TextAttribute(
				provider.getColor(ColorManager.KEYWORD), null, SWT.BOLD));
		IToken string = new Token(new TextAttribute(
				provider.getColor(ColorManager.STRING)));
		IToken singleLineComment = new Token(new TextAttribute(
				provider.getColor(ColorManager.SINGLE_LINE_COMMENT)));
		IToken multiLineComment = new Token(new TextAttribute(
				provider.getColor(ColorManager.SINGLE_LINE_COMMENT)));
		IToken other = new Token(new TextAttribute(
				provider.getColor(ColorManager.DEFAULT)));

		List<Object> rules = new ArrayList<Object>();

		rules.add(new EndOfLineRule("//", singleLineComment));
		rules.add(new MultiLineRule("/*", "*/", multiLineComment));
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));

		rules.add(new WhitespaceRule(new WhitespaceDetector()));

		WordRule wordRule = new WordRule(new IWordDetector() {
			@Override
			public boolean isWordStart(char arg0) {
				return Character.isUnicodeIdentifierStart(arg0);
			}

			@Override
			public boolean isWordPart(char arg0) {
				return Character.isUnicodeIdentifierPart(arg0);
			}
		}, other);
		for (String kw : keywords)
			wordRule.addWord(kw, keyword);
		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}
