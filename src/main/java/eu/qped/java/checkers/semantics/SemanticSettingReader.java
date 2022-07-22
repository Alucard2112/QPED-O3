package eu.qped.java.checkers.semantics;

import eu.qped.java.checkers.mass.QFSemSettings;
import eu.qped.java.checkers.mass.SemanticSettingItem;
import eu.qped.java.checkers.semantics.configs.FileSettingEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemanticSettingReader {

    private final static boolean REC_ALLOWED_DEFAULT = false;
    private final static int MAX_STMT_DEFAULT = -1;

    private QFSemSettings qfSemSettings;

    private void setDefault() {
        qfSemSettings.getSemantics().forEach(
                semanticSettingItem -> {
                    if (semanticSettingItem.getRecursive() == null) {
                        semanticSettingItem.setRecursive(REC_ALLOWED_DEFAULT);
                    }
                    if (semanticSettingItem.getWhileLoop() == null) {
                        semanticSettingItem.setWhileLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getDoWhileLoop() == null) {
                        semanticSettingItem.setDoWhileLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getForLoop() == null) {
                        semanticSettingItem.setForLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getForEachLoop() == null) {
                        semanticSettingItem.setForEachLoop(MAX_STMT_DEFAULT);
                    }
                    if (semanticSettingItem.getIfElseStmt() == null) {
                        semanticSettingItem.setIfElseStmt(MAX_STMT_DEFAULT);
                    }
                }
        );

    }

    public List<FileSettingEntry> groupByFileName() {
        setDefault();

        List<FileSettingEntry> result = new ArrayList<>();

        qfSemSettings.getSemantics()
                .stream()
                .collect(Collectors.groupingBy(SemanticSettingItem::getFilePath))
                .forEach((filePath, semanticSettingItems) ->
                        result.add(
                                FileSettingEntry.builder()
                                        .filePath(filePath)
                                        .settingItems(semanticSettingItems).build()
                        )
                );

        return result;
    }

}
