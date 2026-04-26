import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class DumpTomcatSessionsTest {

    @Test
    void rejectsZeroArgs() {
        Result r = invoke();
        assertEquals(1, r.exit);
        assertTrue(r.err.contains("Usage:"), () -> "expected usage on stderr, got: " + r.err);
        assertEquals("", r.out);
    }

    @Test
    void rejectsOneArg() {
        Result r = invoke("host");
        assertEquals(1, r.exit);
        assertTrue(r.err.contains("Usage:"));
    }

    @Test
    void rejectsThreeArgs() {
        // Three args used to silently drop the credentials; now it must be rejected.
        Result r = invoke("host", "1099", "user");
        assertEquals(1, r.exit);
        assertTrue(r.err.contains("Usage:"));
    }

    @Test
    void rejectsFiveArgs() {
        Result r = invoke("host", "1099", "user", "pass", "extra");
        assertEquals(1, r.exit);
        assertTrue(r.err.contains("Usage:"));
    }

    @Test
    void twoArgsAttemptConnectionAndFails() {
        // Port 1 will not have a JMX server listening; we expect a non-zero exit
        // distinct from the usage-error code (1).
        Result r = invoke("127.0.0.1", "1");
        assertEquals(2, r.exit);
        assertNotEquals("", r.err, "expected stack trace on stderr");
    }

    @Test
    void fourArgsAttemptConnectionAndFails() {
        Result r = invoke("127.0.0.1", "1", "user", "pass");
        assertEquals(2, r.exit);
        assertNotEquals("", r.err);
    }

    private static Result invoke(String... args) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int exit = DumpTomcatSessions.run(
                args,
                new PrintStream(out, true, StandardCharsets.UTF_8),
                new PrintStream(err, true, StandardCharsets.UTF_8));
        return new Result(exit,
                out.toString(StandardCharsets.UTF_8),
                err.toString(StandardCharsets.UTF_8));
    }

    private record Result(int exit, String out, String err) {}
}
