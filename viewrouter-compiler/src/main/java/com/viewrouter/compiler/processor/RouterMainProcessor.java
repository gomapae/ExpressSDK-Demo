package com.viewrouter.compiler.processor;

import com.google.auto.service.AutoService;
import com.viewrouter.annotation.ViewRouteParam;
import com.viewrouter.annotation.ViewRoute;
import com.viewrouter.compiler.utils.LogUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;


/**
 * @author lxm
 * @createtime 2021/6/8
 */
@AutoService(Processor.class)
public class RouterMainProcessor extends BaseProcessor {
    private Map<TypeElement, RouterParamParser> mRouterParamParserMap;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        LogUtil.getInstance().init(processingEnvironment.getMessager());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new HashSet<>();
        annotationTypes.add(ViewRoute.class.getCanonicalName());
        annotationTypes.add(ViewRouteParam.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment env) {
        parseRouterParamAnnotation(env);
        parserRouterAnnotation(env);
        return true;
    }

    private void parserRouterAnnotation(RoundEnvironment env) {
        new RouterProcessor(env, mFiler,mElementUtils,types,mRouterParamParserMap).parse();
    }

    private void parseRouterParamAnnotation(RoundEnvironment env) {
        mRouterParamParserMap = new RouterParamProcessor(env, mFiler, mElementUtils).parse();
    }
}
