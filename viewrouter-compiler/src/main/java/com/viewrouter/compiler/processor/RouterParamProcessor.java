package com.viewrouter.compiler.processor;

import com.viewrouter.annotation.ViewRouteParam;
import com.viewrouter.compiler.utils.LogUtil;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * @author lxm
 * @createtime 2021/6/8
 * 参数解析
 */
class RouterParamProcessor {

    private final RoundEnvironment mEnv;
    private final Filer mFiler;
    private Elements mElementUtils;

    RouterParamProcessor(RoundEnvironment env,
                         Filer filer,
                         Elements elementUtils) {
        mEnv = env;
        mFiler = filer;
        mElementUtils = elementUtils;
    }

    Map<TypeElement, RouterParamParser> parse() {
        Map<TypeElement, RouterParamParser> routerParamMap = new LinkedHashMap<>();
        Set<? extends Element> routerParamSet = mEnv.getElementsAnnotatedWith(ViewRouteParam.class);

        for (Element elem : routerParamSet) {
            ViewRouteParam routerParam = elem.getAnnotation(ViewRouteParam.class);
            TypeElement typeElement = (TypeElement) elem.getEnclosingElement();

            if (!isInaccessibleViaGeneratedCode(elem, typeElement)) {
                RouterParamParser parser = getOrCreateRouterParamClass(routerParamMap, typeElement);
                if (parser != null) {
                    parser.addRouterParam(routerParam, elem);
                }
            }
        }

        for (Map.Entry<TypeElement, RouterParamParser> entry : routerParamMap.entrySet()) {
            try {
                entry.getValue().brewJava().writeTo(mFiler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return routerParamMap;
    }


    /**
     * 获取RouterParamParser
     *
     * @param routerParamsMap RouterParamsMap
     * @param element
     */
    private RouterParamParser getOrCreateRouterParamClass(
            Map<TypeElement, RouterParamParser> routerParamsMap,
            TypeElement element) {
        if (routerParamsMap == null || element == null) {
            error("routerParamsMap or element not valid");
            return null;
        }

        if (!routerParamsMap.containsKey(element)) {
            String classPackage = getPackageName(element);
            String targetClass = element.getSimpleName().toString();
            RouterParamParser parser = new RouterParamParser(classPackage, targetClass);
            routerParamsMap.put(element, parser);
        }

        return routerParamsMap.get(element);
    }

    /**
     * package
     *
     * @return string 包名
     */
    private String getPackageName(TypeElement element) {
        return mElementUtils.getPackageOf(element).getQualifiedName().toString();
    }

    /**
     *
     *
     * @param element
     * @param typeElement
     * @return boolean
     */
    private boolean isInaccessibleViaGeneratedCode(Element element,
                                                   TypeElement typeElement) {
        boolean hasError = false;

        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(PRIVATE) || modifiers.contains(STATIC)) {
            error("%s must not be private or static.(%s)",
                    element.getSimpleName(), typeElement.getQualifiedName());
            hasError = true;
        }

        // 校验是否Class.
        if (typeElement.getKind() != CLASS) {
            error("%s may only be contained in classes.(%s)",
                    element.getSimpleName(), typeElement.getQualifiedName());
            hasError = true;
        }

        // 校验Class是否私有.
        if (typeElement.getModifiers().contains(PRIVATE)) {
            error("%s may not be contained in private classes. (%s)",
                    element.getSimpleName(), typeElement.getQualifiedName());
            hasError = true;
        }

        return hasError;
    }

    /**
     * 打印错误日志
     *
     * @param message 错误日志
     * @param args 参数信息
     */
    private void error(String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        LogUtil.getInstance().error(message);
    }
}
