package no.uio.aeroscript.runtime;

import no.uio.aeroscript.antlr.AeroScriptBaseVisitor;
import no.uio.aeroscript.antlr.AeroScriptParser;
import no.uio.aeroscript.error.TypeError;

public class TypeChecker extends AeroScriptBaseVisitor<Object>  {
    
    enum Type {
        NUM,
        POINT
    }

    public void check(AeroScriptParser.ProgramContext ctx) {
        visitProgram(ctx);
    }
    
    @Override
    public Object visitProgram(AeroScriptParser.ProgramContext ctx) {
        
        for (AeroScriptParser.ExecutionContext exeCtx: ctx.execution() ) {
            visit(exeCtx);
        }
        return null;
    }
    
    @Override
    public Object visitExecution(AeroScriptParser.ExecutionContext ctx) {
        
        for (AeroScriptParser.StatementContext stmCtx: ctx.statement()) {
            visit(stmCtx);
        }
        return null;
    }
    
    @Override
    public Object visitStatement(AeroScriptParser.StatementContext ctx) {
        
        if (ctx.action() != null) {
            return visit(ctx.action());
        }
        else if(ctx.execution() != null) {
            return visit(ctx.execution());
        }
        else if(ctx.reaction() != null) {
            return visit(ctx.reaction());
        }
        else {
            throw new IllegalArgumentException("invalid operator: " + ctx.getText());
        }
    }
    
    @Override
    public Object visitAction(AeroScriptParser.ActionContext ctx) {
        
        if (ctx.SPEED() != null || ctx.FOR() != null) {
            Type speedTimeExpression = (Type) visit(ctx.expression());
            if (speedTimeExpression != Type.NUM){
                throw new TypeError("Expression is not a number: " + ctx.getText());
            }
        }
        
        if (ctx.acAscend() != null) {
            Type ascendExpression = (Type) visit(ctx.acAscend().expression());
            if (ascendExpression != Type.NUM){
                throw new TypeError("Expression is not a number: " + ctx.getText());
            } 
        }

        else if (ctx.acDescend() != null) {
            return null;
        }

        else if (ctx.acDock() != null) {
            return null;
        }
        
        else if (ctx.acMove() != null) {
            
            if(ctx.acMove().POINT() != null) {
                Type pointMove = (Type) visit(ctx.acMove().point());
                if (pointMove != Type.POINT){
                    throw new TypeError("Not a point grrrr: " + ctx.getText());
                }
            }
            if(ctx.acMove().expression() != null) {
                Type numberMove = (Type) visit(ctx.acMove().expression());
                if (numberMove != Type.NUM){
                    throw new TypeError("Not a number grrrr: " + ctx.getText());
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public Object visitReaction(AeroScriptParser.ReactionContext ctx) {
        return null;
    }
    
    @Override
    public Type visitPoint(AeroScriptParser.PointContext ctx) {
        Type xNode = (Type) visit(ctx.expression(0));
        Type yNode = (Type) visit(ctx.expression(1));
        if (xNode != Type.NUM){
            throw new TypeError("Type is not number: " + ctx.expression(0).getText());
        }
        if (yNode != Type.NUM){
            throw new TypeError("Type is not number: " + ctx.expression(1).getText());  
        }
        return Type.POINT;
    }
    
    @Override
    public Object visitRange(AeroScriptParser.RangeContext ctx) {
        Type startNode = (Type) visit(ctx.expression(0));
        Type endNode = (Type) visit(ctx.expression(1));
        if (startNode != Type.NUM){
            throw new TypeError("Type is not number: " + ctx.expression(0).getText());
        }
        if (endNode != Type.NUM){
            throw new TypeError("Type is not number: " + ctx.expression(1).getText());
        }
        return Type.NUM;
    }
    
    @Override
    public Type visitExpression(AeroScriptParser.ExpressionContext ctx) {

        if (ctx.PLUS() != null || ctx.MINUS() != null || ctx.TIMES() != null) {
            Type left = (Type) visit(ctx.expression(0));
            Type right = (Type) visit(ctx.expression(1));
            if (left != Type.NUM){
                throw new TypeError("Type is not number: " + ctx.expression(0).getText());
            }
            if (right != Type.NUM){
                throw new TypeError("Type is not number: " + ctx.expression(1).getText());
            }
        }
        else if (ctx.NEG() != null) {
            Type expr = (Type) visit(ctx.expression(0));
            if (expr != Type.NUM){
                throw new TypeError("Type is not number: " + ctx.expression(1).getText());
            }
        }
        else if (ctx.RANDOM() != null) {
            
            Type start;
            Type end;

            if (ctx.expression(0).range() != null) {
                AeroScriptParser.RangeContext rangeContext = ctx.expression(0).range();
                start = (Type) visit(rangeContext.expression(0));
                end = (Type) visit(rangeContext.expression(1));
                if (start != Type.NUM){
                    throw new TypeError("Type is not number: " + ctx.expression(0).range().expression(0).getText());
                }
                if (end != Type.NUM){
                    throw new TypeError("Type is not number: " + ctx.expression(0).range().expression(1).getText());
                }
            } 
        }
        else if (ctx.POINT() != null) {
            return Type.POINT;
        }
        else if (ctx.NUMBER() != null) {
            return Type.NUM;
        }
        else if (ctx.LPAREN() != null) {
            return (Type) visit(ctx.expression(0));
        }
        throw new IllegalArgumentException("Invalid operator: " + ctx.getText());
    }  
}

