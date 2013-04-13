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

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;

public class TSLKPartitionScanner extends RuleBasedPartitionScanner {

	public final static String[] PARTITION_TYPES = new String[] {};

	public TSLKPartitionScanner() {
		IPredicateRule[] result = new IPredicateRule[0];
		setPredicateRules(result);
	}
}
