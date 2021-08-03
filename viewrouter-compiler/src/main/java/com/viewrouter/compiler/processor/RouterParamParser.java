package com.viewrouter.compiler.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import com.viewrouter.annotation.ViewRouteParam;
import com.viewrouter.compiler.utils.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * @author lxm
 * @createtime 2021/6/8
 */
class RouterParamParser {

    private final Map<ViewRouteParam, Element> mRouterParamsMap = new LinkedHashMap<>();
    private final String mPackageName;
    private final String mTargetClass;

    RouterParamParser(String packageName,
                      String targetClass) {
        mPackageName = packageName;
        mTargetClass = targetClass;
    }

    /**
     *
     */
    JavaFile brewJava() {
        TypeSpec.Builder routerHandler = TypeSpec
                .classBuilder(mTargetClass + Constants.ROUTER_SUFFIX)
                .addTypeVariable(TypeVariableName.get("T", ClassName.bestGuess(mTargetClass)))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(ParameterizedTypeName
                        .get(Constants.ClassNameList.IVIEWROUTER_INTERFACE, TypeVariableName.get("T")))
                .addMethod(createBindMethod());

        return JavaFile.builder(mPackageName, routerHandler.build())
                .addFileComment("Generated code from Router. Do not modify!")
                .build();
    }

    void addRouterParam(ViewRouteParam routerParam, Element element) {
        mRouterParamsMap.put(routerParam, element);
    }

    /**
     * 创建bind方法
     *
     * @return MethodSpec bind方法
     */
    private MethodSpec createBindMethod() {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bindData")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(TypeVariableName.get("T"), "router", Modifier.FINAL)
                .addParameter(Constants.ClassNameList.BUNDLE_CLASS, "bundle", Modifier.FINAL);

        builder.beginControlFlow("if (bundle != null)");
        for (Map.Entry<ViewRouteParam, Element> entry : mRouterParamsMap.entrySet()) {
            String[] routerParamValue = entry.getKey().value();
            for (String routerParam : routerParamValue) {
                builder.beginControlFlow("if (bundle.containsKey($S))", routerParam);
                builder.beginControlFlow("try");
                builder.addStatement("router.$L = $T.castValue(bundle.get($S))",
                        entry.getValue().getSimpleName(),
                        Constants.ClassNameList.VIEWROUTER_CLASS,
                        routerParam);
                builder.nextControlFlow("catch(Exception e)");
                builder.addStatement("e.printStackTrace()");
                builder.endControlFlow();
                builder.endControlFlow();
            }
        }
        builder.endControlFlow();
        return builder.build();
    }

    Map<ViewRouteParam, Element> getRouterParamsMap() {
        return mRouterParamsMap;
    }
}

