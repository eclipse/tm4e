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
package org.eclipse.tm4e.core.internal.grammar.reader;

import java.io.InputStream;

import org.eclipse.tm4e.core.internal.grammar.RawRule;
import org.eclipse.tm4e.core.internal.grammar.RawCaptures;
import org.eclipse.tm4e.core.internal.grammar.RawGrammar;
import org.eclipse.tm4e.core.internal.grammar.RawRepository;
import org.eclipse.tm4e.core.internal.parser.PListParser;
import org.eclipse.tm4e.core.internal.parser.PListParserJSON;
import org.eclipse.tm4e.core.internal.parser.PListParserXML;
import org.eclipse.tm4e.core.internal.parser.PListParserYAML;
import org.eclipse.tm4e.core.internal.parser.PListPath;
import org.eclipse.tm4e.core.internal.parser.PropertySettable;
import org.eclipse.tm4e.core.internal.types.IRawGrammar;

/**
 * TextMate Grammar reader utilities.
 */
public final class GrammarReader {

	public static final PropertySettable.Factory<PListPath> OBJECT_FACTORY = path -> {
		if (path.size() == 0) {
			return new RawGrammar();
		}
		switch (path.last()) {
		case RawRule.REPOSITORY:
			return new RawRepository();
		case RawRule.BEGIN_CAPTURES:
		case RawRule.CAPTURES:
		case RawRule.END_CAPTURES:
		case RawRule.WHILE_CAPTURES:
			return new RawCaptures();
		}
		return new RawRule();
	};

	private static final PListParser<RawGrammar> JSON_PARSER = new PListParserJSON<>(OBJECT_FACTORY);
	private static final PListParser<RawGrammar> XML_PARSER = new PListParserXML<>(OBJECT_FACTORY);
	private static final PListParser<RawGrammar> YAML_PARSER = new PListParserYAML<>(OBJECT_FACTORY);

	public static IRawGrammar readGrammarSync(final String filePath, final InputStream in) throws Exception {
		return getGrammarParser(filePath).parse(in);
	}

	private static PListParser<RawGrammar> getGrammarParser(final String filePath) {
		String extension = filePath.substring(filePath.lastIndexOf('.') + 1).trim().toLowerCase();

		switch (extension) {

		case "json":
			return JSON_PARSER;

		case "yaml":
		case "yaml-tmlanguage":
		case "yml":
			return YAML_PARSER;

		default:
			return XML_PARSER;
		}
	}

	/**
	 * methods should be accessed statically
	 */
	private GrammarReader() {
	}
}
