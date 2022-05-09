/**
 * Copyright (c) 2022 Sebastian Thomschke and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.tm4e.core.benchmarks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.grammar.IStackElement;
import org.eclipse.tm4e.core.registry.Registry;

public final class GrammarBenchmark implements Runnable {

	private static final int WARMUP_ROUNDS = 3;
	private static final int BENCHMARK_ROUNDS = 3;
	private static final int OPS_PER_BENCHMARK_ROUND = 50;

	public static void main(final String... args) throws Exception {
		BenchmarkRunner.run(WARMUP_ROUNDS, BENCHMARK_ROUNDS, OPS_PER_BENCHMARK_ROUND, new GrammarBenchmark());
	}

	final IGrammar grammar;
	final String[] sourceCode;

	GrammarBenchmark() throws Exception {
		/*
		 * load the grammar
		 */
		try (var grammarFileIS = GrammarBenchmark.class.getResourceAsStream("/GrammarBenchmark.Java.tmLanguage.json")) {
			grammar = new Registry().loadGrammarFromPathSync("/GrammarBenchmark.Java.tmLanguage.json", grammarFileIS);
		}

		/*
		 * load the file to be parsed
		 */
		try (var sourceFileIS = GrammarBenchmark.class.getResourceAsStream("/GrammarBenchmark.JavaFile.txt")) {
			sourceCode = new BufferedReader(new InputStreamReader(sourceFileIS, StandardCharsets.UTF_8))
					.lines()
					.toArray(String[]::new);
		}
		System.out.println(
				String.format("Source Code chars: %,d", Arrays.stream(sourceCode).mapToInt(String::length).sum()));
		System.out.println(String.format("Source Code lines: %,d", sourceCode.length));
	}

	/**
	 * Tokenize all lines of the pre-loaded source file
	 */
	@Override
	public void run() {
		IStackElement state = null;
		for (final var line : sourceCode) {
			state = grammar.tokenizeLine(line, state).getRuleStack();
		}
	}
}