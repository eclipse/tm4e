/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.registry;

/**
 * TextMate grammar definition API.
 *
 */
public interface IGrammarDefinition extends ITMResource {

	/**
	 * Returns the scope name of the TextMate grammar.
	 * 
	 * @return the scope name of the TextMate grammar.
	 */
	String getScopeName();
}
