package eu.qped.umr.checkers;


import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class StatementsVisitor {
    private int whileCounter = 0;
    private int forEachCounter = 0;
    private int forCounter = 0;
    private int doCounter = 0;
    private int ifElseCounter = 0;


    private String source;
    private BlockStmt methodBody;

    public StatementsVisitor(String source) {
        this.source = source;

    }

    public StatementsVisitor(BlockStmt methodBody) {
        this.methodBody = methodBody;
        execute();
    }


    public String getSource() {
        return source;
    }

    public int getIfElseCounter() {
        return ifElseCounter;
    }

    public int getWhileCounter() {
        return whileCounter;
    }

    public int getForEachCounter() {
        return forEachCounter;
    }

    public int getForCounter() {
        return forCounter;
    }

    public int getDoCounter() {
        return doCounter;
    }

    static class State {
        final Statement statement;
        // The states that follow this state:
        final List<State> nextStates = new ArrayList<>();

        State(Statement statement) {
            this.statement = statement;
        }
    }

    private void findStates(Statement block, State beforeState, State afterState) {
        List<State> states = new ArrayList<>();
        // Create the sequence in this block
        for (Statement statement : block.asBlockStmt().getStatements()) {
            states.add(new State(statement));
        }
        if (states.size()>0){
            beforeState.nextStates.add(states.get(0));
        }


        // Attach to next states
        for (int i = 0; i < states.size(); i++) {
            State currentState = states.get(i);

            State nextState;
            if (i == states.size() - 1) {
                nextState = afterState;
            } else {
                nextState = states.get(i + 1);
            }

            currentState.statement.accept(new VoidVisitorWithDefaults<Void>() {
                @Override
                public void visit(ForEachStmt n, Void arg) {
                    currentState.nextStates.add(nextState);
                    System.out.println("forEach found");
                    forEachCounter++;
                    if (n.getBody().isBlockStmt()){
                        findStates(n.getBody(), currentState, nextState);

                    }
                    else if (n.getBody().isForEachStmt()){
                        System.out.println("forEach found");
                        forEachCounter++;
                        for (int j = 0; j <n.getChildNodes().size() ; j++) {
                            Node temp = n.getChildNodes().get(j);
                            temp.accept(new VoidVisitorWithDefaults<Void>() {
                                @Override
                                public void visit(ForEachStmt n, Void arg) {
                                    System.out.println("forEach found");
                                    forEachCounter++;
                                }
                            },null);
                        }
                    }
                }

                @Override
                public void visit(ForStmt n, Void arg) {
                    currentState.nextStates.add(nextState);
                    System.out.println("for found");
                    forCounter++;
                    if (n.getBody().isBlockStmt()){
                        findStates(n.getBody(), currentState, nextState);
                    }
                    else if (n.getBody().isForStmt()){
                        System.out.println("for found");
                        forCounter++;
                        for (int j = 0; j <n.getChildNodes().size() ; j++) {
                            Node temp = n.getChildNodes().get(j);
                            temp.accept(new VoidVisitorWithDefaults<Void>() {
                                @Override
                                public void visit(ForStmt n, Void arg) {
                                    System.out.println("for found");
                                    forCounter++;
                                }
                            },null);
                        }
                    }
                }

                @Override
                public void visit(WhileStmt n, Void arg) {
                    currentState.nextStates.add(nextState);
                    System.out.println("while found");
                    whileCounter++;
                    if (n.getBody().isBlockStmt()){
                        findStates(n.getBody(), currentState, nextState);
                    }
                    else if (n.getBody().isWhileStmt()){
                        System.out.println("while for");
                        whileCounter++;
                        for (int j = 0; j <n.getChildNodes().size() ; j++) {
                            Node temp = n.getChildNodes().get(j);
                            temp.accept(new VoidVisitorWithDefaults<Void>() {
                                @Override
                                public void visit(WhileStmt n, Void arg) {
                                    System.out.println("while found");
                                    whileCounter++;
                                }
                            },null);
                        }
                    }
                }

                @Override
                public void visit(DoStmt n, Void arg) {
                    currentState.nextStates.add(nextState);
                    System.out.println("do found");
                    doCounter++;
                    if (n.getBody().isBlockStmt()){
                        findStates(n.getBody(), currentState, nextState);
                    }
                    else if (n.getBody().isDoStmt()){
                        System.out.println("found for");
                        doCounter++;
                        for (int j = 0; j <n.getChildNodes().size() ; j++) {
                            Node temp = n.getChildNodes().get(j);
                            temp.accept(new VoidVisitorWithDefaults<Void>() {
                                @Override
                                public void visit(DoStmt n, Void arg) {
                                    System.out.println("do found");
                                    doCounter++;
                                }
                            },null);
                        }
                    }
                }
                @Override
                public void visit(IfStmt n, Void arg) {
                    ifElseCounter++;
                    System.out.println("if found");
                    if (n.getThenStmt().isBlockStmt()){
                        for (int k = 0; k < n.getThenStmt().getChildNodes().size() ; k++) {
                            Node temp = n.getThenStmt().getChildNodes().get(k);
                            temp.accept(new VoidVisitorWithDefaults<Void>() {
                                @Override
                                public void visit(IfStmt n, Void arg) {
                                    ifElseCounter++;
                                    System.out.println("if found");
                                }
                            } , null);
                        }
                    }
                    if (n.getElseStmt().isPresent() && n.getElseStmt().get().isBlockStmt()){
                        for (int j = 0; j <n.getElseStmt().get().asBlockStmt().getChildNodes().size() ; j++) {
                            Node temp = n.getElseStmt().get().asBlockStmt().getChildNodes().get(j);
                            temp.accept(new VoidVisitorWithDefaults<Void>() {
                                @Override
                                public void visit(IfStmt n, Void arg) {
                                    ifElseCounter++;
                                    System.out.println("if found");
                                }
                            } , null);
                        }
                    }
                }
            }, null);
        }
    }

    private void execute() {
        State start = new State(null);
        State end = new State(null);
        this.findStates(methodBody, start, end);
    }
}
