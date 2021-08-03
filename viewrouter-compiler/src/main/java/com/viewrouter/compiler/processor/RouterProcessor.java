package com.viewrouter.compiler.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.viewrouter.annotation.ViewRoute;
import com.viewrouter.annotation.ViewRouteParam;
import com.viewrouter.compiler.utils.Constants;
import com.viewrouter.compiler.utils.LogUtil;
import com.viewrouter.compiler.utils.TypeUtils;
import com.viewrouter.model.ViewInfo;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * @author lxm
 * @createtime 2021/6/8
 */
class RouterProcessor {

    private final RoundEnvironment mEnv;
    private final Filer mFiler;
    private final Map<TypeElement, RouterParamParser> mParamParserMap;
    private Elements elementUtils;
    private Types types;

    private Map<String, Set<ViewInfo>> groupMap = new HashMap<>();

    RouterProcessor(RoundEnvironment env,
                    Filer filer,
                    Elements elementUtils,
                    Types types,
                    Map<TypeElement, RouterParamParser> map) {
        mEnv = env;
        mFiler = filer;
        this.types = types;
        this.elementUtils = elementUtils;
        mParamParserMap = map;
    }

    private void parseRoutes(){
        Set<? extends Element> routers = mEnv.getElementsAnnotatedWith(ViewRoute.class);
        for (Element element : routers) {
            TypeMirror tm = element.asType();
            ViewRoute router = element.getAnnotation(ViewRoute.class);

            TypeMirror typeView = elementUtils.getTypeElement(Constants.VIEW).asType();

            ViewInfo routeMeta;

            // Activity or Fragment
            if (types.isSubtype(tm, typeView)) {
                routeMeta = new ViewInfo();
                String group = router.group();
                String path = router.path();

                routeMeta.setGroup(group);
                routeMeta.setElement(element);
                routeMeta.setRoute(router);
                routeMeta.setPath(path);
            } else {
                throw new RuntimeException("The @Route is marked on unsupported class, look at [" + tm.toString() + "].");
            }
            categories(routeMeta);
        }

    }

    private void categories(ViewInfo viewInfo) {
        if (!viewInfo.getGroup().isEmpty()) {
            Set<ViewInfo> routeMetas = groupMap.get(viewInfo.getGroup());
            if (routeMetas == null || routeMetas.isEmpty()) {
                Set<ViewInfo> routeMetaSet = new TreeSet<>(new Comparator<ViewInfo>() {
                    @Override
                    public int compare(ViewInfo r1, ViewInfo r2) {
                        try {
                            return r1.getPath().compareTo(r2.getPath());
                        } catch (NullPointerException npe) {
                            LogUtil.getInstance().error(npe.getMessage());
                            return 0;
                        }
                    }
                });
                routeMetaSet.add(viewInfo);
                groupMap.put(viewInfo.getGroup(), routeMetaSet);
            } else {
                routeMetas.add(viewInfo);
            }
        } else {
            LogUtil.getInstance().warning(">>> Route meta verify error, group is " + viewInfo.getPath() + " <<<");
        }
    }

    /**
     * create routerMap file
     */
    private JavaFile brewJava(String group,Set<ViewInfo> viewInfos) {

        String groupName = Constants.ROUTER_MAP + Constants.SEPARATOR + group ;
//        String groupName = Constants.ROUTER_MAP ;

        TypeSpec.Builder routerHandler = TypeSpec.classBuilder(groupName)
                .addSuperinterface(Constants.ClassNameList.IROUTER_MAP_INTERFACE)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        routerHandler.addMethod(createInitMethod(viewInfos));

        return JavaFile
                .builder(Constants.ROUTER_BASE_PACKAGE, routerHandler.build())
                .addFileComment("Generated code from Router. Do not modify!")
                .build();
    }

    private MethodSpec createInitMethod(Set<ViewInfo> viewInfos) {

          /*
              ```Map<String, ViewInfo>```
             */
        ParameterizedTypeName inputMapTypeOfGroup = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(ViewInfo.class)
        );
        ParameterSpec groupParamSpec = ParameterSpec.builder(inputMapTypeOfGroup, "atlas").build();

        MethodSpec.Builder builder = MethodSpec.methodBuilder("loadInto")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(groupParamSpec);

        builder.addStatement("ViewInfo viewInfo = null");
        builder.addCode("\n");
        for (ViewInfo viewInfo : viewInfos) {
            ViewRoute router = viewInfo.getRoute();
            Element element = viewInfo.getElement();
            try {
                String value = router.path();
                if (value != null) {
                    ClassName className = ClassName.get((TypeElement) element);
                    builder.addStatement("viewInfo = new ViewInfo()");
                    builder.addStatement("viewInfo.setViewClass($T.class)", className);
                    for (Map.Entry<TypeElement, RouterParamParser> entry : mParamParserMap
                            .entrySet()) {
                        if (entry != null
                                && ClassName.get(entry.getKey()).compareTo(className) == 0) {
                            Map<ViewRouteParam, Element> routerParamElementMap = entry.getValue()
                                    .getRouterParamsMap();
                            for (Map.Entry<ViewRouteParam, Element> routerParamEntry : routerParamElementMap
                                    .entrySet()) {
                                String[] routerParamValue = routerParamEntry.getKey().value();
                                for (String routerParam : routerParamValue) {
                                    String typeName = routerParamEntry.getValue().asType()
                                            .toString();
                                    builder.addStatement("viewInfo.addParam($S, $T.class)",
                                            routerParam, TypeUtils.INSTANCE.getValidClassName(typeName));
                                }
                            }
                            break;
                        }
                    }
                    builder.addStatement("atlas.put($S, viewInfo)", value);
                    builder.addCode("\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return builder.build();
    }

    void parse() {
        try {
            parseRoutes();
            for (Map.Entry<String, Set<ViewInfo>> entry : groupMap.entrySet()) {
                brewJava(entry.getKey(),entry.getValue()).writeTo(mFiler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
