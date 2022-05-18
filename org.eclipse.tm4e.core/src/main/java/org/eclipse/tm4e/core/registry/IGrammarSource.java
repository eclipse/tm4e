/**
 * Copyright (c) 2022 Sebastian Thomschke and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.tm4e.core.registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jdt.annotation.Nullable;

public interface IGrammarSource {

	/**
	 * Supported grammar content types
	 */
	enum ContentType {
		JSON,
		YAML,
		XML
	}

	private static ContentType guessFileFormat(final String fileName) {
		final String extension = fileName.substring(fileName.lastIndexOf('.') + 1).trim().toLowerCase();

		return switch (extension) {
		case "json" -> ContentType.JSON;
		case "yaml", "yaml-tmlanguage", "yml" -> ContentType.YAML;
		case "plist", "tmlanguage", "xml" -> ContentType.XML;
		default -> throw new IllegalArgumentException("Unsupported file type: " + fileName);
		};
	}

	static IGrammarSource fromFile(final Path file) {
		return fromFile(file, null, null);
	}

	static IGrammarSource fromFile(final Path file, @Nullable final ContentType contentType,
		@Nullable final Charset charset) {

		final var filePath = file.toString();
		final var contentType1 = contentType == null ? guessFileFormat(filePath) : contentType;
		return new IGrammarSource() {
			@Override
			public Reader getReader() throws IOException {
				return Files.newBufferedReader(file, charset == null ? StandardCharsets.UTF_8 : charset);
			}

			@Override
			public String getFilePath() {
				return filePath;
			}

			@Override
			public ContentType getContentType() {
				return contentType1;
			}
		};
	}

	/**
	 * @throws IllegalArgumentException if the content type is unsupported or cannot be determined
	 */
	static IGrammarSource fromResource(final Class<?> clazz, final String resourceName) {
		return fromResource(clazz, resourceName, null, null);
	}

	/**
	 * @throws IllegalArgumentException if the content type is unsupported or cannot be determined
	 */
	static IGrammarSource fromResource(final Class<?> clazz, final String resourceName,
		@Nullable final ContentType contentType, @Nullable final Charset charset) {

		final var contentType1 = contentType == null ? guessFileFormat(resourceName) : contentType;
		return new IGrammarSource() {
			@Override
			public Reader getReader() throws IOException {
				return new BufferedReader(new InputStreamReader(
					clazz.getResourceAsStream(resourceName),
					charset == null ? StandardCharsets.UTF_8 : charset));
			}

			@Override
			public String getFilePath() {
				return resourceName;
			}

			@Override
			public ContentType getContentType() {
				return contentType1;
			}
		};
	}

	static IGrammarSource fromString(final ContentType contentType, final String content) {

		return new IGrammarSource() {
			@Override
			public Reader getReader() throws IOException {
				return new StringReader(content);
			}

			@Override
			public String getFilePath() {
				return "string." + contentType.name().toLowerCase();
			}

			@Override
			public ContentType getContentType() {
				return contentType;
			}
		};
	}

	default ContentType getContentType() {
		return guessFileFormat(getFilePath());
	}

	String getFilePath();

	Reader getReader() throws IOException;
}
