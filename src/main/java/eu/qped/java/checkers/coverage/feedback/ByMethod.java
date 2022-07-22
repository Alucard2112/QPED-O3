package eu.qped.java.checkers.coverage.feedback;

import eu.qped.java.checkers.coverage.CoverageCount;
import eu.qped.java.checkers.coverage.enums.StateOfCoverage;
import eu.qped.java.checkers.coverage.feedback.wanted.ProviderWF;
import eu.qped.java.checkers.coverage.framework.ast.AstMethod;
import eu.qped.java.checkers.coverage.framework.coverage.Coverage;
import eu.qped.java.checkers.coverage.framework.coverage.CoverageClass;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Is a part of a tree-structure with the root in the class {@link Summary}.
 * In this node all information related to a method is stored.
 * @author Herfurth
 * @version 1.0
 */
public class ByMethod implements Comparable<ByMethod> {
    final LinkedList<StmtFB> statementsFB = new LinkedList<>();
    public AstMethod content;
    public Coverage coverage;
    String contentString = " ";

    ByMethod(Node node) {
        insert(node);
    }

    void insert(Node node) {
        node.insert(this);
    }


    public int start() {
        return content.start();
    }

    public int end() {
        return content.end();
    }

    boolean hasContent() {
        return Objects.nonNull(content);
    }

    boolean hasCoverage() {
        return Objects.nonNull(coverage);
    }

    public StateOfCoverage state() {
        return coverage.state();
    }

    void analyze(ProviderWF provider, CoverageClass aClass) {
        if (state().equals(StateOfCoverage.FULL)) {
            statementsFB.clear();
            return;
        } else if (state().equals(StateOfCoverage.NOT)) {
            statementsFB.clear();
            StmtFB notCovered = new StmtFB(content);
            notCovered.createFeedback(provider);
            if (! notCovered.getBody().isBlank()) {
                statementsFB.add(notCovered);
            }
            return;
        }

        LinkedList<StmtFB> stack = new LinkedList<>(statementsFB);
        LinkedList<StmtFB> flatt = new LinkedList<>();
        Set<String> isDuplicated = new HashSet<>();

        StmtFB first;
        StateOfCoverage state;
        SKIP:
        while (! stack.isEmpty()) {
            first = stack.removeFirst();
            try {
                for (int i = first.start() + 1; i <= first.end(); i ++) {
                    state = aClass.byIndex(i);
                    if (Objects.nonNull(state) && (state.equals(StateOfCoverage.FULL) || state.equals(StateOfCoverage.PARTLY))) {
                        stack.addAll(0, first.statementsFB);
                        continue SKIP;
                    }
                }
                first.statementsFB.clear();
                first.createFeedback(provider);
                if (! isDuplicated.contains(first.getBody()) && ! first.getBody().isBlank()) {
                    flatt.add(first);
                    isDuplicated.add(first.getBody());
                }
            } catch (IndexOutOfBoundsException i) {
                continue SKIP;
            }
        }

        statementsFB.clear();
        statementsFB.addAll(flatt);

        StringBuilder builder = new StringBuilder();
        builder.append("");

        int i = content.start();
        for (String line : content.content()) {
            String head = "";

            switch (aClass.byIndex(i ++)) {
                case FULL:
                    head = "\n[O] ";
                    break;
                case NOT:
                    head = "\n[X] ";
                    break;
                case PARTLY:
                    head = "\n[P] ";
                    break;
                default:
                    head = "\n[_] ";
                    break;
            }
            builder.append(head).append(line).append("");
        }
        contentString =  builder.append("\n\n").toString();
    }

    public CoverageCount branch() {
        return coverage.branch();
    }

    public CoverageCount line() {
        return coverage.line();
    }

    public String content() {
        return contentString;
    }

    @Override
    public int compareTo(ByMethod byMethod) {
        return start() - byMethod.start();
    }

}
