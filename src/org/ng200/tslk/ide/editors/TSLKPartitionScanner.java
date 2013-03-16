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

import org.eclipse.jface.text.rules.*;

public class TSLKPartitionScanner extends RuleBasedPartitionScanner {
	public final static String TSLK_ML_COMMENT = "__tslk_ml_comment";
	public final static String TSLK_SL_COMMENT = "__tslk_sl_comment";
	public final static String TSLK_STRING = "__tslk_string";

	public final static String[] PARTITION_TYPES = new String[] {
			TSLK_ML_COMMENT, TSLK_SL_COMMENT, TSLK_STRING };

	public TSLKPartitionScanner() {

		IToken tslkMultilineComment = new Token(TSLK_ML_COMMENT);
		IToken tslkSinglelineComment = new Token(TSLK_SL_COMMENT);
		IToken tslkString = new Token(TSLK_STRING);

		ArrayList<IPredicateRule> rules = new ArrayList<IPredicateRule>();

		rules.add(new MultiLineRule("/*", "*/", tslkMultilineComment));
		rules.add(new SingleLineRule("\"", "\"", tslkString, '\\'));
		rules.add(new EndOfLineRule("//", tslkSinglelineComment));
		IPredicateRule[] result = new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
