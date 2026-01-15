import lombok.SneakyThrows;
import org.Main;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

///  Not exhaustive suite of tests for mappings utility
public class RunWithKeysTest {

    @Test
    void runItWithEmptyArgsTest() {
        try {
            Main.main(new String[0]);
        } catch (Exception exception) {
            assertThat(exception.getMessage())
                    .contains("Cannot run with empty arguments list, usage:");
        }
    }

    @Test
    void projectKeyIsMissingTest() {
        try {
            Main.main(new String[]{"--mappings=/Users/alex/Downloads/migration_reports/ЕСМ/reports/SFPM.json"});
        } catch (Exception exception) {
            assertThat(exception.getMessage())
                    .contains("--project key was not found in arguments list");
        }
    }

    @Test
    void projectKeyNotEmptyTest() {
        try {
            Main.main(new String[]{"--project", "--mappings=/Users/alex/Downloads/migration_reports/ЕСМ/reports/SFPM.json"});
        } catch (Exception exception) {
            assertThat(exception.getMessage())
                    .contains("Failed to split string into key&value pair, usage:");
        }
    }

    @Test
    void mappingsKeyIsMissingTest() {
        try {
            Main.main(new String[]{"--project=/a/b/c"});
        } catch (Exception exception) {
            assertThat(exception.getMessage())
                    .contains("--mappings key was not found in arguments list, usage:");
        }
    }

    @Test
    void mappingsKeyNotEmptyTest() {
        try {
            Main.main(new String[]{"--project=/a/b/c", "--mappings"});
        } catch (Exception exception) {
            assertThat(exception.getMessage())
                    .contains("Failed to split string into key&value pair, usage:");
        }
    }

    @Test
    @SneakyThrows
    void runItProperlyDefined() {
        Main.main(new String[]{"--project=/Users/user/IdeaProjects/project_name_copy", "--mappings=/Users/user/Downloads/migration_reports/mappings.json"});
    }
}
