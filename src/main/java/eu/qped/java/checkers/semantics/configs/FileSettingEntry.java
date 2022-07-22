package eu.qped.java.checkers.semantics.configs;

import eu.qped.java.checkers.mass.SemanticSettingItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class FileSettingEntry {

    private String filePath;

    private List<SemanticSettingItem> settingItems;

}
