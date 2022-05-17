/**
 * Copyright (c) 2022 Sebastian Thomschke and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Initial code from https://github.com/microsoft/vscode-textmate/
 * Initial copyright Copyright (C) Microsoft Corporation. All rights reserved.
 * Initial license: MIT
 *
 * Contributors:
 * - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 * - Sebastian Thomschke - translation and adaptation to Java
 */
package org.eclipse.tm4e.core.internal.grammar.tokenattrs;

/**
 * @see <a href=
 *      "https://github.com/microsoft/vscode-textmate/blob/e8d1fc5d04b2fc91384c7a895f6c9ff296a38ac8/src/encodedTokenAttributes.ts#L184">
 *      github.com/microsoft/vscode-textmate/blob/main/src/encodedTokenAttributes.ts</a>
 */
public final class OptionalStandardTokenType {

	/**
	 * Content should be accessed statically
	 */
	private OptionalStandardTokenType() {
	}

	public static final int Other = StandardTokenType.Other;
	public static final int Comment = StandardTokenType.Comment;
	public static final int String = StandardTokenType.String;
	public static final int RegEx = StandardTokenType.RegEx;

	/**
	 * Indicates that no token type is set.
	 */
	public static final int NotSet = 8;
}
