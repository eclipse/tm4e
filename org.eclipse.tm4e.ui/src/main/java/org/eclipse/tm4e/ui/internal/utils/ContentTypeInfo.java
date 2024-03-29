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
package org.eclipse.tm4e.ui.internal.utils;

import org.eclipse.core.runtime.content.IContentType;

/**
 * @author azerr
 */
public final class ContentTypeInfo {

	private final String fileName;
	private final IContentType[] contentTypes;

	public ContentTypeInfo(final String fileName, final IContentType[] contentTypes) {
		this.fileName = fileName;
		this.contentTypes = contentTypes;
	}

	public String getFileName() {
		return fileName;
	}

	public IContentType[] getContentTypes() {
		return contentTypes;
	}
}
