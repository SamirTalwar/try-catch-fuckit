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
    private OutputStream out;

    private PrintStream originalErr;
    private OutputStream err;

    @Before public void
    mock_output() {
        originalOut = System.out;
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        originalErr = System.err;
        err = new ByteArrayOutputStream();
        System.setErr(new PrintStream(err));
    }

    @After public void
    restore_output() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test public void
    tries_the_thing() throws Throwable {
        tryDoing(() -> { System.out.println("Hello!"); })
            .run();
        assertThat(output(), is("Hello!"));
    }

    @Test public void
    catches_an_exception() throws Throwable {
        tryDoing(() -> { throw new WeirdAndWonderfulException("It broke."); })
                .catching(WeirdAndWonderfulException.class, (e) -> { System.err.println(e.getMessage()); })
                .run();
        assertThat(error(), is("It broke."));
    }

    @Test(expected=AnotherException.class) public void
    throws_any_uncaught_exceptions() throws Throwable {
        tryDoing(() -> { throw new AnotherException(); })
                .catching(WeirdAndWonderfulException.class, (e) -> { System.err.println(e.getMessage()); })
                .run();
    }

    private String output() {
        return out.toString().replaceFirst("\n$", "");
    }

    private String error() {
        return err.toString().replaceFirst("\n$", "");
    }

    public static final class WeirdAndWonderfulException extends Exception {
        public WeirdAndWonderfulException(String message) {
            super(message);
        }
    }

    private static class AnotherException extends Exception { }
}
