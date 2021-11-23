package eu.qped.umr.chekcers.semanticChecker.helpers;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


import java.util.ArrayList;

public class RecursiveCheckHelper {


    private final BlockStmt methodBody;
    private final ArrayList<String> calledMethods;
    private final String methodName;

    public RecursiveCheckHelper(BlockStmt methodBody , String methodName) {
        this.methodBody = methodBody;
        this.calledMethods = new ArrayList<>();
        this.methodName = methodName;
        walk();
    }


    public boolean check(){
        return calledMethods.contains(methodName);
    }

    private void walk(){
        if (methodBody.getChildNodes() != null){
            methodBody.accept(new VoidVisitorAdapter<Void>() {

                @Override
                public void visit(MethodCallExpr n, Void arg){
                    super.visit(n, arg);
                    calledMethods.add(n.getName().toString());
                }
            } , null);
        }
    }
}
