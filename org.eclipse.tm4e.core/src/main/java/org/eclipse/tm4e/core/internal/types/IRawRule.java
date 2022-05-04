/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Initial code from https://github.com/Microsoft/vscode-textmate/
 * Initial copyright Copyright (C) Microsoft Corporation. All rights reserved.
 * Initial license: MIT
 *
 * Contributors:
 * - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 * - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.core.internal.types;

import java.util.Collection;

import org.eclipse.jdt.annotation.Nullable;

public interface IRawRule {

	@Nullable
	Integer getId();

	void setId(Integer id);

	@Nullable
	String getInclude();

	@Nullable
	String getName();

	@Nullable
	String getContentName();

	@Nullable
	String getMatch();

	@Nullable
	IRawCaptures getCaptures();

	@Nullable
	String getBegin();

	@Nullable
	IRawCaptures getBeginCaptures();

	@Nullable
	String getEnd();

	@Nullable
	String getWhile();

	@Nullable
	IRawCaptures getEndCaptures();

	@Nullable
	IRawCaptures getWhileCaptures();

	@Nullable
	Collection<IRawRule> getPatterns();

	@Nullable
	IRawRepository getRepository();

	boolean isApplyEndPatternLast();
}
