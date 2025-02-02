/*
 * Copyright 2016-2022 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junitpioneer.testkit;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junitpioneer.testkit.assertion.PioneerAssert.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Execute")
class PioneerTestKitTests {

	@Test
	@DisplayName("all tests of a class")
	void executeTestClass() {
		ExecutionResults results = PioneerTestKit.executeTestClass(DummyClass.class);

		assertThat(results).hasNumberOfStartedTests(1);
	}

	@Test
	@DisplayName("all tests of all given classes")
	void executeTestClasses() {
		ExecutionResults results = PioneerTestKit.executeTestClasses(asList(DummyClass.class, SecondDummyClass.class));

		assertThat(results).hasNumberOfStartedTests(2);
	}

	@Test
	@DisplayName("a specific method")
	void executeTestMethod() {
		ExecutionResults results = PioneerTestKit.executeTestMethod(DummyClass.class, "nothing");

		assertThat(results).hasNumberOfStartedTests(1);
	}

	@Nested
	@DisplayName("a specific parametrized method")
	class ExecuteTestMethodWithParametersTests {

		@Test
		@DisplayName(" where parameter is a single class")
		void executeTestMethodWithParameterTypes_singleParameterType() {
			ExecutionResults results = PioneerTestKit
					.executeTestMethodWithParameterTypes(DummyPropertyClass.class, "single", String.class);

			assertThat(results).hasNumberOfStartedTests(1);
		}

		@Test
		@DisplayName(" where parameter is an array of classes")
		void executeTestMethodWithParameterTypes_parameterTypeAsArray() {
			Class<?>[] classes = { String.class };

			ExecutionResults results = PioneerTestKit
					.executeTestMethodWithParameterTypes(DummyPropertyClass.class, "single", classes);

			assertThat(results).hasNumberOfStartedTests(1);
		}

		@Test
		@DisplayName("without parameter results in IllegalArgumentException")
		void executeTestMethodWithParameterTypes_parameterArrayIsNull_NullPointerException() {
			assertThatThrownBy(() -> PioneerTestKit
					.executeTestMethodWithParameterTypes(DummyPropertyClass.class, "single", (Class<?>) null))
							.isInstanceOf(NullPointerException.class);
		}

		@Test
		@DisplayName("without parameter results in IllegalArgumentException")
		void executeTestMethodWithParameterTypes_singleParameterIsNull_IllegalArgumentException() {
			assertThatThrownBy(() -> PioneerTestKit
					.executeTestMethodWithParameterTypes(DummyPropertyClass.class, "single", (Class<?>[]) null))
							.isInstanceOf(IllegalArgumentException.class)
							.hasMessage("methodParameterTypes must not be null");
		}

	}

	static class DummyPropertyClass {

		@ParameterizedTest(name = "See if enabled with {0}")
		@ValueSource(strings = { "parameter" })
		void single(String reason) {
			// Do nothing
		}

	}

	static class DummyClass {

		@Test
		void nothing() {
			// Do nothing
		}

	}

	static class SecondDummyClass {

		@Test
		void nothing() {
			// Do nothing
		}

	}

}
