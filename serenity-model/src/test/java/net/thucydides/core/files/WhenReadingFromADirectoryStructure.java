package net.thucydides.core.files;

import net.serenitybdd.core.CurrentOS;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

public class WhenReadingFromADirectoryStructure {
    private static final File singleLevelFolder = new File("src/test/resources/sample-features/flat-directory-structure");
    private static final File singleLevelFolderWithWindowsPathSeparators = new File("src\\test\\resources\\sample-features\\flat-directory-structure");
    private static final File multiLevelFolder = new File("src/test/resources/sample-features/multiple-levels");
    private static final File multiLevelFolderWithWindowsPathSeparators = new File("src\\test\\resources\\sample-features\\multiple-levels");

    @Test
    public void shouldReadTheDirectoryDepthOfAFeatureFolder() {
        assertThat(TheDirectoryStructure.startingAt(singleLevelFolder).maxDepth()).isEqualTo(0);
    }

    @Test
    public void shouldReadTheDirectoryDepthOfAFeatureFolderForAWindowsPath() {
        if(CurrentOS.isWindows()) {
            assertThat(TheDirectoryStructure.startingAt(singleLevelFolderWithWindowsPathSeparators).maxDepth()).isEqualTo(0);
        }
    }

    @Test
    public void shouldReadTheDirectoryDepthOfAMultiLevelFeatureFolder() {
        assertThat(TheDirectoryStructure.startingAt(multiLevelFolder).maxDepth()).isEqualTo(2);
    }

    @Test
    public void shouldReadTheDirectoryDepthOfAMultiLevelFeatureFolderForAWindowsPath() {
        assertThat(TheDirectoryStructure.startingAt(multiLevelFolderWithWindowsPathSeparators).maxDepth()).isEqualTo(2);
    }
}
