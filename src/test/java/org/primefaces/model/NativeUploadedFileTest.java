package org.primefaces.model;

import javax.faces.FacesException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NativeUploadedFileTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    NativeUploadedFile file = new NativeUploadedFile();

    @Test
    public void testValid1() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename=\"hello.png\"";

        // Act
        final String output = file.getContentDispositionFileName(input);

        // Assert
        Assert.assertEquals("hello.png", output);
    }

    @Test
    public void testValid2() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename=\"Test;123.txt\"";

        // Act
        final String output = file.getContentDispositionFileName(input);

        // Assert
        Assert.assertEquals("Test;123.txt", output);
    }

    @Test
    public void testValid3() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename=\"Test; \\\"123.txt\"";

        // Act
        final String output = file.getContentDispositionFileName(input);

        // Assert
        Assert.assertEquals("Test; \"123.txt", output);
    }

    @Test
    public void testValid4() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename=\"Test;123.txt\"; charset=\"UTF-8\"";

        // Act
        final String output = file.getContentDispositionFileName(input);

        // Assert
        Assert.assertEquals("Test;123.txt", output);
    }

    @Test
    public void testNoFilename() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; file=\"hello.png\"";

        // Act
        final String output = file.getContentDispositionFileName(input);

        // Assert
        Assert.assertNull(output);
    }

    @Test
    public void testNoEquals() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename\"hello.png\"";

        thrown.expect(FacesException.class);
        thrown.expectMessage("Content-Disposition filename property did not have '='.");

        // Act
        file.getContentDispositionFileName(input);

        // Assert (expected exception)
    }

    @Test
    public void testNoStartQuote() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename=hello.png\"";
        thrown.expect(FacesException.class);
        thrown.expectMessage("Content-Disposition filename property was not quoted.");

        // Act
        file.getContentDispositionFileName(input);

        // Assert (expected exception)
    }

    @Test
    public void testUnknownEscape() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename=\"Test; \\hello.png\"";
        thrown.expect(FacesException.class);
        thrown.expectMessage("Content-Disposition filename has unknown escape character: 'h'.");

        // Act
        file.getContentDispositionFileName(input);

        // Assert (expected exception)
    }

    @Test
    public void testNoEndQuote() {
        // Arrange
        final String input = "form-data; name=\"XXX:XXX\"; filename=\"hello.png";
        thrown.expect(FacesException.class);
        thrown.expectMessage("Content-Disposition filename has unclosed quote.");

        // Act
        file.getContentDispositionFileName(input);

        // Assert (expected exception)
    }

}
