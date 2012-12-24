package com.noodlesandwich.trycatch;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static com.noodlesandwich.trycatch.TryCatch.tryDoing;

public final class TryCatchTest {
    private PrintStream originalOut;
    private OutputStream out = new ByteArrayOutputStream();

    @Before public void
    mock_output() {
        originalOut = System.out;
        System.setOut(new PrintStream(out));
    }

    @After public void
    restore_output() {
        System.setOut(originalOut);
    }

    @Test public void
    tries_the_thing() {
        tryDoing(() -> { System.out.println("Hello!"); });
        assertThat(output(), is("Hello!"));
    }

    private String output() {
        return out.toString().replaceFirst("\n$", "");
    }
}
