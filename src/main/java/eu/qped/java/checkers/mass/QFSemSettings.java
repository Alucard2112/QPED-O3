package eu.qped.java.checkers.mass;

import eu.qped.framework.qf.QfObjectBase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QFSemSettings extends QfObjectBase {

    private List<SemanticSettingItem> semantics;

}
