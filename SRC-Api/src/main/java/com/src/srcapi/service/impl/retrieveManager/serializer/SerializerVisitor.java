package com.src.srcapi.service.impl.retrieveManager.serializer;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
// 自定义AST序列化访问者
public class SerializerVisitor extends GenericListVisitorAdapter<String, Void> {

    @Override
    public List<String> visit(CompilationUnit n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 33)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(PackageDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 34)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(TypeParameter n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 35)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ClassOrInterfaceDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 36)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(RecordDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 37)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(CompactConstructorDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 38)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(EnumDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 39)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(EnumConstantDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 40)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(AnnotationDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 41)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(AnnotationMemberDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 42)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(FieldDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 43)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(VariableDeclarator n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 44)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ConstructorDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 45)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(MethodDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 46)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(Parameter n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 47)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(InitializerDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 48)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ClassOrInterfaceType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 49)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(PrimitiveType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 50)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ArrayType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 51)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ArrayCreationLevel n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 52)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(IntersectionType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 53)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(UnionType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 54)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(VoidType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 55)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(WildcardType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 56)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(UnknownType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 57)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ArrayAccessExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 58)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ArrayCreationExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 59)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ArrayInitializerExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 60)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(AssignExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 61)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(BinaryExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 62)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(CastExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 63)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ClassExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 64)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ConditionalExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 65)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(EnclosedExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 66)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(FieldAccessExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 67)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(InstanceOfExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 68)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(StringLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 69)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(IntegerLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 70)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(LongLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 71)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(CharLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 72)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(DoubleLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 73)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(BooleanLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 74)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(NullLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 75)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(MethodCallExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 76)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(NameExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 77)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ObjectCreationExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 78)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ThisExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 79)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(SuperExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 80)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(UnaryExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 81)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(VariableDeclarationExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 82)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(MarkerAnnotationExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 83)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(SingleMemberAnnotationExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 84)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(NormalAnnotationExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 85)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(MemberValuePair n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 86)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ExplicitConstructorInvocationStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 87))));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(LocalClassDeclarationStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 88)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(LocalRecordDeclarationStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 89)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(AssertStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 90)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(BlockStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 91)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(LabeledStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 92)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(EmptyStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 93)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ExpressionStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 94)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(SwitchStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 95)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(SwitchEntry n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 96)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(BreakStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 97)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ReturnStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 98)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(IfStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 99)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(WhileStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 100)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ContinueStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 101)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(DoStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 102)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ForEachStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 103)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ForStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 104)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ThrowStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 105)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(SynchronizedStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 106)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(TryStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 107)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(CatchClause n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 108)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(LambdaExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 109)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(MethodReferenceExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 110)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(TypeExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 111)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(NodeList n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 112)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(Name n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 113)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(SimpleName n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 114)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ImportDeclaration n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 115)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(UnparsableStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 116)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(ReceiverParameter n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 117)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(VarType n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 118)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(Modifier n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 119)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(SwitchExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 120)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(YieldStmt n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 121)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(TextBlockLiteralExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 122)));
        s.addAll(super.visit(n, arg));
        return s;
    }

    @Override
    public List<String> visit(PatternExpr n, Void arg) {
        List<String> s = new java.util.ArrayList<>(Collections.singletonList(String.valueOf((char) 123)));
        s.addAll(super.visit(n, arg));
        return s;
    }
}
