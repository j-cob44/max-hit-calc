/* DataSourceTest.java
 * Holds the line that every test says where its expected values came from.
 *
 * Copyright (c) 2026, Jacob Burton <https://github.com/j-cob44>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.maxhitcalc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Enforces the {@link DataSource} contract: no test may assert a number without saying what that
 * number was checked against, so an expected value can never quietly come from reading our own
 * calculator.
 * <p>
 * This is a lint rather than a calculation test. It reads the test sources off disk instead of
 * using reflection, so a test file added tomorrow is covered the moment it is saved, with nothing
 * to register. The cost is that it depends on the working directory, which is why
 * {@link #theSourcesAreWhereThisExpects()} and {@link #theLintCanSeeTheTests()} exist. Without
 * those two a parsing mistake would make the whole check pass by inspecting nothing, which is
 * exactly how an earlier version of this managed to enforce nothing at all.
 * <p>
 * Only {@link DataSource.Derived} may sit on a class, because "nothing here comes from the game"
 * is true of a whole file by construction. A wiki page justifies one particular value, so it goes
 * on the test that asserts it, naming the page that value was read from. Otherwise a test added
 * later would silently inherit a citation nobody chose for it.
 */
class DataSourceTest
{
    private static final Path TEST_SOURCES = Paths.get("src", "test", "java", "com", "maxhitcalc");

    /** Every test file should hold at least this many tests between them, or the lint saw nothing */
    private static final int FEWEST_TESTS_EXPECTED = 100;

    /** An annotation stack and the declaration it sits on */
    private static final class Annotated
    {
        private final String annotations;
        private final String declaration;

        private Annotated(String annotations, String declaration)
        {
            this.annotations = annotations;
            this.declaration = declaration;
        }

        private boolean isTest()
        {
            return annotations.contains("@Test") || annotations.contains("@ParameterizedTest");
        }

        private boolean isSourced()
        {
            return annotations.contains("@DataSource.");
        }

        private boolean isDerived()
        {
            return annotations.contains("@DataSource.Derived");
        }

        private boolean declares(String className)
        {
            return declaration.contains("class " + className);
        }
    }

    private static List<Path> testSources() throws IOException
    {
        try (Stream<Path> files = Files.list(TEST_SOURCES))
        {
            return files
                .filter(p -> p.getFileName().toString().endsWith("Test.java"))
                .sorted()
                .collect(Collectors.toList());
        }
    }

    /**
     * Brackets opened minus brackets closed. Counting these is what lets an annotation wrap over
     * several lines, since only its first line starts with an @. String contents are counted too,
     * which is fine while no annotation here holds an unbalanced bracket in a literal.
     */
    private static int bracketDepth(String line)
    {
        int depth = 0;

        for (char c : line.toCharArray())
        {
            if (c == '(' || c == '{') depth++;
            else if (c == ')' || c == '}') depth--;
        }

        return depth;
    }

    /** Pairs each annotation stack in a file with whatever it is annotating */
    private static List<Annotated> annotated(List<String> lines)
    {
        List<Annotated> found = new ArrayList<>();
        StringBuilder stack = new StringBuilder();
        int depth = 0;

        for (String line : lines)
        {
            String trimmed = line.trim();

            // mid way through an annotation that wrapped onto more lines
            if (depth > 0)
            {
                stack.append(trimmed).append(' ');
                depth += bracketDepth(trimmed);
                continue;
            }

            if (trimmed.startsWith("@"))
            {
                stack.append(trimmed).append(' ');
                depth += bracketDepth(trimmed);
                continue;
            }

            if (stack.length() > 0)
            {
                found.add(new Annotated(stack.toString(), trimmed));
                stack.setLength(0);
            }
        }

        return found;
    }

    @Test
    @DisplayName("Every test names what its expected values are checked against")
    void everyTestNamesItsSource() throws IOException
    {
        List<String> unmarked = new ArrayList<>();

        for (Path source : testSources())
        {
            String className = source.getFileName().toString().replace(".java", "");
            if (className.equals("DataSourceTest")) continue;

            List<Annotated> members = annotated(Files.readAllLines(source, StandardCharsets.UTF_8));

            boolean wholeFileDerived = members.stream()
                .anyMatch(m -> m.declares(className) && m.isDerived());

            for (Annotated member : members)
            {
                if (!member.isTest() || wholeFileDerived || member.isSourced()) continue;

                unmarked.add(className + ": " + member.declaration);
            }
        }

        assertTrue(unmarked.isEmpty(),
            "These tests do not name what their expected values are checked against:\n  "
                + String.join("\n  ", unmarked));
    }

    @Test
    @DisplayName("Only a derived source may cover a whole class")
    void onlyDerivedCoversAWholeClass() throws IOException
    {
        List<String> overreaching = new ArrayList<>();

        for (Path source : testSources())
        {
            String className = source.getFileName().toString().replace(".java", "");

            for (Annotated member : annotated(Files.readAllLines(source, StandardCharsets.UTF_8)))
            {
                if (member.declares(className) && member.isSourced() && !member.isDerived())
                {
                    overreaching.add(className);
                }
            }
        }

        assertTrue(overreaching.isEmpty(),
            "A wiki page or an in game reading justifies one value, so it belongs on the test that\n"
                + "asserts it rather than on the whole class:\n  "
                + String.join("\n  ", overreaching));
    }

    @Test
    @DisplayName("The test sources are where this expects them")
    void theSourcesAreWhereThisExpects() throws IOException
    {
        assertTrue(Files.isDirectory(TEST_SOURCES), "not found from " + Paths.get("").toAbsolutePath());
        assertTrue(testSources().size() > 10, "only found " + testSources().size() + " test files");
    }

    @Test
    @DisplayName("The lint can see the tests it is meant to be checking")
    void theLintCanSeeTheTests() throws IOException
    {
        int tests = 0;

        for (Path source : testSources())
        {
            for (Annotated member : annotated(Files.readAllLines(source, StandardCharsets.UTF_8)))
            {
                if (member.isTest()) tests++;
            }
        }

        assertTrue(tests >= FEWEST_TESTS_EXPECTED,
            "only parsed " + tests + " tests, so the parser is not seeing the suite");
    }
}
